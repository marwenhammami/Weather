package com.marouenhammami.weather.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.marouenhammami.weather.activity.R;
import com.marouenhammami.weather.adapter.SettingListAdapter;
import com.marouenhammami.weather.item.SettingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maouenhammami on 14/06/15.
 */
public class SettingFragment extends Fragment {
    private String[] options;
    private String[] details;
    private List<SettingItem> listSettings;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        options = new String[]{getString(R.string.setting_length), getString(R.string.setting_temprature)};
        details = new String[]{getString(R.string.setting_metric), getString(R.string.setting_celsius)};
        listSettings = new ArrayList<SettingItem>();

        for (int i = 0; i < options.length; i++) {
            SettingItem settingItem = new SettingItem(options[i], details[i]);
            listSettings.add(settingItem);
        }

        list = (ListView) view.findViewById(R.id.list_setting);
        SettingListAdapter settingListAdapter = new SettingListAdapter(getActivity().getBaseContext(),R.layout.setting_item,listSettings);
        list.setAdapter(settingListAdapter);
        return view;

    }
}
