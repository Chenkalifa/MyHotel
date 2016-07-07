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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Tal on 21/01/2016.
 */
public class BasicLogInFragment extends DialogFragment implements View.OnClickListener {
    EditText usernameEt, passwordEt;
    TextView loginBtn, cancelBtn;
    FragmentCommincator commincator;
    CheckBox loggedCheckBox;
    Bundle args;
    SharedPreferences preferences;
    final String MY_APP_PREFERNCES="preferences";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.basic_login_fragment_layout, container, false);
        usernameEt = (EditText) view.findViewById(R.id.usernameEt);
        passwordEt = (EditText) view.findViewById(R.id.passwordEt);
        loginBtn = (TextView) view.findViewById(R.id.loginBtn);
        cancelBtn= (TextView) view.findViewById(R.id.cancelBtn);
        loggedCheckBox = (CheckBox) view.findViewById(R.id.keeplogged_checkbox);
        usernameEt.setText("");
        passwordEt.setText("");
        loginBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.i("myApp", "logInFragment - onCreateView " + (args != null ? args.toString() : "args is null"));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("myApp", "basiclogInFragment - onActivityCreated");
        preferences=getActivity().getSharedPreferences(MY_APP_PREFERNCES, Activity.MODE_PRIVATE);
        loggedCheckBox.setChecked(preferences.getBoolean("keepLogged", false));
        commincator = (FragmentCommincator) getActivity();
    }

    @Override
    public void onClick(View v) {
        final View vv = v;
        switch (vv.getId()){
            case R.id.loginBtn:
                userLogin();
                break;
            case R.id.cancelBtn:
                dismiss();
                break;
        }
    }

    private void userLogin() {
        String mUser=usernameEt.getText().toString().toLowerCase().trim();
        String mPssword=passwordEt.getText().toString().toLowerCase().trim();
        Log.i("myApp", "inside userLogIn, username:" + mUser + " password: " + mPssword);
        ParseUser.logInInBackground(mUser, mPssword, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.i("myApp", "login succeeded:" + user.getUsername());
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

}
