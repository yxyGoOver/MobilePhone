package com.jumu.mobilephone.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.jumu.mobilephone.BlackNumber;
import com.jumu.mobilephone.databasesql.dao.BlackNumberDao;

public class BlackNumberService extends Service {
    private         BlackNumberDao mDao;
    InnerSnsRecevie innerSnsRecevie;
    private         TelephonyManager telephonyManager;
    @Override
    public void onCreate() {
        //拦截短信
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
         intentFilter.setPriority(1000);

        innerSnsRecevie = new InnerSnsRecevie();
        registerReceiver(innerSnsRecevie,intentFilter);

        //监听电话状态
        //1.获取电话管理者对象
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
         //2.监听电话状态
        PhoneStateLis phoneStateLis = new PhoneStateLis();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (innerSnsRecevie != null){
            unregisterReceiver(innerSnsRecevie);
        }
        super.onDestroy();
    }

    private class InnerSnsRecevie extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
             //获取短信内容，获取发送短信的电话号码，如果此电话号码在黑名单中，
             //并且拦截模式为1，或3，拦截短信
             //1.获取短信内容
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            //2.便利循环过程
            for (Object obj:pdus) {
               //3.获取短信对象
                SmsMessage fromPdu = SmsMessage.createFromPdu((byte[]) obj);
                //5.获取短信对象的基本信息
                String originatingAddress = fromPdu.getOriginatingAddress(); //短信号码
                String messageBody = fromPdu.getMessageBody();

                mDao = BlackNumberDao.getInstance(context);
                int mode = mDao.getMode(originatingAddress);

                if (mode == 1 || mode == 3) {
                    //拿到短信
                    abortBroadcast();//中断广播接收者
                }

            }
        }
    }

    private class PhoneStateLis extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    endCall();
                    break;
            }
            super.onCallStateChanged(state,phoneNumber);
        }
    }

    private void endCall() {

    }
}
