package hyperactive.co.il.mehearthotel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Tal on 14/02/2016.
 */
public class SummaryAdapter extends ArrayAdapter {
    Context context;
    List<JSONObject> reservations;

    public SummaryAdapter(Context context, int resource, List<JSONObject> reservations) {
        super(context, resource, reservations);
        this.context=context;
        this.reservations=reservations;

    }

    class ViewHolder{
        TableLayout summaryTable;
        TextView checkinDayTv;
        TextView checkinMonthTv;
        TextView checkoutDayTv;
        TextView checkoutMonthTv;
        TextView totalAmountTv;
        TextView totalCurrencyTv;
        TextView nightsTv;

        public ViewHolder(View v, LayoutInflater inflater, JSONObject reservationDetails, ViewGroup parent){
            summaryTable = (TableLayout) v.findViewById(R.id.summary_booking_detail_conatiner);
            checkinDayTv = (TextView) v.findViewById(R.id.summary_checkin_dayTv);
            checkinMonthTv = (TextView) v.findViewById(R.id.summary_checkin_monthTv);
            checkoutDayTv = (TextView) v.findViewById(R.id.summary_checkout_dayTv);
            checkoutMonthTv = (TextView) v.findViewById(R.id.summary_checkout_monthTv);
            totalAmountTv = (TextView) v.findViewById(R.id.summary_payment_amountTv);
            totalCurrencyTv = (TextView) v.findViewById(R.id.summary_payment_currencyTv);
            nightsTv= (TextView) v.findViewById(R.id.summary_nightsTv);
            try{
                int rowAmount = reservationDetails.getJSONObject("rooms").getInt("amount");
            for (int i = 0; i < rowAmount; i++) {
                View row = inflater.inflate(R.layout.summary_tablerow_layout, parent, false);
                TextView roomNumberTv = (TextView) row.findViewById(R.id.summary_tablerow_roomNumberTv);
                TextView typeTv = (TextView) row.findViewById(R.id.summary_tablerow_typeTv);
                TextView priceTv = (TextView) row.findViewById(R.id.summary_tablerow_priceTv);
                JSONObject room = reservationDetails.getJSONObject("rooms").getJSONArray("details").getJSONObject(i);
                Log.i("myApp", "adding a row details=" + room.toString());
                roomNumberTv.setText(String.format("%d", (i + 1)));
                typeTv.setText(room.getString("type"));
                priceTv.setText(String.format("%d", room.getInt("price")));
                boolean stopPrintRows= summaryTable.getChildCount()>rowAmount;
                if(!stopPrintRows)
                    summaryTable.addView(row);
            }
            }catch (JSONException ex){
                Log.e("myApp", "error", ex);
            }

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        ViewHolder holder;
        JSONObject reservationDetails =reservations.get(position);
        if (convertView==null){
            inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.summary_single_row_layout, parent, false);
            holder=new ViewHolder(convertView, inflater, reservationDetails, parent);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }
        try {
            DateTime tempDate = new DateTime(reservationDetails.getString("check_in"));
            holder.nightsTv.setText(Integer.toString(reservationDetails.getInt("nights")));
            Log.i("myApp", "in summary fragment, tempDate day=" + tempDate.dayOfMonth().get() + " month:" + tempDate.monthOfYear().getAsText());
            holder.checkinDayTv.setText(String.format("%d", tempDate.dayOfMonth().get()));
            holder.checkinMonthTv.setText(tempDate.monthOfYear().getAsText());
            tempDate = new DateTime(reservationDetails.getString("check_out"));
            Log.i("myApp", "in summary fragment, tempDate day=" + tempDate.dayOfMonth().get() + " month:" + tempDate.monthOfYear().getAsText());
            holder.checkoutDayTv.setText(String.format("%d",tempDate.dayOfMonth().get()));
            holder.checkoutMonthTv.setText(tempDate.monthOfYear().getAsText());
            holder.totalAmountTv.setText(reservationDetails.getString("totalPayment"));
            holder.totalCurrencyTv.setText("USD"); // need to put currency variable to reservationDetails
            int amount = reservationDetails.getJSONObject("rooms").getInt("amount");
            Log.i("myApp", "amount=" + amount);
            Log.i("myApp", reservationDetails.toString());
            Log.i("myApp", "position="+position);
//            for (int i = 0; i < amount; i++) {
//                View row = inflater.inflate(R.layout.summary_tablerow_layout, parent, false);
//                TextView roomNumberTv = (TextView) row.findViewById(R.id.summary_tablerow_roomNumberTv);
//                TextView typeTv = (TextView) row.findViewById(R.id.summary_tablerow_typeTv);
//                TextView priceTv = (TextView) row.findViewById(R.id.summary_tablerow_priceTv);
//                JSONObject room = reservationDetails.getJSONObject("rooms").getJSONArray("details").getJSONObject(i);
//                Log.i("myApp", "adding a row details=" + room.toString());
//                roomNumberTv.setText(String.format("%d", (i + 1)));
//                typeTv.setText(room.getString("type"));
//                priceTv.setText(String.format("%d", room.getInt("price")));
//                boolean stopPrintRows= holder.summaryTable.getChildCount()>amount;
//                if(!stopPrintRows)
//                    holder.summaryTable.addView(row);
//            }

        } catch (JSONException ex) {
            Log.e("myApp", "error", ex);

        }
        return convertView;
    }


    @Override
    public int getCount() {
        return reservations.size();
    }

}
