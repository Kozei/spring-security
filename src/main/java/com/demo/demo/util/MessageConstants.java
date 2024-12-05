package com.demo.demo.util;

public class MessageConstants {

    private MessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String PUBLIC_API = "/public/**";
    public static final String PRIVATE_API = "/private/**";
    public static final String ORIGINAL_PATH = "originalRequestUri";
    public static final String LOGIN_API = "/public/login";

    public static final String LOGIN_FAILED = "login failed. Please try again with valid credentials";
    public static final String ACCESS_DENIED = "You have no permission to perform this action";

    public static final String USER = "user";
    public static final String ADMIN = "admin";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    //must be stored in vault
    public final static String SECRET_KEY = "a3d2f8b71e3f0e5c9e4d8ab0c7f2e0a9b1c2d3e4f5a6b7c8e9f0a1b2c3d4e5f6";

    public final static String POST_METHOD = "POST";

}
