package com.example.withcafe;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AverageInfo implements Serializable {
    private String numOfStar;
    private String seekBar_num;

    public AverageInfo(String numOfStar, String seekBar_num){
        this.numOfStar = numOfStar;
        this.seekBar_num = seekBar_num;
    }

    public Map<String, Object> getReviewInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("numOfStar",numOfStar);
        docData.put("seekBar_num",seekBar_num);
        return  docData;
    }

    public void setSeekBar_num(String seekBar_num) { this.seekBar_num = seekBar_num; }

    public String getNumOfStar() { return numOfStar; }

    public void setNumOfStar(String numOfStar) { this.numOfStar = numOfStar; }

    public String getSeekBar_num() { return seekBar_num; }

}