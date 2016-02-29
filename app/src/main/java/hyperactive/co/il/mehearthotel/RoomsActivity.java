package hyperactive.co.il.mehearthotel;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoomsActivity extends FragmentActivity//AppCompatActivity //
//        implements NavigationView.OnNavigationItemSelectedListener {
{
    RoomsMap roomsMap;
    ViewPager roomVp;
    Resources res;
    List<String> occupiedRoomsTypeList;
    JSONObject reservationDetails;
    static boolean isCalledFromRoomListActivity = false;
    static int positionFromRoomListActivity = -1;
    RoomsFragmentAdapter rfc;

    private List<Fragment> getFragments() {
//            getAvailability();
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(RoomFragment.newInstance(this, res.getString(R.string.room_1_description), 300, R.drawable.room_1));
        list.add(RoomFragment.newInstance(this, res.getString(R.string.room_2_description), 275, R.drawable.room_2a));
        list.add(RoomFragment.newInstance(this, res.getString(R.string.room_3_description), 240, R.drawable.room_3a));
        list.add(RoomFragment.newInstance(this, res.getString(R.string.room_4_description), 325, R.drawable.room_4a));
        list.add(RoomFragment.newInstance(this, res.getString(R.string.room_5_description), 310, R.drawable.room_5a));
        list.add(RoomFragment.newInstance(this, res.getString(R.string.room_6_description), 350, R.drawable.room_6a));
        list.add(RoomFragment.newInstance(this, res.getString(R.string.room_7_description), 420, R.drawable.room_7a));
        return list;
    }

    private void getOccupiedRoomsType() {
        try {
            JSONArray occupiedRoomsTypeList = reservationDetails.getJSONArray("occupiedRoomsTypeList");
//            Log.i("myApp", "occupiedRoomsList="+occupiedRoomsList.toString());
//            for(int i=0;i<occupiedRoomsList.length();i++){
//                Log.i("myApp", "iterting over occupiedRoomsList," + occupiedRoomsList.getInt(i));
//                occupiedRoomsTypeList.add(roomsMap.getRoomType(occupiedRoomsList.getInt(i)));
//            }
            Log.i("myApp", "occupiedRoomsTypeList=" + occupiedRoomsTypeList);
        } catch (JSONException e) {
            Log.e("myApp", "JSON exception", e);
        }

    }

    private ViewPager.OnPageChangeListener mPageLitsener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            switch (position)
            {
                case 0:
                    ((RoomFragment)rfc.getItem(0)).onUpdate();
                    break;
                case 1:
                    ((RoomFragment)rfc.getItem(1)).onUpdate();
                    break;
                case 2:
                    ((RoomFragment)rfc.getItem(2)).onUpdate();
                    break;
                default:
                    ((RoomFragment)rfc.getItem(0)).onUpdate();
                    break;
            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}
        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

//        getActionBar().setTitle(getResources().getString(R.string.rooms));
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent intent = new Intent(RoomsActivity.this, HomeScreenActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_info) {
                    Intent intent = new Intent(RoomsActivity.this, TouristInfoActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_rooms) {
//            Intent intent=new Intent(HomeScreenActivity.this, RoomsActivity.class);
//            startActivity(intent);

                } else if (id == R.id.nav_myprofile) {
                    Intent intent = new Intent(RoomsActivity.this, MyProfileActivity.class);
                    startActivity(intent);


                } else if (id == R.id.nav_reserv) {
                    Intent intent = new Intent(RoomsActivity.this, ReservationActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_contact) {
                    ContactFragment contactFragment = new ContactFragment();
                    contactFragment.show(getFragmentManager(), "contactFragment");



                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        roomsMap = new RoomsMap();
        roomVp = (ViewPager) findViewById(R.id.roomVp);
        res = getResources();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Intent intent = getIntent();
            int callingActivity = intent.getIntExtra("calling-activity", 0);
            switch (callingActivity) {
                case CallingActivity.OTHER_ACTIVITY:
                    isCalledFromRoomListActivity = false;
                    break;
                case CallingActivity.ROOMS_LIST_ACTIVITY:
                    isCalledFromRoomListActivity = true;
                    positionFromRoomListActivity = intent.getIntExtra("position", -1);
                    reservationDetails = new JSONObject(intent.getStringExtra("reservationDetails"));
                    String test = res.getString(R.string.room_1_description).toLowerCase().replace(" ", "_");
                    String tempJSONArray = reservationDetails.getJSONArray("occupiedRoomsTypeList").toString();
                    if (tempJSONArray.contains(test))
                        Log.i("myApp", "from " + test + " left:" + (roomsMap.getRoomAmountByType(test) - 1));
//                    }
                    break;

            }
            rfc = new RoomsFragmentAdapter(getSupportFragmentManager(), getFragments());
            roomVp.addOnPageChangeListener(mPageLitsener);
            roomVp.setAdapter(rfc);
//            if (intent != null) {
//                isCalledFromRoomListActivity=true;
//                positionFromRoomListActivity=intent.getIntExtra("position",-1);
//            }
        } catch (NullPointerException ex) {
            Log.i("myApp", "no room was chosen yet");
        } catch (JSONException e) {
            Log.e("myApp", "JSON exception", e);
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            RoomListActivity.isPressedBackFromFragment=true;
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.rooms, menu);
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

//    @SuppressWarnings("StatementWithEmptyBody")
//    //@Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            Intent intent=new Intent(RoomsActivity.this, HomeScreenActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_info) {
//            Intent intent=new Intent(RoomsActivity.this, TouristInfoActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_offers) {
//            Intent intent = new Intent(RoomsActivity.this, DealsActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_rooms) {
////            Intent intent=new Intent(HomeScreenActivity.this, RoomsActivity.class);
////            startActivity(intent);
//
//        } else if (id == R.id.nav_myprofile) {
//            Intent intent=new Intent(RoomsActivity.this, MyProfileActivity.class);
//            startActivity(intent);
//
//
//        } else if (id == R.id.nav_reserv) {
//            Intent intent = new Intent(RoomsActivity.this, ReservationActivity.class);
//            startActivity(intent);
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
