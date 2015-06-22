package com.marouenhammami.weather.item;

/**
 * Created by maouenhammami on 14/06/15.
 */
public class SettingItem {

    private String option;
    private String detail;

    public SettingItem(String option, String detail) {
        this.option = option;
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
