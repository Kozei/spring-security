package com.demo.demo.util;

public class MessageConstants {

    private MessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String PUBLIC_API = "/public/**";
    public static final String PRIVATE_API = "/private/**";

    public static final String LOGIN_FAILED = "login failed. Please try again with valid credentials";
    public static final String ACCESS_DENIED = "You have no permission to perform this action";
}
