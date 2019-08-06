package com.egemen.mapsdeneme;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBody {
    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("message")
    @Expose
    private ArrayList<MessageType> messages = null;


    public ArrayList<MessageType> getMessages() {
        return messages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
