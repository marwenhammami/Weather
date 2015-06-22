package com.marouenhammami.weather.item;

/**
 * Created by maouenhammami on 09/06/15.
 */
public class NavDrawerItem {

    private String mTitle;
    private int mIcon;

    public NavDrawerItem(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmIcon() {
        return mIcon;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }
}
