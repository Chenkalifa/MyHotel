package hyperactive.co.il.mehearthotel;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ReservationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    MyProgressDialog progressDialog;
    ContactFragment contactFragment;
    public static final String _sharedPreferences = "HotelSharedPreferences";
//    RelativeLayout;
    TextView check_outDateTv, check_inDateTv, numberOfDaysTv, continueResrvationBtn, check_inTV, check_outTv;
    Calendar arriveCalendar, departCalendar, currentDateCalendar, today, tempCal;
    JSONObject reservationDetails, rooms;
    Spinner roomsSpn, adultsSpn, childrenSpn;
    LinearLayout tableContainer, check_inView, check_outView;
        int[] ids, numbers;
    int roomLists;
    int year, month, day;
    //    ParseObject parseRoom;
    ParseObject reservation;
    List<List<ParseObject>> allDaysReservationLog;
    ArrayList<Integer> freeRooms;
    static int linesPrinted = 1;
    RoomsMap roomsMap;

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        TextView roomsTv=(TextView)findViewById(R.id.roomsTv);
//        TextView adultsTv=(TextView)findViewById(R.id.adultsTv);
//        TextView childrenTv=(TextView)findViewById(R.id.childrenTv);
//        int adultWidth=adultsTv.getWidth();
//        int childrenWidth=childrenTv.getWidth();
//        int roomWidt=roomsTv.getWidth();
//        Log.i("myApp", "somthing");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactFragment = new ContactFragment();
                contactFragment.show(getFragmentManager(), "contactFragment");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        roomsMap=new RoomsMap();
        tableContainer = (LinearLayout) findViewById(R.id.table_container);
        check_inTV= (TextView) findViewById(R.id.check_inTv);
        check_inTV.setTypeface(MyApp.FONT_MAIN);
        check_outTv= (TextView) findViewById(R.id.check_outTv);
        check_outTv.setTypeface(MyApp.FONT_MAIN);
        numberOfDaysTv = (TextView) findViewById(R.id.numberOfDaysTV);
        check_outDateTv = (TextView) findViewById(R.id.check_outDateTv);
        check_inDateTv = (TextView) findViewById(R.id.check_inDateTv);
        check_outView = (LinearLayout) findViewById(R.id.check_outView);
        check_inView = (LinearLayout) findViewById(R.id.check_inView);
        continueResrvationBtn = (TextView) findViewById(R.id.continueResrvationBtn);
        continueResrvationBtn.setTypeface(MyApp.FONT_SECONDARY);
        check_inDateTv.setTypeface(MyApp.FONT_SECONDARY);
        check_outDateTv.setTypeface(MyApp.FONT_SECONDARY);
        numberOfDaysTv.setTypeface(MyApp.FONT_MAIN);
        reservationDetails = new JSONObject();
        rooms = new JSONObject();
        roomsSpn = (Spinner) findViewById(R.id.roomsSpinner);
        adultsSpn = (Spinner) findViewById(R.id.adultsSpinner);
        childrenSpn = (Spinner) findViewById(R.id.childrenSpinner);
        arriveCalendar = Calendar.getInstance();
        departCalendar = Calendar.getInstance();
        departCalendar.add(Calendar.DAY_OF_YEAR, 1);
        currentDateCalendar = Calendar.getInstance();
        numbers = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        today = Calendar.getInstance();
        ids = getResources().getIntArray(R.array.rooms);
        Integer[] items = new Integer[ids.length];
        int i = 0;
        for (int value : ids) {
            items[i++] = Integer.valueOf(value);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        roomsSpn.setAdapter(adapter);
    }

    private void saveReseravtion()/*temp method for parctice only!*/ {

        final int next = new Random().nextInt(numbers.length);
        ParseUser currentUser = ParseUser.getCurrentUser();
        reservation = new ParseObject("Reservations");
        if (currentUser != null) {
            ParseRelation relation = reservation.getRelation("user");
            relation.add(currentUser);
        }
        try{
            DateTime checkin=new DateTime(arriveCalendar);
            reservationDetails.put("check_in", checkin);
            DateTime checkout=new DateTime(departCalendar);
            reservationDetails.put("check_out", checkout);
            DateTime tempJoda=new DateTime(reservationDetails.get("check_in"));
            Date temp=tempJoda.toDate();
            reservation.put("arrive_date", temp);
            tempJoda=new DateTime(reservationDetails.get("check_out"));
            temp=tempJoda.toDate();
            reservation.put("depart_date", temp);
        }catch (JSONException ex){
            Log.e("myApp", "json error", ex);
        }

        reservation.put("nights", (int) checkDatesGap());
        ParseQuery<ParseObject> roomQuery = ParseQuery.getQuery("Rooms");
        roomQuery.whereEqualTo("room_number", next);
        roomQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    reservation.put("room", object);
                    Log.i("myApp", "objectID:" + object.getObjectId()
                            + "\nobjectClass:" + object.getClassName()
                            + "\nobjectRoomNumber:" + object.get("room_number"));
//                    ParseObject.createWithoutData("Rooms", parseRoom.getObjectId());
                    getReservationNumberAndSave();
                } else {
                    Log.i("myApp", e.getMessage());
                }
            }

            private void getReservationNumberAndSave() {
                ParseQuery<ParseObject> counterQuery = ParseQuery.getQuery("Global_Setting");
                counterQuery.whereEqualTo("objectId", "FduHMFExBg");
                counterQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            object.increment("counter");
                            try {
                                object.save();
                            } catch (ParseException e1) {
                                Log.e("myApp", "save parseObject eror", e1);
                            }
                            int number = (int) object.get("counter");
                            reservation.put("reservation_number", number);
                            reservation.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null)
                                        Log.i("myApp", "reservation saved");
                                    else
                                        Log.i("myApp", e.getMessage());
                                }
                            });
                            Log.i("myApp", "counter=" + object.get("counter"));
                        } else {
                            Log.i("myApp", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Intent intent = getIntent();
            if (intent != null) {
                if(intent.hasExtra("type")){
                    rooms.getJSONArray("details").getJSONObject(0).put("type", intent.getStringExtra("type"));
                    Log.i("myApp", intent.getStringExtra("type"));
                }
            }
        } catch (NullPointerException ex) {
            Log.e("myApp", "no room was chosen yet", ex);
        } catch (JSONException e) {
            Log.e("myApp", "JSON exception", e);
        }

//        SharedPreferences sharedPreferences=getSharedPreferences(_sharedPreferences, Activity.MODE_PRIVATE);
//        Log.i("myApp", sharedPreferences.getString("room", "room wasn't chosen yet"));
        updateDates();
        roomsSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.i("myApp", "room " + parent.getItemAtPosition(position));
                addLines(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        continueResrvationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressDialog=new MyProgressDialog(ReservationActivity.this, "Checking availability", "please wait");
                    progressDialog.show();
                    JSONArray roomsDetailsArr = new JSONArray();
                    DateTime checkin=new DateTime(arriveCalendar);
                    reservationDetails.put("check_in", checkin);
                    DateTime checkout=new DateTime(departCalendar);
//                    String.format("%tD", arriveCalendar
                    reservationDetails.put("check_out", checkout);
                    reservationDetails.put("nights", checkDatesGap());
                    rooms.put("amount", roomsSpn.getSelectedItem());
//                    reservationDetails.put("rooms", roomsSpn.getSelectedItem());
                    int childViewNum = tableContainer.getChildCount();
                    Log.i("myApp", "in res, cuntinue, childNumber="+childViewNum);
                    int index = childViewNum - 1;
                    for (int i = 2; i <= index; i++) {
                        JSONObject roomInfo = new JSONObject();
                        Spinner tempAdultSpn = null;
                        Spinner tempChildSpn = null;
                        if (i == 2) {
                            tempAdultSpn = adultsSpn;
                            tempChildSpn = childrenSpn;
                        } else {
                            LinearLayout view = (LinearLayout) tableContainer.getChildAt(i);
                            tempAdultSpn = (Spinner) view.getChildAt(1);
                            tempChildSpn = (Spinner) view.getChildAt(2);
                        }
                        roomInfo.put(getResources().getString(R.string.adults), tempAdultSpn.getSelectedItem());
                        roomInfo.put(getResources().getString(R.string.children), tempChildSpn.getSelectedItem());
                        roomsDetailsArr.put(roomInfo);
//                        String roomNumber=getResources().getString(R.string.room)+(i+1);
//                        reservationDetails.put(roomNumber+getResources().getString(R.string.adults), tempAdultSpn.getSelectedItem());
//                        reservationDetails.put(roomNumber+getResources().getString(R.string.children), tempChildSpn.getSelectedItem());
                    }
                    rooms.put("details", roomsDetailsArr);
                    reservationDetails.put("rooms", rooms);
                    checkRoomsAvailability();
                } catch (JSONException e) {
                    Log.e("myApp","JSON exception", e);
                }
            }
        });
        View.OnClickListener showDatePicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View vv = v;
                int mYear, mMonth, mDay;
                if (vv.getId() == R.id.check_inView) {
                    mYear = arriveCalendar.get(Calendar.YEAR);
                    mMonth = arriveCalendar.get(Calendar.MONTH);
                    mDay = arriveCalendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    mYear = departCalendar.get(Calendar.YEAR);
                    mMonth = departCalendar.get(Calendar.MONTH);
                    mDay = departCalendar.get(Calendar.DAY_OF_MONTH);
                }
