package com.jumu.mobilephone.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;
import com.jumu.mobilephone.com.mobile.utils.ToastUtils;

/**
 * @author Administrator
 */
public class MyBootReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(TAG, "onReceive: 开机了" + intent.getAction());
        ToastUtils.show(context, "开机了");


        //发送报警短信
        //1获取开机后的sim卡号
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String simSerialNumber = telephonyManager.getSimSerialNumber()+"xxx";
        //2.sp中储存的序列卡号
        String sim_number = SharedPerferenceUtils.getString(context, ConstantValue.SIM_NUMBER, "");
        //3.比对
        if (!simSerialNumber.equals(sim_number)){
          //4.发送报警短信给安全号码
            SmsManager aDefault = SmsManager.getDefault();
            aDefault.sendTextMessage("17651276264",null,"sim changge!!!",null,null);
        }
    }
}
