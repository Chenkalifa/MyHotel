package hyperactive.co.il.mehearthotel;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Tal on 19/01/2016.
 */
public class LogInFragment extends DialogFragment implements View.OnClickListener {
    EditText usernameEt, passwordEt;
    ImageView ic_close;
    Button loginBtn;
    FragmentCommincator commincator;
    CheckBox loggedCheckBox;
    Bundle args;
    ParseUser user;
    SharedPreferences preferences;
    final String MY_APP_PREFERNCES="preferences";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_layout_new, container, false);
        ic_close= (ImageView) view.findViewById(R.id.ic_close);
        usernameEt = (EditText) view.findViewById(R.id.usernameEt);
        passwordEt = (EditText) view.findViewById(R.id.passwordEt);
        loginBtn = (Button) view.findViewById(R.id.loginBtn);
        loginBtn.setTypeface(MyApp.FONT_SECONDARY);
        loggedCheckBox = (CheckBox) view.findViewById(R.id.keeplogged_checkbox);
        loggedCheckBox.setTypeface(MyApp.FONT_MAIN);
        usernameEt.setText("");
        usernameEt.setTypeface(MyApp.FONT_MAIN);
        passwordEt.setTypeface(MyApp.FONT_MAIN);
        passwordEt.setText("");
        loginBtn.setOnClickListener(this);
        ic_close.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.i("myApp", "logInFragment - onCreateView " + (args != null ? args.toString() : "args is null"));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(savedInstanceState!=null){
//            usernameEt.setText(savedInstanceState.getString("username"));
//            passwordEt.setText(savedInstanceState.getString("password"));
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("myApp", "logInFragment - onActivityCreated");
        preferences=getActivity().getSharedPreferences(MY_APP_PREFERNCES, Activity.MODE_PRIVATE);
        loggedCheckBox.setChecked(preferences.getBoolean("keepLogged", false));
//        if(loggedCheckBox.isChecked()&&savedInstanceState!=null){
//            usernameEt.setText(savedInstanceState.getString("username", ""));
//            passwordEt.setText(savedInstanceState.getString("password", ""));
//        }
        commincator = (FragmentCommincator) getActivity();
        if (args != null) {
            upDateFileds(args);
        }
    }

    @Override
    public void onClick(View v) {
        final View vv = v;
        switch (vv.getId()) {
            case R.id.loginBtn:
                userLogin();
                break;
            case R.id.ic_close:
                dismiss();
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Log.i("myApp", "logInFragment - onSaveInstanceState");
//        if(saveLoggedCheckBoxState()){
//            outState.putString("username", usernameEt.getText().toString());
//            outState.putString("password", passwordEt.getText().toString());
//        }
        super.onSaveInstanceState(outState);
    }

    private void userLogin() {
//        user=new ParseUser();
        String mUser=usernameEt.getText().toString().toLowerCase().trim();
        String mPssword=passwordEt.getText().toString().toLowerCase().trim();
        Log.i("myApp", "inside userLogIn, username:" + mUser + " password: " + mPssword);
        ParseUser.logInInBackground(mUser, mPssword, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.i("myApp", "login succeeded:" + user.getUsername());
//                    saveLoggedCheckBoxState();
                    Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                    intent.putExtra("username", user.getUsername());
                    intent.putExtra("password", passwordEt.getText().toString().trim().toLowerCase());
                    startActivity(intent);
                    // Hooray! The user is logged in.
                } else {
                    Log.i("myApp", "login failed cause:" + e.getMessage());
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("myApp", "login fragment onPause");
        saveLoggedCheckBoxState();
    }

    private boolean saveLoggedCheckBoxState() {
        preferences.edit().putBoolean("keepLogged", loggedCheckBox.isChecked()).commit();
        Log.i("myApp", "login loggedCheckBox state: " + loggedCheckBox.isChecked());
        return loggedCheckBox.isChecked();
    }

    public void setNewArguments(Bundle bundle) {
        args = bundle;
    }

    private void showSignUpFragment() {
        Bundle bundle = new Bundle();
        String userString = usernameEt.getText().toString();
        String passwordString = passwordEt.getText().toString();
        bundle.putString("from", "Home");
        bundle.putString("username", userString);
        bundle.putString("password", passwordString);
        Log.i("myApp", "LogInFragment - showSignUpFragment " + bundle.toString());
        commincator.respone(bundle);
    }

    public void upDateFileds(Bundle bundle){
        Log.i("myApp", "LogInFragment upDateFileds: " + (bundle != null ? bundle.toString() : "bundle is null"));
        if(bundle!=null){
            usernameEt.setText(bundle.getString("username"));
            passwordEt.setText(bundle.getString("password"));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        preferences.edit().putBoolean("keepLogged", loggedCheckBox.isChecked()).apply();
//        Log.i("myApp", "login fragment onDetach: "+loggedCheckBox.isChecked());
    }
}