//                Calendar mcurrentDate=Calendar.getInstance();
//                int mYear=mcurrentDate.get(Calendar.YEAR);
//                int mMonth=mcurrentDate.get(Calendar.MONTH);
//                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(ReservationActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        currentDateCalendar.set(selectedyear, selectedmonth, selectedday);
                        if (checkCurrentDate()) {
                            if (vv.getId() == R.id.check_inView)
                                arriveCalendar.set(selectedyear, selectedmonth, selectedday, 12, 0);
                            else
                                departCalendar.set(selectedyear, selectedmonth, selectedday, 11, 0);
                            updateDates();
                        }
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select your stay dates");
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePicker.show();
            }
        };
        check_inView.setOnClickListener(showDatePicker);
        check_outView.setOnClickListener(showDatePicker);
    }

    public void checkRoomsAvailability() {
        roomLists = 0;
        final int nights = (int) checkDatesGap();
        allDaysReservationLog = new ArrayList<List<ParseObject>>();
        tempCal = Calendar.getInstance();
        tempCal.set(Calendar.DAY_OF_MONTH, arriveCalendar.get(Calendar.DAY_OF_MONTH));
        tempCal.set(Calendar.MONTH, arriveCalendar.get(Calendar.MONTH));
        tempCal.set(Calendar.YEAR, arriveCalendar.get(Calendar.YEAR));
        for (int i = 0; i < nights; i++) {
            final Date date = tempCal.getTime();
            Log.i("myApp", date.toString());
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Reservations");
            query.whereLessThan("arrive_date", date);
            query.whereGreaterThanOrEqualTo("depart_date", date);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> reservations, ParseException e) {
                    if (e == null) {
                        roomLists++;
                        Log.i("myApp", "in " + date.toString() + " " + reservations.size() + " rooms are occupied");
                        allDaysReservationLog.add(reservations);
                        if (roomLists == nights && roomLists == allDaysReservationLog.size())
                            logOccupiedRooms();
                    } else {
                        Log.d("myApp", "Error: " + e.getMessage());
                    }
                }

                private void logOccupiedRooms() {
                    List<Integer> occupiedRoomsList = new ArrayList<Integer>();
//                    Log.i("myApp", "inside logOccupiedRooms");
//                    Log.i("myApp", "number of lists=" + allDaysReservationLog.size());
                    for (List<ParseObject> dayList : allDaysReservationLog) {
                        int counter = 1;
                        Log.i("myApp", "List:" + counter);
                        for (ParseObject reservation : dayList) {

                            ParseObject obj = reservation.getParseObject("room");
                            if (obj != null) {
//                                Log.i("myApp", "room id:" + room.getObjectId());
                                try {
                                    ParseObject room = obj.fetch();
                                    int roomNumber = room.getInt("room_number");
                                    if (!occupiedRoomsList.contains(roomNumber))
                                        occupiedRoomsList.add(roomNumber);
                                    Log.i("myApp", "room id:" + room.getObjectId()
                                            + " room number:" + roomNumber);
                                } catch (ParseException e1) {
                                    Log.e("myApp", "ParseException", e1);
                                }
                            }
                        }
                        counter++;
                    }
                    List<Integer> roomList = new ArrayList<Integer>();
                    for (int i = 0; i < numbers.length; i++) {
                        roomList.add(numbers[i]);
                    }
                    ArrayList<Integer> freeRoomsList = new ArrayList<Integer>(roomList);
                    freeRoomsList.removeAll(occupiedRoomsList);
                    Log.i("myApp", "free rooms:" + freeRoomsList.toString());
                    try {
//                        JSONArray occupiedRoomsListJSONArray = new JSONArray();
//                        for (int i=0; i < occupiedRoomsList.size(); i++) {
//                            occupiedRoomsListJSONArray.put(occupiedRoomsList.get(i));
//                        }
//                        JSONArray freeRoomsListJSONArray = new JSONArray();
//                        for (int i=0; i < freeRoomsList.size(); i++) {
//                            freeRoomsListJSONArray.put(freeRoomsList.get(i));
//                        }
                        reservationDetails.put("occupiedRoomsList", new JSONArray(occupiedRoomsList));
                        reservationDetails.put("freeRoomsList", new JSONArray(freeRoomsList));
                        List<String> occupiedRoomsTypeList = new ArrayList<String>();
                        for (int i = 0; i < occupiedRoomsList.size(); i++) {
                            Log.i("myApp", "iterting over occupiedRoomsList," + occupiedRoomsList.get(i));
                            occupiedRoomsTypeList.add(roomsMap.getRoomType(occupiedRoomsList.get(i)));
                        }
                        reservationDetails.put("occupiedRoomsTypeList", new JSONArray(occupiedRoomsTypeList));
                        int roomAmount = (int) roomsSpn.getSelectedItem();
//                        progressDialog.dismiss();
                        Log.i("myApp", reservationDetails.toString());
                        callRoomListActivity(roomAmount);
                    } catch (JSONException ex) {
                        Log.e("myApp", "JSON exception", ex);
                    }
//                    for(Integer i:freeRoomsList)
                }
            });
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void callRoomListActivity(int roomAmount) {
        progressDialog.dismiss();
        if (freeRooms != null) {

        }
        try {
            if (roomAmount == 1 && (rooms.getJSONArray("details").getJSONObject(0).has("type"))) {
                // go directly to Booking Summary fragment
                Log.i("myApp", " go directly to Booking Summary fragment");

            } else {
                Intent intent = new Intent(this, RoomListActivity.class);
                intent.putExtra("calling-activity", CallingActivity.RESERVATION_ACTIVITY);
                intent.putExtra("reservationDetails", reservationDetails.toString());
                intent.putExtra("arrivedFirstTime", true);
                intent.putIntegerArrayListExtra("free_rooms", freeRooms);
                intent.putExtra("amount", roomAmount);
                startActivity(intent);
            }
        } catch (JSONException e) {
            Log.e("myApp", "JSON exception", e);
        }
    }

    private void addLines(Object itemAtPosition) {
        int num = (int) itemAtPosition;
        int removeLines=num+3;
        int spinner_width=roomsSpn.getMeasuredWidth();
        LayoutInflater inflater = getLayoutInflater();
        Log.i("myApp","in res, addlines, spinner_width="+spinner_width);
        for (; linesPrinted < num; linesPrinted++) {
            View lineView = inflater.inflate(R.layout.line_layout, tableContainer, true);
            TextView roomLineTv = (TextView) lineView.findViewById(R.id.line_roomsTv);
            roomLineTv.setText(getResources().getString(R.string.room)+" " + (linesPrinted+1));
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(spinner_width, LinearLayout.LayoutParams.WRAP_CONTENT);
            roomLineTv.setLayoutParams(lp);
            Log.i("myApp", "next child index: " + (tableContainer.getChildCount() + 1));
        }
        for (; linesPrinted > num; linesPrinted--) {
            int childCount=tableContainer.getChildCount();
            Log.i("myApp", "childCount="+childCount);
            tableContainer.removeView(tableContainer.getChildAt(childCount - 1));
        }
    }

    private void setDatesInfo() {
        check_inDateTv.setText(String.format("%1$tb %1$td\n%1$tY", arriveCalendar));
        check_outDateTv.setText(String.format("%1$tb %1$td\n%1$tY", departCalendar));
    }

    private boolean checkCurrentDate() {
        if (currentDateCalendar.compareTo(today) < 0) {
            MyToast.makeToast(ReservationActivity.this, "can't book past days");
//            Toast.makeText(ReservationActivity.this, "can't book past days", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private float checkDatesGap() {
        long diff = departCalendar.getTimeInMillis() - arriveCalendar.getTimeInMillis();
        float dayCount = (float) diff / (23 * 60 * 60 * 1000);
        if((int)dayCount==0)
            dayCount=1;
        return dayCount;
    }

    private boolean updateDates() {
        boolean isDatesChanged = false;
        if (!(departCalendar.compareTo(arriveCalendar) > 0)) {
            departCalendar = (Calendar) arriveCalendar.clone();
            departCalendar.add(Calendar.DAY_OF_YEAR, 1);
            isDatesChanged = true;
        }
        numberOfDaysTv.setText(getResources().getString(R.string.nights)+": "+(int) checkDatesGap());
        setDatesInfo();
        Log.i("myApp", "arrive date: " + arriveCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (arriveCalendar.get(Calendar.MONTH) + 1) + "/" + arriveCalendar.get(Calendar.YEAR));
        Log.i("myApp", "depart date: " + departCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (departCalendar.get(Calendar.MONTH) + 1) + "/" + departCalendar.get(Calendar.YEAR));
        return isDatesChanged;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(ReservationActivity.this, HomeScreenActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(ReservationActivity.this, TouristInfoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_rooms) {
            Intent intent = new Intent(ReservationActivity.this, RoomsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_myprofile) {
            Intent intent = new Intent(ReservationActivity.this, MyProfileActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_reserv) {
            Intent intent = new Intent(ReservationActivity.this, ReservationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            contactFragment = new ContactFragment();
            contactFragment.show(getFragmentManager(), "contactFragment");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
