package com.example.SplitItGo;

import java.util.HashMap;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils{

        private SharedPreferences pref;
        private Context context;

        private static final String TOKEN = "token";
        private static final String IS_LOGIN = "IsLoggedIn";
        public static final String KEY_USERNAME = "username";

        public PreferenceUtils(Context context){
            this.context = context;
            pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        }

        public void createLoginSession(String username, String token){
            pref.edit().putBoolean(IS_LOGIN, true).apply();
            pref.edit().putString(KEY_USERNAME, username).apply();
            pref.edit().putString(TOKEN, token).apply();
        }

        public boolean checkLogin() {
            return pref.getBoolean(IS_LOGIN, false);
        }

//        public HashMap<String, String> getUserDetails(){
//            HashMap<String, String> user = new HashMap<>();
//            user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
////          user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
//            return user;
//        }

        public String getKeyUsername() {
            return pref.getString(KEY_USERNAME, null);
        }

        public void logoutUser(){
            pref.edit().clear().apply();
        }
}