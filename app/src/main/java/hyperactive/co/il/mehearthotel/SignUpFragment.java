package hyperactive.co.il.mehearthotel;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tal on 18/01/2016.
 */
public class SignUpFragment extends DialogFragment implements View.OnClickListener{
    ImageView ic_close;
    EditText usernameEt, emailEt, verifyEmailEt, passwordEt;
    Button continueBtn;
    FragmentCommincator commincator;
    Bundle args;
    String username, email,verify, password;
    ParseUser user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.signup_fragment_layout_new, container, false);
        ic_close= (ImageView) view.findViewById(R.id.ic_close);
        usernameEt= (EditText) view.findViewById(R.id.signUp_usernameEt);
        usernameEt.setTypeface(MyApp.FONT_MAIN);
        emailEt= (EditText) view.findViewById(R.id.signUp_emailEt);
        emailEt.setTypeface(MyApp.FONT_MAIN);
        verifyEmailEt= (EditText) view.findViewById(R.id.signUp_emailVerifyEt);
        verifyEmailEt.setTypeface(MyApp.FONT_MAIN);
        passwordEt= (EditText) view.findViewById(R.id.signUp_passwordEt);
        passwordEt.setTypeface(MyApp.FONT_MAIN);
        continueBtn= (Button) view.findViewById(R.id.signUp_cuntinueBtn);
        continueBtn.setTypeface(MyApp.FONT_SECONDARY);
        username="username";
        email="email";
        verify="verify";
        password="password";
        Log.i("myApp", "SignUpFragment - onCreateView "+(args!=null?args.toString():"args is null"));
        if(args!=null){
            upDateFileds(args);
        }
        ic_close.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commincator= (FragmentCommincator) getActivity();
    }

    @Override
    public void onClick(View v) {
        final View vv=v;
        switch (vv.getId()){
            case R.id.ic_close:
                dismiss();
//                backToCallingFragment();
                break;
            case R.id.signUp_cuntinueBtn:
                signUpUser();
                break;
        }
    }

    private void signUpUser() {
        Log.i("myApp", "signupFragment - signUser");
        if(checkAllFields()){
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        MyToast.makeToast(getActivity(), "saved");
//                        Toast.makeText(getActivity(), "saved", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getActivity(), MyProfileActivity.class);
                        intent.putExtra("username", user.getUsername());
                        intent.putExtra("password", passwordEt.getText().toString().trim().toLowerCase());
                        startActivity(intent);

                    } else {
                        MyToast.makeToast(getActivity(), e.getMessage());
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("myApp", "user register error", e);
                    }
                }
            });
        }
    }

    private boolean checkAllFields() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        user=new ParseUser();
        String mUser=usernameEt.getText().toString().trim().toLowerCase();
        String mEmail=emailEt.getText().toString().trim().toLowerCase();
        String mVerifyEmail=verifyEmailEt.getText().toString().trim().toLowerCase();
        String mPassword=passwordEt.getText().toString().trim().toLowerCase();
        user.setUsername(mUser);
        user.setEmail(mEmail);
        user.setPassword(mPassword);
        Map<String, String> input=new HashMap<String, String>();
        input.put(username, mUser);
        input.put(email, mEmail);
        input.put(verify,mVerifyEmail);
        input.put(password, mPassword);
        Log.i("myApp", input.toString());
        for (Map.Entry<String, String> entry : input.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(value.length()==0){
                MyToast.makeToast(getActivity(), key + " field is empty");
//                Toast.makeText(getActivity(), key+" field is empty", Toast.LENGTH_LONG).show();
                return false;
            }
            if(value.contains(" ")){
                MyToast.makeToast(getActivity(), key + " can't contains spaces");
//                Toast.makeText(getActivity(), key+" can't contains spaces", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(mPassword.contains(" ")){
            MyToast.makeToast(getActivity(), "password can't contains spaces");
//            Toast.makeText(getActivity(), "password can't contains spaces", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isValidEmail(input.get(email))){
            MyToast.makeToast(getActivity(), input.get(email) + " is not a valid email address");
//            Toast.makeText(getActivity(), input.get(email)+" is not a valid email address", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!input.get(email).equals(input.get(verify))) {
            MyToast.makeToast(getActivity(), "verify field must equals email field");
//            Toast.makeText(getActivity(), "verify field must equals email field", Toast.LENGTH_LONG).show();
            return false;
        }
        if(input.get(password).length()<6){
            MyToast.makeToast(getActivity(), "password must be at least 6 chars");
//            Toast.makeText(getActivity(), "password must be at least 6 chars", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void backToCallingFragment() {
        Bundle bundle=new Bundle();
        String userString=usernameEt.getText().toString();
        String passwordString=passwordEt.getText().toString();
        bundle.putString("from", "Sign");
        bundle.putString("username", userString);
        bundle.putString("password", passwordString);
        Log.i("myApp", "signupFragment - back to callingFragment " + bundle.toString());
        commincator.respone(bundle);
    }

    public void setNewArguments(Bundle bundle){
        args=bundle;
    }

    public void upDateFileds(Bundle bundle){
        Log.i("myApp", "signupFragment upDateFileds: "+(bundle!=null?bundle.toString():"bundle is null"));
        if(bundle!=null){
            usernameEt.setText(bundle.getString("username"));
            passwordEt.setText(bundle.getString("password"));
        }
    }
}
