package com.jumu.mobilephone.com.mobile.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
 private static final String TAG="Md5Utils";
    public static String encoder(String Md5pwd){
        try {
            Md5pwd=Md5pwd+"abcdef";
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(Md5pwd.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b:bytes){
                int i=b & 0xff;
                String HaxString = Integer.toHexString(i);
                if (HaxString.length()<2){
                    HaxString="0"+HaxString;
                }
               stringBuffer.append(HaxString);
            }
            Log.d(TAG, "encoder: "+stringBuffer.toString());
             return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
           return "";
    }


}
