package com.materialsteam.materials.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.materialsteam.materials.models.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "sharedprefmanager";
    private static final String KEY_KD_USER = "kduser";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NO_TELEPON = "notelpon";
    private static final String KEY_ALAMAT = "alamat";

    private static SharedPrefManager prefManager;
    private static Context context;

    public SharedPrefManager(Context context){
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if (prefManager == null){
            prefManager = new SharedPrefManager(context);
        }
        return prefManager;
    }

    public void setUser(User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_KD_USER, user.getKd_user());
        editor.putString(KEY_NAMA, user.getNama());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_NO_TELEPON, user.getNo_telpon());
        editor.putString(KEY_ALAMAT, user.getAlamat());
        editor.apply();
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_KD_USER, null) != null;
    }

    public User getUser(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_KD_USER, null),
                sharedPreferences.getString(KEY_NAMA, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_NO_TELEPON, null),
                sharedPreferences.getString(KEY_ALAMAT, null)
        );
    }

    public void logout(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        context.startActivity(new Intent(context, Login.class));
    }
}
