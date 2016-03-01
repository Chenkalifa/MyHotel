package hyperactive.co.il.mehearthotel;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tal on 02/02/2016.
 */
public class SummaryFragment extends DialogFragment implements View.OnClickListener {
    LinearLayout summaryBody;
    JSONObject reservationDetails;
    JSONArray tempJArray;
//    ParseObject reservation;
    List<Integer> freeRoomsList;
    int roomAmount = 0;
    int reservationNumber;
    int tempCounter = 0;
    boolean isGotResNumber;
    DateTime tempDate;
    MyProgressDialog progressDialog;
    Object signal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        signal=new Object();
        isGotResNumber=false;
        reservationDetails = ((RoomListActivity) getActivity()).getReservationDetails();
        View view = inflater.inflate(R.layout.summary_fragment_layout, container, false);
        summaryBody = (LinearLayout) view.findViewById(R.id.summary_fragment_summaryBody_container);
        TextView proceedTv = (TextView) view.findViewById(R.id.summary_proceedTv);
        ImageView ic_close = (ImageView) view.findViewById(R.id.ic_close);
        if (summaryBody != null) {
            View summary = getActivity().getLayoutInflater().inflate(R.layout.summary_single_row_layout, summaryBody, true);
            TableLayout summaryTable = (TableLayout) summary.findViewById(R.id.summary_booking_detail_conatiner);
            TextView checkinDayTv = (TextView) summary.findViewById(R.id.summary_checkin_dayTv);
            TextView checkinMonthTv = (TextView) summary.findViewById(R.id.summary_checkin_monthTv);
            TextView checkoutDayTv = (TextView) summary.findViewById(R.id.summary_checkout_dayTv);
            TextView checkoutMonthTv = (TextView) summary.findViewById(R.id.summary_checkout_monthTv);
            TextView totalAmountTv = (TextView) summary.findViewById(R.id.summary_payment_amountTv);
            TextView totalCurrencyTv = (TextView) summary.findViewById(R.id.summary_payment_currencyTv);
            TextView nightsTv= (TextView) summary.findViewById(R.id.summary_nightsTv);
            try {
                Log.i("myApp", "in summary - nights=" + reservationDetails.getInt("nights"));
                nightsTv.setText(Integer.toString(reservationDetails.getInt("nights")));
                tempDate = new DateTime(reservationDetails.getString("check_in"));
                checkinDayTv.setText(tempDate.dayOfMonth().get() + "");
                checkinMonthTv.setText(tempDate.monthOfYear().getAsText());
                tempDate = new DateTime(reservationDetails.getString("check_out"));
                Log.i("myApp", "in summary fragment, tempDate day=" + tempDate.dayOfMonth().get() + " month:" + tempDate.monthOfYear().getAsText());
                checkoutDayTv.setText(tempDate.dayOfMonth().get() + "");
                checkoutMonthTv.setText(tempDate.monthOfYear().getAsText());
                totalAmountTv.setText(reservationDetails.getString("totalPayment"));
                totalCurrencyTv.setText("USD"); // need to put currency variable to reservationDetails
                int amount = reservationDetails.getJSONObject("rooms").getInt("amount");
                for (int i = 0; i < amount; i++) {
                    View row = getActivity().getLayoutInflater().inflate(R.layout.summary_tablerow_layout, container, false);
                    TextView roomNumberTv = (TextView) row.findViewById(R.id.summary_tablerow_roomNumberTv);
                    TextView typeTv = (TextView) row.findViewById(R.id.summary_tablerow_typeTv);
                    TextView priceTv = (TextView) row.findViewById(R.id.summary_tablerow_priceTv);
                    JSONObject room = reservationDetails.getJSONObject("rooms").getJSONArray("details").getJSONObject(i);
                    Log.i("myApp", "adding a row details=" + room.toString());
                    roomNumberTv.setText("" + (i + 1));
                    typeTv.setText(room.getString("type"));
                    priceTv.setText("" + room.getInt("price"));
                    summaryTable.addView(row);
                }

            } catch (JSONException ex) {
                Log.e("myApp", "error", ex);

            }

//            checkinMonthTv.setText(calendar.get(Calendar.MONTH));
//            try{
//                tempStringDate=reservationDetails.getString("check_out");
//                date= sdf.parse(tempStringDate);
//                calendar.setTime(date);
//            }catch (JSONException ex){
//                Log.e("myApp", "error", ex);
//            }catch (ParseException ex){
//                Log.e("myApp", "error", ex);
//            }
//            Log.i("myApp", "in summary fragment, calendar day="+calendar.get(Calendar.DAY_OF_MONTH));
//            checkoutDayTv.setText("" + calendar.get(Calendar.DAY_OF_MONTH));
//            checkoutMonthTv.setText(calendar.get(Calendar.MONTH));
        }
        proceedTv.setOnClickListener(this);
        ic_close.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        final View vv = v;
        switch (vv.getId()) {
            case R.id.ic_close:
                getDialog().dismiss();
                break;
            case R.id.summary_proceedTv:
                proceedReservation();
                break;
        }

    }

