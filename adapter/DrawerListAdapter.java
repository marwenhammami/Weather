package com.marouenhammami.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marouenhammami.weather.activity.R;
import com.marouenhammami.weather.item.NavDrawerItem;

import java.util.ArrayList;

/**
 * Created by marouen hammami on 09/06/15.
 */
public class DrawerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavDrawerItem> mNavItems;

    public DrawerListAdapter(Context context, ArrayList<NavDrawerItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_list_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.label_item);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon_item);

        titleView.setText( mNavItems.get(position).getmTitle() );
        iconView.setImageResource(mNavItems.get(position).getmIcon());

        return view;
    }
}