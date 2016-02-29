package hyperactive.co.il.mehearthotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tal on 27/12/2015.
 */
public class RoomFragment extends Fragment {

    public static final String _roomType ="text";
    public static final String _roomImage="image";
    public static final String _roomPrice="price";
    public static final String _sharedPreferences="HotelSharedPreferences";
    Context context;
    int availableAmount;
    int roomNumber;
    List<Integer> occupiedRoomsList, freeRoomsList;
    List<String> occupiedRoomsTypeList;
    JSONObject reservationDetails;

    public static final RoomFragment newInstance(Context context, String roomType,int price, int image){
        RoomFragment rf=new RoomFragment();
        Bundle bundle=new Bundle();
        bundle.putString(_roomType, roomType);
        bundle.putInt(_roomImage, image);
        bundle.putInt(_roomPrice, price);
        rf.setArguments(bundle);
        rf.context=context;
        return rf;
    }
//     @Override
//    public void onDetach() {
//         Log.i("myApp", "in room fragment onDetach..");
//         RoomListActivity.isPressedBackFromFragment=true;
////         (RoomListActivity)getActivity()
//         super.onDetach();
//    }
//
    @Override
    public void onResume() {
        super.onResume();
        Log.i("myApp", "in room fragment onResume..");
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    RoomListActivity.isPressedBackFromFragment=true;
//                    // handle back button's click listener
//                    return true;
//                }
//                return false;
//            }
//        });
    }



    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("myApp", "in room fragment onCreateView..");
        Intent intent=getActivity().getIntent();
        JSONArray tempJArray;
        occupiedRoomsList=new ArrayList<Integer>();
        freeRoomsList=new ArrayList<Integer>();
        occupiedRoomsTypeList=new ArrayList<String>();
        reservationDetails=new JSONObject();
        if(intent!=null&&intent.hasExtra("reservationDetails")){
            try{
                reservationDetails=new JSONObject(intent.getStringExtra("reservationDetails"));
                tempJArray=reservationDetails.getJSONArray("occupiedRoomsList");
                for(int i=0;i<tempJArray.length();i++){
                    occupiedRoomsList.add(tempJArray.getInt(i));
                }
                tempJArray=reservationDetails.getJSONArray("freeRoomsList");
                for(int i=0;i<tempJArray.length();i++){
                    freeRoomsList.add(tempJArray.getInt(i)); //need to use this list as keys
                }
                tempJArray=reservationDetails.getJSONArray("occupiedRoomsTypeList");
                for(int i=0;i<tempJArray.length();i++){
                    occupiedRoomsTypeList.add(tempJArray.getString(i)); //need to use this list as keys
                }

            }catch (JSONException ex){
                Log.e("myApp", "JSON exception", ex);
            }
        }

//        return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.room_fragment_layout, container, false);
        final String type=getArguments().getString(_roomType);
        final int image=getArguments().getInt(_roomImage);
        final int price=getArguments().getInt(_roomPrice);
        TextView roomDescriptionTv=(TextView)view.findViewById(R.id.roomDescriptionTv);
        roomDescriptionTv.setTypeface(MyApp.FONT_MAIN);
        final TextView roomPriceTv= (TextView) view.findViewById(R.id.roomPriceTv);
        TextView roomAvilabiltyTv = (TextView) view.findViewById(R.id.roomAvilabiltyTv);
        roomAvilabiltyTv.setTypeface(MyApp.FONT_MAIN);
        String result="";
        String mtype=type.toLowerCase().replace(" ", "_");
        Log.i("myApp", "in room fragment,roomType=" + mtype);
        CurrentRoomsMap currentRoomsMap=CurrentRoomsMap.initializeRooms();
        currentRoomsMap.updateAvilableRoomsList(occupiedRoomsList);
//        CurrentRoomsMap currentRoomsMap=CurrentRoomsMap.getCurrentRoomsMap(occupiedRoomsList);
//        int  availableAmount=0;
        if(currentRoomsMap.hasType(mtype)){
            availableAmount=currentRoomsMap.getRoomAmountByType(mtype);

//            List<Integer> roomsNumbersList=currentRoomsMap.getRoomsNumbersByType(mtype);
//            Log.i("myApp", "in room fragment, roomsNumbersList.size="+roomsNumbersList.size());
//            if(roomsNumbersList.size()>0&&roomsNumbersList!=null){
//                Iterator<Integer> iterator = roomsNumbersList.iterator();
//                roomNumber=iterator.next();
//                occupiedRoomsList.add(roomNumber);
//                freeRoomsList.remove(roomNumber);
//                occupiedRoomsTypeList.add(mtype);
//                Log.i("myApp", "in room fragment updated occupiedRoomsList="+occupiedRoomsList.toString());
//                Log.i("myApp", "in room fragment updated freeRoomsList="+freeRoomsList.toString());
//            }

        } else{
            availableAmount=0;
        }
        switch(availableAmount){
//            case 1:
//                result=": "+availableAmount+" "+getResources().getString(R.string.room)+" "+getResources().getString(R.string.left);
//                break;
            case 0:
                result=": not available at your dates";
                LinearLayout ll= (LinearLayout) view.findViewById(R.id.roomPriceAvilabilityContainer);
                ll.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                roomPriceTv.setTextColor(getResources().getColor(android.R.color.white));
                roomAvilabiltyTv.setTextColor(getResources().getColor(android.R.color.white));
                break;
            default:
                result=": "+availableAmount+" "+getResources().getString(R.string.room)+(availableAmount>1?"s ":" ")+getResources().getString(R.string.left);
                break;
        }
        String avilability=context.getResources().getString(R.string.avilabilty)+result;
        roomAvilabiltyTv.setText(avilability);
        ImageView roomImage=(ImageView)view.findViewById(R.id.roomImage);
        TextView bookBtn=(TextView)view.findViewById(R.id.bookBtn);
        bookBtn.setTypeface(MyApp.FONT_SECONDARY);
        final SharedPreferences sharedPreferences=context.getSharedPreferences(_sharedPreferences, Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("myApp", "book "+type);
                RoomListActivity.isPressedBackFromFragment=false;
                boolean isCalledFromRoomListActivity=RoomsActivity.isCalledFromRoomListActivity;
//                Log.i("myApp", "isCalledFromRoomListActivity:"+isCalledFromRoomListActivity);
                if(isCalledFromRoomListActivity){
                    if(availableAmount !=0){
                        try{
                            reservationDetails.put("occupiedRoomsList", new JSONArray(occupiedRoomsList));
                            reservationDetails.put("freeRoomsList", new JSONArray(freeRoomsList));
                        }catch (JSONException ex){
                            Log.e("myApp", "json eror", ex);
                        }
                        Intent intent=new Intent(getActivity(), RoomListActivity.class);
                        intent.putExtra("calling-activity", CallingActivity.ROOMS_ACTIVITY);
                        intent.putExtra("type",type);
                        intent.putExtra("image", image);
                        intent.putExtra("price", price);
                        intent.putExtra("position", RoomsActivity.positionFromRoomListActivity);
                        Log.i("myApp", "in room fragment - callingActivity:"+intent.getIntExtra("calling-activity",-1));
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }else{
                        Toast.makeText(getActivity(), type+" isn't available at your dates", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Intent intent=new Intent(context, ReservationActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
            }
        });
        roomPriceTv.setTypeface(MyApp.FONT_MAIN);
        roomPriceTv.setText(roomPriceTv.getText()+" "+price+" "+getResources().getString(R.string.currency));
        roomDescriptionTv.setText(type);
        roomImage.setImageResource(image);
        return view;
    }

    public void onUpdate() {
        Log.i("myApp", "in room fragment onUpdate..");
    }
}
