package hyperactive.co.il.mehearthotel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCommincator, View.OnClickListener {
    //    EditText username, password;

    Button loginBtn,signupBtn;
    LogInFragment logInFragment;
    LogoFragment logoFragment;
    SignUpFragment signUpFragment;
    FragmentManager manager;
    ContactFragment contactFragment;
//    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        loginBtn= (Button) findViewById(R.id.loginBtn);
        loginBtn.setTypeface(MyApp.FONT_SECONDARY);
        signupBtn= (Button) findViewById(R.id.signupBtn);
        signupBtn.setTypeface(MyApp.FONT_SECONDARY);
        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
//        background= (ImageView) findViewById(R.id.screen_background_Iv);
//        Glide.with(this)
//                .load(R.drawable.backround)
//                .centerCrop()
//                .into(background);
        logInFragment = new LogInFragment();
        signUpFragment=new SignUpFragment();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.i("myApp", "back was pressed, BackStackEntryCount: " + getFragmentManager().getBackStackEntryCount());
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_screen, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(HomeScreenActivity.this, HomeScreenActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(HomeScreenActivity.this, TouristInfoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_rooms) {
            Intent intent = new Intent(HomeScreenActivity.this, RoomsActivity.class);
            intent.putExtra("calling-activity", CallingActivity.OTHER_ACTIVITY);
            startActivity(intent);

        } else if (id == R.id.nav_myprofile) {
            Intent intent = new Intent(HomeScreenActivity.this, MyProfileActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_reserv) {
            Intent intent = new Intent(HomeScreenActivity.this, ReservationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            contactFragment = new ContactFragment();
            contactFragment.show(manager, "contactFragment");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("myApp", "homeActivity onPause");
    }

    @Override
    public void respone(Bundle bundle) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Log.i("myApp", "BackStackEntryCount:" + manager.getBackStackEntryCount());
        Log.i("myApp", "coming from " + bundle.getString("from"));
        switch (bundle.getString("from")) {
            case "Home":


                if (signUpFragment == null)
                    signUpFragment = new SignUpFragment();
                signUpFragment.setNewArguments(bundle);
                fragmentTransaction.remove(logoFragment);
                fragmentTransaction.remove(logInFragment);
                fragmentTransaction.add(R.id.home_screen_container, signUpFragment, "signUpFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case "Sign":
                if (logInFragment == null)
                    logInFragment = (LogInFragment) manager.findFragmentByTag("logInFragment");
                if (logoFragment == null)
                    logoFragment = (LogoFragment) manager.findFragmentByTag("logoFragment");
                logInFragment.setNewArguments(bundle);
                fragmentTransaction.add(R.id.home_screen_logo_fragment_container, logoFragment, "logoFragment");
                fragmentTransaction.remove(signUpFragment);
                fragmentTransaction.add(R.id.home_screen_login_fragment_container, logInFragment, "logInFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (!logoFragment.isAdded() && !logInFragment.isAdded()) {
//            FragmentTransaction fragmentTransaction = manager.beginTransaction();
//            fragmentTransaction.add(R.id.home_screen_logo_fragment_container, logoFragment, "logoFragment");
//            fragmentTransaction.add(R.id.home_screen_login_fragment_container, logInFragment, "logInFragment");
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }
    }

    @Override
    public void onClick(View v) {
        final View vv=v;
        switch (vv.getId()){
            case R.id.loginBtn:
                logInFragment.show(manager, "login");
                break;
            case R.id.signupBtn:
                signUpFragment.show(manager,"signup");
                break;
        }
    }
}
