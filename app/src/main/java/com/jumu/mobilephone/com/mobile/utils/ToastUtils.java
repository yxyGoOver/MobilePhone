package com.jumu.mobilephone.com.mobile.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void show(Context context,String msg){

        Toast.makeText(context, msg, 1).show();
    }
}
