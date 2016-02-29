package hyperactive.co.il.mehearthotel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends AppCompatActivity {

    JSONArray resHistory;
    SharedPreferences resHistoryFile;
    static final String RES_HISTORY = "reservationHistory";
    ListView roomList;
    RoomListAdapter adapter;
    List<RowItem> items;
    static boolean isFirstArrive = true;
    String room;
    String explanation;
    TextView continueBtn;
    JSONObject reservationDetails;
    Intent callingIntent;
    static int callingActivity = -1;
    static boolean isPressedBackFromFragment = false;
    static final String MY_ITEMS_LIST = "list";
    static final int SEND_ACTIVITY_RESULT = 1;
    boolean isGoingBackToReservation = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isPressedBackFromFragment = false;
        isGoingBackToReservation = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_list);
        getSupportActionBar().setTitle(getResources().getString(R.string.room_list_title));
        roomList = (ListView) findViewById(R.id.roomList);
        room = getResources().getString(R.string.room);
        explanation = getResources().getString(R.string.explanation);
        continueBtn = (TextView) findViewById(R.id.continueBtn);
        continueBtn.setTypeface(MyApp.FONT_SECONDARY);
    }

    protected void onResume() {
        super.onResume();
        Log.i("myApp", "0nResume");
        manageRoomList();
        adapter = new RoomListAdapter(this, items);
        roomList.setAdapter(adapter);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalPayment = 0;
                try {
                    for (int i = 0; i < items.size(); i++) {
                        String[] desc = items.get(i).getDescription().split((room + " " + (i + 1) + " "));
                        int price = items.get(i).getPrice();
                        totalPayment += price;
                        reservationDetails.getJSONObject("rooms").getJSONArray("details").getJSONObject(i).put("type", desc[1]);
                        reservationDetails.getJSONObject("rooms").getJSONArray("details").getJSONObject(i).put("price", price);
                    }
                    reservationDetails.put("paymentPerNight", totalPayment);
                    reservationDetails.put("totalPayment", totalPayment * reservationDetails.getInt("nights"));
                } catch (JSONException e) {
                    Log.i("myApp", "error on json");
                }
                Log.i("myApp", reservationDetails.toString());
                SummaryFragment summaryFragment = new SummaryFragment();
                summaryFragment.show(getFragmentManager(), "summaryFragment");
            }
        });
        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RoomListActivity.this, RoomsActivity.class);
                intent.putExtra("calling-activity", CallingActivity.ROOMS_LIST_ACTIVITY);
                intent.putExtra("position", position);
                intent.putExtra("reservationDetails", reservationDetails.toString());
                startActivityForResult(intent, SEND_ACTIVITY_RESULT);
            }
        });
    }

    public void postResSaved(int reservationNumber) {
        MyToast.makeToast(this, "reservation saved!");
//        Toast.makeText(this, "reservation saved!", Toast.LENGTH_LONG).show();
        final int resNumber = reservationNumber;
        new Thread() {
            @Override
            public void run() {
                String hotelEmail=getResources().getString(R.string.hotelEmail);
                String hotelName=getResources().getString(R.string.hotelName);
                Mail mail = new Mail(hotelEmail, "hotellikeme");
                mail.set_from(hotelEmail);
                mail.set_subject("Welcome to "+hotelName+"!");
                try {
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
                    String checkIn=fmt.print(new DateTime(reservationDetails.getString("check_in")));
                    String checkOut=fmt.print(new DateTime(reservationDetails.getString("check_in")));
                    int totalPayment = reservationDetails.getInt("totalPayment");
                    int nights=reservationDetails.getInt("nights");
                    int roomAmount=reservationDetails.getJSONObject("rooms").getInt("amount");
                    String currency=getResources().getString(R.string.currency);
                    JSONArray roomsArray=reservationDetails.getJSONObject("rooms").getJSONArray("details");
                    StringBuilder subject = new StringBuilder();
                    subject.append("Hello, ").append(ParseUser.getCurrentUser().getUsername()).append("!").append("\n")
                            .append("MeHeartHotel staff welcomes you,\n")
                            .append("And glad you chose us for your next holiday\n\n")
                            .append("Your reservation details:\n")
                            .append("  Reservation number: ").append(resNumber).append("\n")
                            .append("  Check-In: ").append(checkIn).append(", Check-Out: ").append(checkOut).append("\n")
                            .append("  Total stay: ").append(nights).append(" night").append((nights > 1 ? "s" : "")).append("\n")
                            .append("  Rooms:\n");
                    for(int i=0;i<roomAmount;i++){
                        subject.append("    *Room no.").append((i+1))
                                .append(" ").append(roomsArray.getJSONObject(i).getString("type"))
                                .append("\n");
                    }
                    subject.append("  Total payment: ").append(totalPayment).append(" ").append(currency).append("\n")
                            .append("\n")
                            .append("\n")
                            .append("We're looking forward to your arrival\n")
                            .append("And hope you'll enjoy your stay at the hotel");
                    String[] to = {ParseUser.getCurrentUser().getEmail()};
                    mail.set_body(subject.toString());
                    mail.set_to(to);
                } catch (JSONException ex) {
                    Log.e("myApp", "json error", ex);
                }
                try {
                    mail.send();
                    Log.i("myApp", "Succeeded!");
                } catch (Exception e) {
                    Log.e("myApp", "failed", e);
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                FileInputStream fileInputStream = null;
                FileOutputStream fileOutputStream = null;
                StringBuffer stringBuffer = new StringBuffer();
                try {
                    File folder = new File(Environment.getExternalStorageDirectory() + File.separator + ParseUser.getCurrentUser().getUsername());
                    boolean success = true;
                    if (!folder.exists()) {
                        success = folder.mkdir();
                    }
                    if (success) {
                        File resFile = new File(folder, "res.txt");
                        Log.i("myApp", "roomListActivity res file=" + resFile.getAbsolutePath());
                        if (resFile.exists()) {
                            fileInputStream = new FileInputStream(resFile);
                            int read = -1;
                            while ((read = fileInputStream.read()) != -1) {
                                stringBuffer.append((char) read);
                            }
                        }
                        if (stringBuffer == null || stringBuffer.length() == 0)
                            resHistory = new JSONArray();
                        else
                            resHistory = new JSONArray(stringBuffer.toString());

//            if (temp.equals("empty"))
//                resHistory = new JSONArray();
//            else
//                try {
//                    resHistory = new JSONArray(temp);
//                } catch (JSONException e) {
//                    Log.e("myApp", "json error", e);
//                }
                        if (reservationDetails != null)
                            resHistory.put(reservationDetails);
                        Log.i("myApp", "roomListActivity resHistory=" + resHistory.toString());
                        fileOutputStream = new FileOutputStream(resFile);
                        fileOutputStream.write(resHistory.toString().getBytes());
                    }
                } catch (FileNotFoundException e) {
                    Log.e("myApp", "roomListActivity file error", e);
                } catch (IOException e) {
                    Log.e("myApp", "roomListActivity I/O error", e);
                } catch (JSONException e) {
                    Log.e("myApp", "roomListActivity JSON error", e);
                } finally {
                    try {
                        if (fileInputStream != null)
                            fileInputStream.close();
                        if (fileOutputStream != null)
                            fileOutputStream.close();
                    } catch (IOException e) {
                        Log.e("myApp", "roomListActivity I/O error", e);
                    }
                    Intent intent = new Intent(RoomListActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                }
                //        getSharedPreferences(RES_HISTORY, Activity.MODE_PRIVATE).edit().putString("history", resHistory.toString()).commit();
            }
        }.start();
    }

    private void manageRoomList() {
        isGoingBackToReservation = false;

        if (callingIntent == null)
            callingIntent = getIntent();
        RoomListActivity.callingActivity = callingIntent.getIntExtra("calling-activity", -1);
        switch (RoomListActivity.callingActivity) {
            case CallingActivity.RESERVATION_ACTIVITY:
                Log.i("myApp", "reservation called");
                getItemsList();
                break;
            case CallingActivity.ROOMS_ACTIVITY:
                Log.i("myApp", "rooms called");
                break;
            default:
                Log.i("myApp", "callingActivty:" + RoomListActivity.callingActivity);
                callingIntent = getIntent();
                RoomListActivity.callingActivity = callingIntent.getIntExtra("calling-activity", -1);
                break;
        }


    }

    private void getItemsList() {
        try {
            if (!isFirstArrive && !isPressedBackFromFragment) {
                FileInputStream fileInputStream = openFileInput(MY_ITEMS_LIST);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                items = (ArrayList<RowItem>) objectInputStream.readObject();
                Log.i("myApp", "reading" + items.toString());
                objectInputStream.close();
                if (items.size() > 0)
                    Log.i("myApp", "from fileInput, item1 image:" + items.get(0).getImage());
            } else {
                items = new ArrayList<RowItem>();
            }
            if (callingIntent != null) {
                try {
                    reservationDetails = new JSONObject(callingIntent.getStringExtra("reservationDetails"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int amount = callingIntent.getIntExtra("amount", 1);
                int rows = isFirstArrive ? 0 : items.size();
                boolean moreRooms = rows < amount;
                if (moreRooms) {
                    for (int i = rows; i < amount; i++) {
                        String description = room + " " + (i + 1) + " " + explanation;
                        int image = -1;
                        int price = 0;
                        items.add(new RowItem(description, price, image));
                        Log.i("myApp", description + " was added");
                    }
                } else {
                    for (int i = rows; i > amount; i--) {
                        Log.i("myApp", items.get(i - 1).getDescription() + " was removed");
                        items.remove(i - 1);
                    }
                }
                isAllRoomsFilled();
                isFirstArrive = false;
            }
        } catch (NullPointerException ex) {
            Log.e("myApp", "NullPointer", ex);
        } catch (IOException ex) {
            Log.e("myApp", "IOException", ex);
        } catch (ClassNotFoundException ex) {
            Log.e("myApp", "ClassNotFound", ex);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEND_ACTIVITY_RESULT && data != null) {
            callingIntent = data;
            RoomListActivity.callingActivity = callingIntent.getIntExtra("calling-activity", -1);
            int position = data.getIntExtra("position", -1);
            String type = data.getStringExtra("type");
            int image = data.getIntExtra("image", -1);
            int price = data.getIntExtra("price", 0);
            if (position >= 0 && image >= 0) {
                RowItem item = items.get(position);
                item.setDescription(room + " " + (position + 1) + " " + type);
                item.setImage(image);
                item.setPrice(price);
                items.set(position, item);
                adapter.notifyDataSetChanged();
                isAllRoomsFilled();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("myApp", "onPause");
//        Log.i("myApp", items.toString());
        if (isGoingBackToReservation) {
            try {
                FileOutputStream fileOutputStream = openFileOutput(MY_ITEMS_LIST, Activity.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(items);
                Log.i("myApp", "writing" + items.toString());
                if (items.size() > 0) {
                    for (int i = 0; i < items.size(); i++) {
                        Log.i("myApp", "item" + i + "image:" + items.get(i).getImage());
                    }
                }
                objectOutputStream.flush();
                objectOutputStream.close();
            } catch (IOException ex) {
                Log.e("myApp", "IOException", ex);
            }
        }
    }

    public JSONObject getReservationDetails() {
        return reservationDetails;
    }

    private boolean isAllRoomsFilled() {
        for (RowItem item : items) {
            if (item.getImage() == -1)
                return false;
        }
        continueBtn.setVisibility(View.VISIBLE);
        return true;
    }
}
