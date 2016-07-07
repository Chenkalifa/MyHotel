package hyperactive.co.il.mehearthotel;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MyProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCommincator, BasicFragmentCommincator, InputDialogFragment.InputUpDater {
    NotLoggedFragment notLoggedFragment;
    ContactFragment contactFragment;
    BasicLogInFragment basicLogInFragment;
    LogInFragment logInFragment;
    SignUpFragment signUpFragment;
    MyProfileFragment myProfileFragment;
    FragmentManager manager;
    String tempInputType;
    final String MY_APP_PREFERNCES = "preferences";
    SharedPreferences preferences;
    ParseUser currentUser;

    //    final String username=, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences(MY_APP_PREFERNCES, Activity.MODE_PRIVATE);
        manager = getFragmentManager();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactFragment = new ContactFragment();
                contactFragment.show(manager, "contactFragment");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        signUpFragment=new SignUpFragment();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isUserLogged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.my_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(MyProfileActivity.this, HomeScreenActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(MyProfileActivity.this, TouristInfoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_rooms) {
            Intent intent = new Intent(MyProfileActivity.this, RoomsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_myprofile) {
//            Intent intent=new Intent(HomeScreenActivity.this, MyProfileActivity.class);
//            startActivity(intent);


        } else if (id == R.id.nav_reserv) {
            Intent intent = new Intent(MyProfileActivity.this, ReservationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            contactFragment = new ContactFragment();
            contactFragment.show(manager, "contactFragment");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isUserLogged() {
        Log.i("myApp", "mProfile: " + preferences.getBoolean("keepLogged", false));
        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            if (myProfileFragment == null)
                myProfileFragment = new MyProfileFragment();
            if(!myProfileFragment.isAdded()){
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.add(R.id.myprofile_conatiner, myProfileFragment, "myProfileFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            return true;
        } else {
            currentUser.logOut();
            Log.i("myApp", "mProfile: currently no user is logged");
            notLoggedFragment = new NotLoggedFragment();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(R.id.myprofile_conatiner, notLoggedFragment, "notLoggedFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return false;
        }
    }

    @Override
    public void respone(Bundle bundle) {
        int id = bundle.getInt("id");
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Log.i("myApp", "id:" + id);
        switch (id) {
            case R.id.myprofile_loginBtn:
                if (logInFragment == null)
                    logInFragment=new LogInFragment();
//                    basicLogInFragment = new BasicLogInFragment();
                logInFragment.show(manager, "LogInFragment");
//                basicLogInFragment.show(manager, "basicLogInFragment");
                break;
            case R.id.myprofile_signupBtn:
                if (signUpFragment == null)
                    signUpFragment = new SignUpFragment();
                signUpFragment.show(manager, "signUpFragment");
//                fragmentTransaction.replace(R.id.myprofile_conatiner, signUpFragment, "signUpFragment");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                break;
        }

    }

    @Override
    public void respone(String text) {
        tempInputType = text;
        InputDialogFragment inputFragment = InputDialogFragment.newInstance(text);
        inputFragment.setUpdater(this);
        inputFragment.show(manager, "inputFragment");
    }

    @Override
    public void updateChanges(final String value) {
        final String username = "Username";
        final String email = "Email";
        final String password = "Password";
        switch (tempInputType) {
            case username:
                currentUser.setUsername(value);
                break;
            case email:
                currentUser.setEmail(value);
                break;
            case password:
                currentUser.setPassword(value);
                break;
        }
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    MyToast.makeToast(MyProfileActivity.this, "changes succeeded!" + "\nnew " + tempInputType + ":" + value);
//                    Toast.makeText(MyProfileActivity.this, "changes succeeded!" + "\nnew " + tempInputType + ":" + value, Toast.LENGTH_LONG).show();
                else
                MyToast.makeToast(MyProfileActivity.this, "error:" + e.getMessage());
//                    Toast.makeText(MyProfileActivity.this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        if (myProfileFragment == null)
            myProfileFragment = (MyProfileFragment) manager.findFragmentByTag("myProfileFragment");
        myProfileFragment.update();
    }

}
