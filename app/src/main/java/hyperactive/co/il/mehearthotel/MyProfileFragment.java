package hyperactive.co.il.mehearthotel;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tal on 24/01/2016.
 */
public class MyProfileFragment extends Fragment implements ListView.OnItemClickListener {
    TextView usernameTv;
    ListView optionsList;
    ParseUser currentUser;
    String username, email, password;
    BasicFragmentCommincator commincator;
    JSONArray resHistory;
    List<JSONObject> upcomingResList, pastResList;
    Boolean isResHistoryEmpty;
    SharedPreferences resHistoryFile;
    static final String RES_HISTORY="reservationHistory";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        username=getResources().getString(R.string.username);
        email=getResources().getString(R.string.email);
        password=getResources().getString(R.string.password);
        View view = inflater.inflate(R.layout.myprofile_fragment_layout, container, false);
        usernameTv= (TextView) view.findViewById(R.id.myprofile_usernameTv);
        usernameTv.setTypeface(MyApp.FONT_MAIN);
        optionsList= (ListView) view.findViewById(R.id.myprofile_option_listView);
        currentUser = ParseUser.getCurrentUser();
        if(currentUser!=null){
            usernameTv.setText(getResources().getString(R.string.hello)+" "+currentUser.getUsername()+"!");
        }
        String[] options=getResources().getStringArray(R.array.myprofile_options);
        CustomAdapter adapter=new CustomAdapter(getActivity(), R.layout.myprofile_option_layout, options);
        optionsList.setAdapter(adapter);
        optionsList.setOnItemClickListener(this);
        return view;
    }

    private class CustomAdapter extends ArrayAdapter<CharSequence>{

        Context context;
        int layoutResourceId;
        String data[] = null;

        public CustomAdapter(Context context, int layoutResourceId, String[] data ) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.myprofile_option_layout, parent, false);
            }
            ((TextView)convertView).setTypeface(MyApp.FONT_SECONDARY);
            ((TextView) convertView).setText(data[position]);
            return convertView;
        }
    }

        @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commincator= (BasicFragmentCommincator) getActivity();
//        upcomingReservationBtn.setOnClickListener(this);
//        reservationHistoryBtn.setOnClickListener(this);
//        editPasswordBtn.setOnClickListener(this);
//        editUsernameBtn.setOnClickListener(this);
//        editEmailBtn.setOnClickListener(this);
        new Thread() {
            @Override
            public void run() {
                FileInputStream fileInputStream = null;
                StringBuffer stringBuffer = new StringBuffer();
                try {
                    File folder = new File(Environment.getExternalStorageDirectory() + File.separator + ParseUser.getCurrentUser().getUsername());
                    File resFile = new File(folder, "res.txt");
                    Log.i("myApp", "res file=" + resFile.getAbsolutePath());
                    fileInputStream = new FileInputStream(resFile);
                    int read = -1;
                    while ((read = fileInputStream.read()) != -1) {
                        stringBuffer.append((char) read);
                    }
                }catch (FileNotFoundException e){
                    Log.e("myApp", "file error", e);
                }catch (IOException e){
                    Log.e("myApp", "I/O error", e);
                }finally {
                    try{
                        if(fileInputStream!=null)
                            fileInputStream.close();
                    }catch (IOException e){
                        Log.e("myApp", "I/O error", e);
                    }
                }
                if (stringBuffer.length() != 0){
                    try {
                        resHistory=new JSONArray(stringBuffer.toString());
                        isResHistoryEmpty=false;
                        pastResList=new ArrayList<>();
                        upcomingResList=new ArrayList<>();
                        Log.i("myApp", "on MyProfile, resHistory: "+resHistory.toString());
                        for (int i=0;i<resHistory.length();i++){
                            JSONObject res=resHistory.getJSONObject(i);
                            Log.i("myApp", "on MyProfile,current res: "+res.toString());

                            DateTime checkin=new DateTime(res.get("check_in"));
                            DateTime now=new DateTime();
                            Log.i("myApp", "is check_in before now?" + checkin.isBeforeNow());
                            Log.i("myApp", "is check_in equal now?" + checkin.isEqualNow());
                            if (checkin.withTimeAtStartOfDay().isBefore(now.withTimeAtStartOfDay()))
                                pastResList.add(res);

                            else
                                upcomingResList.add(res);
                        }
                    }catch (JSONException ex){
                     Log.e("myApp", "json error", ex);
                    }
                }
            }
        }.start();
        Log.i("myApp", "check it");
