package hyperactive.co.il.mehearthotel;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class TouristInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TouristInfoFragment infoFragment;
    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        manager=getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        infoFragment=new TouristInfoFragment();
        fragmentTransaction.add(R.id.tourist_info_activity_container, infoFragment, "infoFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(manager.getBackStackEntryCount() > 1 ) {
                manager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.info, menu);
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
            Intent intent=new Intent(TouristInfoActivity.this, HomeScreenActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_info) {
//            Intent intent=new Intent(HomeScreenActivity.this, TouristInfoActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_rooms) {
            Intent intent=new Intent(TouristInfoActivity.this, RoomsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_myprofile) {
            Intent intent=new Intent(TouristInfoActivity.this, MyProfileActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_reserv) {
            Intent intent = new Intent(TouristInfoActivity.this, ReservationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            ContactFragment contactFragment = new ContactFragment();
            contactFragment.show(manager, "contactFragment");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
