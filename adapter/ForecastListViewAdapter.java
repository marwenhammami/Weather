package com.marouenhammami.weather.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marouenhammami.weather.activity.R;
import com.marouenhammami.weather.item.EntryItem;

import java.util.List;

/**
 * Created by maouenhammami on 13/06/15.
 */
public class ForecastListViewAdapter extends ArrayAdapter<EntryItem> {
    private Context context;
    private Typeface weatherFont;


    public ForecastListViewAdapter(Context context, int resourceId, List<EntryItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        EntryItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_entry, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.list_item_entry_summary);
            holder.txtDesc.setTypeface(weatherFont);

            holder.txtTitle = (TextView) convertView.findViewById(R.id.list_item_entry_title);
            holder.txtTitle.setTypeface(weatherFont);
            holder.txtTitle.setTypeface(holder.txtTitle.getTypeface(),Typeface.BOLD);

            holder.imageView = (ImageView) convertView.findViewById(R.id.list_item_entry_icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getTemp()+getContext().getString(R.string.celcius));
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageBitmap(rowItem.getImageId());
        return convertView;
    }
}
