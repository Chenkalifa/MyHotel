package hyperactive.co.il.mehearthotel;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Tal on 17/02/2016.
 */
public class TouristInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tourist_info_fragment_layout, container, false);
        ListView list= (ListView) view.findViewById(R.id.tourist_info_listView);
        final String[] urls=getResources().getStringArray(R.array.tourist_info_urls);
        String[] titles=getResources().getStringArray(R.array.tourist_info_titles);
        TypedArray images=getResources().obtainTypedArray(R.array.tourist_info_images);
        TouristInfoAdapter adapter=new TouristInfoAdapter(getActivity(), R.layout.tourist_info_list_item_layout, titles, images);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position]));
                getActivity().startActivity(browserIntent);
            }
        });
        return view;
    }
}
