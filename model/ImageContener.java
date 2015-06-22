package com.marouenhammami.weather.model;

import android.graphics.Bitmap;

import com.marouenhammami.weather.utility.FlickrManager;

/**
 * Created by maouenhammami on 21/06/15.
 */
public class ImageContener {
    private String id;
    private int position;
    private Bitmap photo;
    private String largeURL;
    private String owner;
    private String secret;
    private String server;
    private String farm;

    private String extension = ".jpg";

    public ImageContener(String id, String owner, String secret, String server, String farm) {
        super();
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        setLargeURL(createPhotoURL(FlickrManager.PHOTO_LARGE, this));

    }

    public String getLargeURL() {
        return largeURL;
    }
    public void setLargeURL(String largeURL) {
        this.largeURL = largeURL;
    }

    private String createPhotoURL(int photoType, ImageContener imgCon) {
        String tmp = null;
        tmp = "http://farm" + imgCon.farm + ".staticflickr.com/" + imgCon.server + "/" + imgCon.id + "_" + imgCon.secret;
        switch (photoType) {
            case FlickrManager.PHOTO_LARGE:
                tmp += "_z";
                break;

        }
        tmp += extension;
        return tmp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

}
