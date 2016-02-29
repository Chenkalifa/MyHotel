package hyperactive.co.il.mehearthotel;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Tal on 14/02/2016.
 */
public class SummaryHistoryFragment extends DialogFragment {
    List<JSONObject> reservations;
    Context context;
    String title;

    public static SummaryHistoryFragment getInstance(Context context, List<JSONObject> reservations, String title){
        SummaryHistoryFragment summaryHistoryFragment=new SummaryHistoryFragment();
        summaryHistoryFragment.reservations=reservations;
        summaryHistoryFragment.context=context;
        summaryHistoryFragment.title=title;
        return summaryHistoryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.summary_res_history_fragment_layout, container, false);
        ListView resHistoryList= (ListView) view.findViewById(R.id.summary_Lv);
        TextView titleTv= (TextView) view.findViewById(R.id.summary_titleTv);
        titleTv.setText(title);
        SummaryAdapter summaryAdapter=new SummaryAdapter(context, R.layout.summary_single_row_layout, reservations);
        resHistoryList.setAdapter(summaryAdapter);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }
}
