package com.example.withcafe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReviewInfo implements Serializable {
    private String cafe_name;
    private String title;
    private ArrayList<String> contents;
    private String user;
    private Date createdAt;
    private String numOfStar;
    private String seekBar_num;
    private String fast, ordspeed, slow;
    private String noisy, ordnoise, quiet;

    public ReviewInfo(String cafe_name, String title, ArrayList<String> contents, String user,Date createdAt, String numOfStar, String seekBar_num) {
        this.cafe_name = cafe_name;
        this.title = title;
        this.contents = contents;
        this.user = user;
        this.numOfStar = numOfStar;
        this.seekBar_num = seekBar_num;
        this.createdAt = createdAt;
    }

    public ReviewInfo(String cafe_name, String title, String user, Date createdAt, String numOfStar) {
        this.cafe_name = cafe_name;
        this.title = title;
        this.user = user;
        this.numOfStar = numOfStar;
        this.createdAt = createdAt;
    }

    public ReviewInfo(String cafe_name, String title, ArrayList<String> contents, String user,Date createdAt, String numOfStar, String seekBar_num,
                      String fast, String ordspeed, String slow,
                      String noisy,String ordnoise, String quiet) {
        this.cafe_name = cafe_name;
        this.title = title;
        this.contents = contents;
        this.user = user;
        this.numOfStar = numOfStar;
        this.seekBar_num = seekBar_num;
        this.createdAt = createdAt;
        this.fast = fast;
        this.ordspeed = ordspeed;
        this.slow = slow;
        this.noisy = noisy;
        this.ordnoise = ordnoise;
        this.quiet = quiet;
    }

    public Map<String, Object> getReviewInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("cafe_name",cafe_name);
        docData.put("title",title);
        docData.put("contents",contents);
        docData.put("user",user);
        docData.put("createdAt",createdAt);
        docData.put("numOfStar",numOfStar);
        docData.put("seekBar_num",seekBar_num);
        docData.put("fast",fast);
        docData.put("ordspeed",ordspeed);
        docData.put("slow",slow);
        docData.put("noisy",noisy);
        docData.put("ordnoise",ordnoise);
        docData.put("quiet",quiet);
        return  docData;
    }

    public String getCafe_name() { return cafe_name; }

    public void setCafe_name(String cafe_name) { this.cafe_name = cafe_name; }

    public String getFast() {return fast;}

    public void setFast(String fast) {this.fast = fast;}

    public String getOrdspeed() {return ordspeed;}

    public void setOrdspeed(String ordspeed) {this.ordspeed = ordspeed;}

    public String getSlow() {return slow;}

    public void setSlow(String slow) {this.slow = slow;}

    public String getNoisy() {return noisy;}

    public void setNoisy(String noisy) {this.noisy = noisy;}

    public String getOrdnoise() {return ordnoise;}

    public void setOrdnoise(String ordnoise) {this.ordnoise = ordnoise;}

    public String getQuiet() {return quiet;}

    public void setQuiet(String quiet) {this.quiet = quiet;}

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public ArrayList<String> getContents() { return contents; }

    public void setContents(ArrayList<String> contents) { this.contents = contents; }

    public void setSeekBar_num(String seekBar_num) { this.seekBar_num = seekBar_num; }

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getNumOfStar() { return numOfStar; }

    public void setNumOfStar(String numOfStar) { this.numOfStar = numOfStar; }

    public String getSeekBar_num() { return seekBar_num; }

}