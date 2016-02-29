package hyperactive.co.il.mehearthotel;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tal on 10/01/2016.
 */
public class RoomListAdapter extends ArrayAdapter<RowItem> {
    Context context;
    List<RowItem> items;

    public RoomListAdapter(Context context, List<RowItem> items) {
        super(context, R.layout.room_list_item, items);
        this.context=context;
        this.items=items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(R.layout.room_list_item, parent, false);
        }
        TextView tv=(TextView)convertView.findViewById(R.id.room_list_itemTv);
        tv.setTypeface(MyApp.FONT_MAIN);
        tv.setGravity(Gravity.CENTER);
        TextView priceTv= (TextView) convertView.findViewById(R.id.room_list_priceTv);
        priceTv.setTypeface(MyApp.FONT_MAIN);
        int price=items.get(position).getPrice();
        String input="";
        if(price!=0){
            String priceString=context.getResources().getString(R.string.price);
            input=priceString+" "+price+" "+context.getResources().getString(R.string.currency);
        }
        priceTv.setText(input);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.room_list_image);
        int image=items.get(position).getImage();
        if(image!=-1)
            imageView.setImageResource(image);
        tv.setText(items.get(position).getDescription());
        return convertView;
    }
}
