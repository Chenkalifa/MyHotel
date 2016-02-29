package hyperactive.co.il.mehearthotel;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tal on 27/12/2015.
 */
public class RoomsFragmentAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragments;
    Context context;



    public RoomsFragmentAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
       
        return super.getItemPosition(object);
    }


    //    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        LinearLayout linearLayout=(LinearLayout)container.inflate(context, R.layout.room_fragment_layout, null);
//        TextView descrption=(TextView)linearLayout.findViewById(R.id.roomDescriptionTv);
//        ImageView roomImage=(ImageView)linearLayout.findViewById(R.id.roomImage);
//        TextView bookBtn=(TextView)linearLayout.findViewById(R.id.bookBtn);
//        ((ViewPager)container).addView(linearLayout);
//        return linearLayout;
//    }
}
