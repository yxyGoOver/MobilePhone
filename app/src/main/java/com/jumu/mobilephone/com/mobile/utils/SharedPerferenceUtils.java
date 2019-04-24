package com.jumu.mobilephone.com.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPerferenceUtils {
    private static final String CONFIG="config";
    public static void putInt(Context context,String key,int value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key,value);
        edit.commit();
    }
    public static int getInt(Context context,String key,int defValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        int anInt = sharedPreferences.getInt(key, defValue);
        return anInt;
    }
    public static void putString(Context context,String key,String value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,value).commit();
    }
    public static String getString(Context context,String key,String defValue){
       SharedPreferences sharedPreferences=context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        return  sharedPreferences.getString(key,defValue);
    }
    public static void putBoolen(Context context,String key,Boolean value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key,value).commit();
    }
    public static boolean getBoolen(Context context,String key,boolean defValue){
       SharedPreferences sharedPreferences=context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(key,defValue);
    }

    public static void remover(Context context,String key) {

           SharedPreferences sharedPreferences=context.getSharedPreferences(CONFIG,Context.MODE_PRIVATE);
           sharedPreferences.edit().remove(key).commit();
    }
}
