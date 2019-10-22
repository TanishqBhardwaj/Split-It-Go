package com.example.SplitItGo;

import java.util.HashMap;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class PreferenceUtils{

        SharedPreferences pref;
        Editor editor;
        Context context;

        private static final String TOKEN = "token";
        private static final String IS_LOGIN = "IsLoggedIn";
        public static final String KEY_USERNAME = "username";

        public PreferenceUtils(Context context){
            this.context = context;
            pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
            editor = pref.edit();
        }

        public void createLoginSession(String username, String token){

            editor.putBoolean(IS_LOGIN, true);
            editor.putString(KEY_USERNAME, username);
            editor.putString(TOKEN, token);
            editor.apply();
        }

        public boolean checkLogin() {

            if (!this.isLoggedIn()) {
                return false;
            }
            return true;
        }

        public HashMap<String, String> getUserDetails(){
            HashMap<String, String> user = new HashMap<String, String>();
            user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
//            user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
            return user;
        }

        public void logoutUser(){
            editor.clear();
            editor.commit();
        }

        public boolean isLoggedIn(){
            Log.d(IS_LOGIN, "isLoggedIn: ");
            return pref.getBoolean(IS_LOGIN, false);
        }
}
