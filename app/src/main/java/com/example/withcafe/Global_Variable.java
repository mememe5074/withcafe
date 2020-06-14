package com.example.withcafe;

import android.app.Application;

public class Global_Variable extends Application {
    private String Gb_UnivName = "";
    private String Gb_SublineName = "";
    private String Gb_SubwayName = "";

    public String getGb_UnivName() {
        return Gb_UnivName;
    }

    public void setGb_UnivName(String gb_UnivName) { this.Gb_UnivName = gb_UnivName; }

    public String getGb_SublineName() {
        return Gb_SublineName;
    }

    public void setGb_SublineName(String gb_SublineName) {
        this.Gb_SublineName = gb_SublineName;
    }

    public String getGb_SubwayName() { return Gb_SubwayName; }

    public void setGb_SubwayName(String gb_SubwayName) {
        this.Gb_SubwayName = gb_SubwayName;
    }
}
