package com.scalx.locationly.config;

import com.scalx.locationly.model.UserType;

public class AppParameters {
    public static final String REST_API_URL="http://localhost:8082/";
    public static boolean doubleBackToExitPressedOnce = false;
    public static UserType appUser = new UserType();
}
