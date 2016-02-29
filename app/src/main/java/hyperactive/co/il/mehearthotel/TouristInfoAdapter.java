package hyperactive.co.il.mehearthotel;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tal on 17/02/2016.
 */
public class TouristInfoAdapter extends ArrayAdapter<String>{
    String[] titles;
    TypedArray images;
    Context context;

    public TouristInfoAdapter(Context context, int resource, String[] titles, TypedArray images) {
        super(context, resource, titles);
        this.context=context;
        this.titles=titles;
        this.images=images;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.tourist_info_list_item_layout, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.image.setImageDrawable(images.getDrawable(position));
        holder.title.setTypeface(MyApp.FONT_SECONDARY);
        holder.title.setText(titles[position]);
        return convertView;
    }



    private class ViewHolder{
        ImageView image;
        TextView title;
        public ViewHolder(View view){
            image= (ImageView) view.findViewById(R.id.tourist_info_item_img);
            title= (TextView) view.findViewById(R.id.tourist_info_item_titleTv);
        }
    }
}
