package com.example.withcafe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReplyInfo implements Serializable {
    private String reply_contents;
    private String user;
    private Date createdAt;



    public Map<String, Object> getReplyInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("reply_contents",reply_contents);
        docData.put("user",user);
        docData.put("createdAt",createdAt);
        return  docData;
    }

    public ReplyInfo(String reply_contents, String user, Date createdAt) {
        this.reply_contents = reply_contents;
        this.user = user;
        this.createdAt = createdAt;

    }
    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getReply_contents() {
        return reply_contents;
    }

    public void setReply_contents(String reply_contents) {
        this.reply_contents = reply_contents;
    }

}
