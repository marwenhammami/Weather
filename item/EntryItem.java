package com.marouenhammami.weather.item;

import android.graphics.Bitmap;

/**
 * Created by maouenhammami on 13/06/15.
 */

public class EntryItem {


    private Bitmap imageId;
    private final String title;
    private final String temp;

    public EntryItem(Bitmap imageId, String title, String temp) {
        this.imageId = imageId;
        this.title = title;
        this.temp = temp;
    }

    public Bitmap getImageId() {
        return imageId;
    }

    public void setImageId(Bitmap imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        return title + "\n" + temp;
    }
}
