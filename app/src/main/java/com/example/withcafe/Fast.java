package com.example.withcafe;

import android.app.Application;

public class Fast extends Application {
    private  int fast_num;

    public int getFast_num(){
        return fast_num;
    }

    public void setFast_num(int fast_num){
        this.fast_num = fast_num;
    }
}
