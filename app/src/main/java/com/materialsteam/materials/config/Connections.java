package com.materialsteam.materials.config;

public class Connections {
//    private static final String BASE_URL = "http://localhost/api/";
    private static final String BASE_URL = "http://192.168.0.103/materials-api/api/";
    
    // EMAIL CONFIG
    public static final String EMAIL = "alvinmokoagow5@gmail.com";
    public static final String PASSWORD = "alvin16.";

    // user
    public static String URL_REGIS = BASE_URL + "auth/signup";
    public static String URL_LOGIN = BASE_URL + "auth/login";
    public static String URL_GET = BASE_URL + "auth/user";
    public static String URL_UPDATE_PROFIL = BASE_URL + "auth/update";
    public static String URL_VERIFIKASI = BASE_URL + "auth/verifikasi";
}
