package com.example.withcafe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CafeInfo implements Serializable {
    private String cafe_name;
    private ArrayList<String> picture_cafe;
    private ArrayList<String> menu;
    private String wifi;
    private String time_close;
    private String time_open;
    private String district;
    private ArrayList<String> near_Univ;
    private ArrayList<String> near_Subway;

    public CafeInfo(String cafe_name, ArrayList<String> menu, ArrayList<String> picture_cafe, String wifi,
                     String time_open,  String time_close, String district, ArrayList<String> near_Univ, ArrayList<String> near_Subway) {
        this.cafe_name = cafe_name;
        this.menu = menu;
        this.picture_cafe = picture_cafe;
        this.wifi = wifi;
        this.time_close = time_close;
        this.time_open = time_open;
        this.district = district;
        this.near_Univ = near_Univ;
        this.near_Subway = near_Subway;
    }

    public Map<String, Object> getCafeInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("cafe_name",cafe_name);
        docData.put("picture_cafe",picture_cafe);
        docData.put("menu",menu);
        docData.put("wifi",wifi);
        docData.put("time_close",time_close);
        docData.put("time_open",time_open);
        docData.put("district",district);
        docData.put("near_Univ",near_Univ);
        docData.put("near_Subway",near_Subway);
        return  docData;
    }

    public ArrayList<String> getNear_Univ() {return near_Univ; }

    public void setNear_Univ(ArrayList<String> near_Univ) { this.near_Univ = near_Univ; }

    public ArrayList<String> getNear_Subway() { return near_Subway; }

    public void setNear_Subway(ArrayList<String> near_Subway) { this.near_Subway = near_Subway; }

    public String getCafe_name() {
        return cafe_name;
    }

    public void setCafe_name(String cafe_name) {
        this.cafe_name = cafe_name;
    }

    public ArrayList<String> getPicture_cafe() {
        return picture_cafe;
    }

    public void setPicture_cafe(ArrayList<String> picture_cafe) { this.picture_cafe = picture_cafe; }

    public ArrayList<String> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<String> menu) {
        this.menu = menu;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getTime_close() {
        return time_close;
    }

    public void setTime_close(String time_close) {
        this.time_close = time_close;
    }

    public String getTime_open() {
        return time_open;
    }

    public void setTime_open(String time_open) {
        this.time_open = time_open;
    }

    public String getDistrict() { return district; }

    public void setDistrict(String district) { this.district = district; }
}
