package hyperactive.co.il.mehearthotel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Tal on 21/01/2016.
 */
public class NotLoggedFragment extends Fragment implements View.OnClickListener {
    TextView loginBtn, signupBtn, memberTv, notMemberTv;
    FragmentCommincator commincator;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.not_logged_fragment_layout, container, false);
        loginBtn= (TextView) view.findViewById(R.id.myprofile_loginBtn);
        loginBtn.setTypeface(MyApp.FONT_SECONDARY);
        signupBtn= (TextView) view.findViewById(R.id.myprofile_signupBtn);
        signupBtn.setTypeface(MyApp.FONT_SECONDARY);
        memberTv= (TextView) view.findViewById(R.id.myprofile_not_logged_memeberTv);
        memberTv.setTypeface(MyApp.FONT_MAIN);
        notMemberTv= (TextView) view.findViewById(R.id.myprofile_not_logged_not_memeberTv);
        notMemberTv.setTypeface(MyApp.FONT_MAIN);
        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final View vv=v;
        Bundle bundle=new Bundle();
        bundle.putInt("id", vv.getId());
        commincator.respone(bundle);
//        switch (vv.getId()){
//            case R.id.myprofile_loginBtn:
//                break;
//            case R.id.myprofile_signupBtn:
//                break;
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("myApp", "NotLoggedFragment - onActivityCreated");
        commincator = (FragmentCommincator) getActivity();
    }
}
