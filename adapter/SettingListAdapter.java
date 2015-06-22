package com.marouenhammami.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marouenhammami.weather.activity.R;
import com.marouenhammami.weather.item.SettingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maouenhammami on 14/06/15.
 */
public class SettingListAdapter extends ArrayAdapter<SettingItem> {
    Context context;
    ArrayList<SettingItem> settingItems;

    public SettingListAdapter(Context context, int resourceId, List<SettingItem> settingItems) {
        super(context, resourceId, settingItems);
        this.context = context;

    }

    /*private view holder class*/
    private class ViewHolder {
        TextView option;
        TextView details;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder = null;
        SettingItem rowItem = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.setting_item, null);
            holder = new ViewHolder();
            holder.option = (TextView) convertView.findViewById(R.id.setting_option);
            holder.details = (TextView) convertView.findViewById(R.id.setting_detail);
            view = inflater.inflate(R.layout.setting_item, null);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.option.setText(rowItem.getOption());
        holder.details.setText(rowItem.getDetail());

        return convertView;
    }
}
