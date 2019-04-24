package com.jumu.mobilephone.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

public class LoctionService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        //获取手机的经纬度坐标
        //1.获取位置管理者对象
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2.以最优的方式获取经纬度
        Criteria criteria = new Criteria();
        //允许花费
        criteria.setCostAllowed(true);
        //指定获取经纬度的精确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String bestProvider = locationManager.getBestProvider(criteria, true);
        //3.在一定时间间隔，移动一段距离后获取经纬度坐标
        MyLoctionListenr myLoctionListenr = new MyLoctionListenr();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLoctionListenr);


    }

         class MyLoctionListenr implements LocationListener{
             @Override
             public void onLocationChanged(Location location) {
                 //拿到经度
                 double longitude = location.getLongitude();
                 //拿到纬度
                 double latitude = location.getLatitude();
                 //4.发送短信
                 SmsManager aDefault = SmsManager.getDefault();
                   aDefault.sendTextMessage("17651276264",null,"longitude"+longitude+",latitude"+latitude,null,null);
             }
             @Override
             public void onStatusChanged(String provider, int status, Bundle extras) {

             }
             @Override
             public void onProviderEnabled(String provider) {

             }
             @Override
             public void onProviderDisabled(String provider) {

             }
         }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
