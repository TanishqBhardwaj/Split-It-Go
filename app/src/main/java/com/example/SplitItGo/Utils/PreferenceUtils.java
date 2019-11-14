package com.example.SplitItGo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils{

        private SharedPreferences pref;
        public Context context;

        private static final String TOKEN = "token";
        private static final String IS_LOGIN = "IsLoggedIn";
        public static final String KEY_USERNAME = "username";
        private static final String KEY_USER_ID = "user_id";

        public PreferenceUtils(Context context){
            this.context = context;
            pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        }

        public void createLoginSession(String username, String token, String user_id){
            pref.edit().putBoolean(IS_LOGIN, true).apply();
            pref.edit().putString(KEY_USERNAME, username).apply();
            pref.edit().putString(TOKEN, token).apply();
            pref.edit().putString(KEY_USER_ID, user_id).apply();
        }

        public boolean checkLogin() {
            return pref.getBoolean(IS_LOGIN, false);
        }

        public String getKeyUsername() {
            return pref.getString(KEY_USERNAME, null);
        }

        public String getKeyUserId() {
            return pref.getString(KEY_USER_ID, null);
        }

        public void logoutUser(){
            pref.edit().clear().apply();
        }
}