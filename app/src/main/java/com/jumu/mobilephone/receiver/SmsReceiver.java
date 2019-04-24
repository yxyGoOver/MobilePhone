package com.jumu.mobilephone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.jumu.mobilephone.R;
import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;
import com.jumu.mobilephone.service.LoctionService;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //1.判断是否开启防盗保护
        boolean open_security = SharedPerferenceUtils.getBoolen(context, ConstantValue.OPEN_SECURITY, false);
         if (open_security){
             //2.获取短信内容
             Object[] pdus = (Object[]) intent.getExtras().get("pdus");
             //3.循环遍历短信过程
             for (Object object:pdus) {
                 //4.获取短信对象
                 SmsMessage fromPdu = SmsMessage.createFromPdu((byte[]) object);
                 //5.过去短信对象的基本信息
                 String originatingAddress = fromPdu.getOriginatingAddress();
                 String messageBody = fromPdu.getMessageBody();
                 //6.判断是否包含播放音乐的关键字
                 if(messageBody.contains("#*alarm*#")){
                   //7.播放音乐
                     MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                     mediaPlayer.setLooping(true);
                     mediaPlayer.start();
                 }

                 if(messageBody.contains("#*loction*#")){
                     //8.启动位置服务
                     Intent intentservice = new Intent(context, LoctionService.class);
                     context.startService(intentservice);
                 }

                 if(messageBody.contains("#*lockscrenn*#")){
                  //9.锁屏

                 }

                 if(messageBody.contains("#*wipedate*#")){
                     //10.清除数据

                 }
             }
         }
    }
}