//        resHistoryFile=getActivity().getSharedPreferences(RES_HISTORY, Activity.MODE_PRIVATE);
//        try{
//            isResHistoryEmpty = new String(resHistoryFile.getString("history","empty")).equals("empty");
//            if(!isResHistoryEmpty){
//                resHistory=new JSONArray(resHistoryFile.getString("history","empty"));
//                pastResList=new ArrayList<>();
//                upcomingResList=new ArrayList<>();
//                Log.i("myApp", "on MyProfile, resHistory: "+resHistory.toString());
//                for (int i=0;i<resHistory.length();i++){
//                    JSONObject res=resHistory.getJSONObject(i);
//                    Log.i("myApp", "on MyProfile,current res: "+res.toString());
//                    DateTime checkin=new DateTime(res.get("check_in"));
//                    if (checkin.isBeforeNow())
//                        pastResList.add(res);
//                    else
//                        upcomingResList.add(res);
//                }
//            }
//
//
//        }catch (JSONException ex){
//            Log.e("myApp", "json error", ex);
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                commincator.respone(username);
                break;
            case 1:
                commincator.respone(email);
                break;
            case 2:
                commincator.respone(password);
                break;
            case 3:
                if(!isResHistoryEmpty &&upcomingResList.size()>0){
                    SummaryHistoryFragment summaryHistoryFragment=SummaryHistoryFragment.getInstance(getActivity(), upcomingResList, "Upcoming");
                    summaryHistoryFragment.show(getFragmentManager(),"summaryHistoryFragment");
                }else{
                    MyToast.makeToast(getActivity(), "there are no future reservations to show");
//                    Toast.makeText(getActivity(), "there are no future reservations to show", Toast.LENGTH_LONG).show();
                    Log.i("myApp", "no future reservations");
                }

                break;
            case 4:
                if(!isResHistoryEmpty && pastResList.size()>0){
                    SummaryHistoryFragment summaryHistoryFragment=SummaryHistoryFragment.getInstance(getActivity(), pastResList, "History");
                    summaryHistoryFragment.show(getFragmentManager(),"summaryHistoryFragment");
                }else{
                    MyToast.makeToast(getActivity(), "there are no past reservations to show");
//                    Toast.makeText(getActivity(), "there are no past reservations to show", Toast.LENGTH_LONG).show();
                    Log.i("myApp", "no past reservations");
                }
                break;
        }
    }

//    @Override
//    public void onClick(View v) {
//        final View vv = v;
//        switch (vv.getId()) {
//            case R.id.myprofile_edit_emailBtn:
//                commincator.respone(email);
//                break;
//            case R.id.myprofile_edit_usernameBtn:
//                commincator.respone(username);
//                break;
//            case R.id.myprofile_edit_password:
//                commincator.respone(password);
//                break;
//            case R.id.myprofile_reservation_upcomingBtn:
//                if(!isResHistoryEmpty &&upcomingResList.size()>0){
//                    SummaryHistoryFragment summaryHistoryFragment=SummaryHistoryFragment.getInstance(getActivity(), upcomingResList, "Upcoming");
////                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
////                    lp.copyFrom(summaryHistoryFragment.getDialog().getWindow().getAttributes());
////                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
////                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
////                    summaryHistoryFragment.getDialog().getWindow().setAttributes(lp);
//                    summaryHistoryFragment.show(getFragmentManager(),"summaryHistoryFragment");
//                }else{
//                    Toast.makeText(getActivity(), "there are no future reservations to show", Toast.LENGTH_LONG).show();
//                    Log.i("myApp", "no future reservations");
//                }
//
//                break;
//            case R.id.myprofile_reservation_historyBtn:
//                if(!isResHistoryEmpty && pastResList.size()>0){
//                    SummaryHistoryFragment summaryHistoryFragment=SummaryHistoryFragment.getInstance(getActivity(), pastResList, "History");
//                    summaryHistoryFragment.show(getFragmentManager(),"summaryHistoryFragment");
//                }else{
//                    Toast.makeText(getActivity(), "there are no past reservations to show", Toast.LENGTH_LONG).show();
//                    Log.i("myApp", "no past reservations");
//                }
//                break;
//        }
//
//    }

    public void update(){
        currentUser = ParseUser.getCurrentUser();
        if(currentUser!=null){
            usernameTv.setText(currentUser.getUsername());
//            emailValueTv.setText(currentUser.getEmail());
        }
    }

}