//    private class FillReservationAndSave implements Runnable{
//
//        private String mtype;
//        public FillReservationAndSave(String mtype){
//            this.mtype=mtype;
//        }
//
//        @Override
//        public void run() {
//            final String type=mtype;
//            getReservationNumber(type);
//        }
//    }

    private void proceedReservation() {
        progressDialog = new MyProgressDialog(getActivity(), "Saving reservation", "please wait");
        progressDialog.show();
        try {
            tempJArray = reservationDetails.getJSONArray("freeRoomsList");
            freeRoomsList = new ArrayList<Integer>();
            for (int i = 0; i < tempJArray.length(); i++) {
                freeRoomsList.add(tempJArray.getInt(i));
            }
            roomAmount = reservationDetails.getJSONObject("rooms").getInt("amount");
            tempCounter = 0;
            for(int i = 0; i < roomAmount; i++){
                ParseObject reservation = new ParseObject("Reservations");
                reservation.put("nights", reservationDetails.getInt("nights"));
                reservation.put("arrive_date", new DateTime(reservationDetails.getString("check_in")).toDate());
                reservation.put("depart_date", new DateTime(reservationDetails.getString("check_out")).toDate());
                String type = reservationDetails.getJSONObject("rooms").getJSONArray("details")
                        .getJSONObject(i).getString("type").toLowerCase().replace(" ", "_");
                if(i==0){
                    ResNumberJob resNumberJob=new ResNumberJob();
                    resNumberJob.start();
                }
                FillResInfoJob fillResInfoJob=new FillResInfoJob(reservation, type);
                fillResInfoJob.start();
            }
        } catch (JSONException ex) {
            Log.e("myApp", "json error", ex);
        }
    }

    private class ResNumberJob extends Thread{
    @Override
    public void run() {
        ParseQuery<ParseObject> counterQuery = ParseQuery.getQuery("Global_Setting");
        counterQuery.whereEqualTo("objectId", "FduHMFExBg");
        counterQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    reservationNumber=object.getInt("counter");
                    try {
                        reservationDetails.put("resNumber",reservationNumber);
                    }catch (JSONException ex){
                        Log.e("myApp", "json error", ex);
                    }
                    Log.i("myApp", "counter=" + object.getInt("counter"));
                    object.increment("counter");
                    Log.i("myApp", "counter++=" + object.getInt("counter"));
                    try {
                        object.save();
                        notifyFillInfoJob();
                    } catch (ParseException e1) {
                        Log.e("myApp", "save parseObject error", e1);
                    }
                } else {
                    Log.i("myApp", e.getMessage());
                }
            }
        });
    }
    private void notifyFillInfoJob() {
        Log.i("myApp", "ResNumberJob, notifyFillInfoJob, reservationNumber="+reservationNumber);
        synchronized (signal){
            isGotResNumber=true;
            signal.notifyAll();
        }
    }
}


    private class FillResInfoJob extends Thread{
        ParseObject reservation;
        String type;
        public FillResInfoJob(ParseObject reservation, String type){
            this.reservation=reservation;
            this.type=type;
        }

        @Override
        public void run() {
            Log.i("myApp", "FillResInfoJob, started");
            if(!isGotResNumber){
                synchronized (signal){
                    try {
                        signal.wait();
                    } catch (InterruptedException e) {
                        Log.i("myApp", "FillResInfoJob, wait");
                    }
                }
            }
            Log.i("myApp", "FillResInfoJob, notified");
            reservation.put("reservation_number", reservationNumber);
            ParseQuery<ParseObject> roomQuery = ParseQuery.getQuery("Rooms");
            roomQuery.whereEqualTo("type", type);
            roomQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        int x = 0;
                        try {
                            while (!freeRoomsList.contains(objects.get(x).fetch().getInt("room_number"))) {
                                x++;
                            }
                            ParseObject room = objects.get(x).fetch();
                            reservation.put("room", room);
                            reservation.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("myApp", "reservation saved");
                                        checkIfNeedMoreRes();
                                    } else
                                        Log.i("myApp", e.getMessage());
                                }
                            });
                        } catch (ParseException e1) {
                            Log.e("myApp", "ParseException", e1);
                        }
                    } else {
                        Log.i("myApp", e.getMessage());
                    }
                }
            });

        }

        private void checkIfNeedMoreRes() {
            tempCounter++;
            Log.i("myApp", "checkIfNeedMoreRes - tempCounter="+tempCounter);
            if (tempCounter == roomAmount)
                ((RoomListActivity) getActivity()).postResSaved(reservationNumber);
        }
    }
}
