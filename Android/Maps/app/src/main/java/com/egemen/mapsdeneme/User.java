package com.egemen.mapsdeneme;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("isMarkerSeen")
    @Expose
    private List<Object> isMarkerSeen = null;

    User(String username, String firstName, String lastName, String password, String email, String _id) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.id = id;
    }

    User(String username, String firstName, String lastName, String password, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsMarkerSeen(List<Object> isMarkerSeen) {
        this.isMarkerSeen = isMarkerSeen;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public List<Object> getIsMarkerSeen() {
        return isMarkerSeen;
    }

    @Override
    public String toString(){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.id = id;
        return "Username:"+username+
                "\nFirst Name:"+firstName+
                "\nLast Name:"+ lastName+
                "\nPassword:"+password+
                "\nEmail:"+email+
                "\nId:"+id+
                "\nIs Marker Seen:"+isMarkerSeen;

    }
}

