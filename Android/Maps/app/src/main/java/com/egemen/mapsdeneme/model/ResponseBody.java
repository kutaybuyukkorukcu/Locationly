package com.egemen.mapsdeneme.model;

import com.egemen.mapsdeneme.model.MessageType;
import com.egemen.mapsdeneme.model.UserType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBody {
    @SerializedName("user")
    @Expose
    private UserType userType;

    @SerializedName("message")
    @Expose
    private ArrayList<MessageType> messages = null;

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("markerId")
    @Expose
    private ArrayList<String> markerId = null;

    public ArrayList<String> getMarkerId() {
        return markerId;
    }

    public void setMarkerId(ArrayList<String> markerId) {
        this.markerId = markerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<MessageType> getMessages() {
        return messages;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

}
