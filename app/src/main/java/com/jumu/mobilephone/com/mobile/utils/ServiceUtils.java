package com.jumu.mobilephone.com.mobile.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtils {
    public static boolean isRunning(Context context,String serviceName){
         //1.获取activityMangger管理者对象，可以去获取当前手机正在运行的所有服务
        ActivityManager mActivityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //2.获取手机中运行的服务
        List<ActivityManager.RunningServiceInfo> runningServices = mActivityManager.getRunningServices(100);
       //3.遍历获取的所有的服务集合，拿到每一个服务的类的名称，和传递进来的类的名称作对比，如果一致，说明服务正在运行
        for (ActivityManager.RunningServiceInfo runningServiceInfo:runningServices) {
            //4.获取每一个真正运行服务的名称
            if (serviceName.equals(runningServiceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
