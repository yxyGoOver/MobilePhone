package com.jumu.mobilephone.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jumu.mobilephone.R;
import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;
import com.jumu.mobilephone.engine.AddressDao;

/**
 * @author Administrator
 */
public class AddressService extends Service {
    private static final String TAG="AddressService";
   private TelephonyManager telephonyManager;
   private PhoneStateListener mPhoneStateListener;
  private WindowManager mWindowManager;
  private  String mAddress;
   private View toast_view;
   private TextView tv_toast;
   private int[] icons;
   private int height,width;
  private   InnerOutCallRceiver innerOutCallRceiver;
     private final    WindowManager.LayoutParams    mParams  =new WindowManager.LayoutParams();

     private Handler mHandler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
              tv_toast.setText(mAddress);
         }
     };
    @Override
    public void onCreate() {
   //第一次开启服务后，就需要管理服务的土司
     //电话状态监听
        //1.电话管理者对象
         telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
         //2.监听电话状态
        mPhoneStateListener=new MyPhoneStateListener();
        telephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
       //获取窗体对象
       mWindowManager= (WindowManager) getSystemService(WINDOW_SERVICE);
       //获取窗体宽高
        height = mWindowManager.getDefaultDisplay().getHeight();
         width = mWindowManager.getDefaultDisplay().getWidth();

         //监听播出电话的广播过滤条件
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL); //播出一个电话的广播
        //创建广播接收者
        innerOutCallRceiver = new InnerOutCallRceiver();
        registerReceiver(innerOutCallRceiver,intentFilter);

        super.onCreate();
    }
    class  MyPhoneStateListener extends PhoneStateListener{
        //3,手动重写，电话状态发生改变会触发的方法

        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    //空闲状态
                    Log.d(TAG,"电话已挂断，空闲了。。。。");
                    //挂断电话的时候，窗体需要移除土司
                    if (mWindowManager != null && toast_view != null) {
                        mWindowManager.removeView(toast_view);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //摘机状态

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃状态
                    Log.d(TAG, "onCallStateChanged: 来电了。。。。。。");
                    PhoneStateToast(phoneNumber);
                    break;
            }
            super.onCallStateChanged(state,phoneNumber);
        }
    }

    public  void PhoneStateToast(String phoneNumber){
     //   Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();

        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
      //  params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        //在响铃的时候显示土司，和电话类型一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
              //  | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        //指定土司的所在位置
       params.gravity= Gravity.LEFT+Gravity.TOP;
       //土司效果显示
          toast_view = View.inflate(getApplicationContext(), R.layout.toast_view, null);
        tv_toast = toast_view.findViewById(R.id.tv_toast);

        toast_view.setOnTouchListener(new View.OnTouchListener() {
             int startX,startY,moveX,moveY,disX,disY;
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                  switch (event.getAction()){
                      case MotionEvent.ACTION_DOWN:
                          startX = (int) event.getRawX();
                           startY = (int) event.getRawY();
                          break;
                      case MotionEvent.ACTION_MOVE:
                         moveX= (int) event.getRawX();
                          moveY= (int) event.getRawY();

                          disX=moveX-startY;
                          disY=moveY-startY;

                          params.x=params.x+disX;
                          params.y=params.y+disY;
                          //容错处理
                          if (params.x<0){
                              params.x=0;
                          }
                          if (params.y<0){
                              params.y=0;
                          }
                          if (params.x>width-toast_view.getWidth()){
                                  params.x=width-toast_view.getWidth();
                          }
                          if (params.y>height-toast_view.getHeight()-22){
                                  params.y=height-toast_view.getHeight()-22;
                          }
                          //告诉窗体土司需要按照手势的移动，去做位置的更新
                           mWindowManager.updateViewLayout(toast_view,params);

                          startX = (int) event.getRawX();
                          startY = (int) event.getRawY();
                          break;
                      case MotionEvent.ACTION_UP:
               SharedPerferenceUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_X,params.x);
               SharedPerferenceUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_Y,params.y);
                          break;
                  }
                 return true;
             }
         });

        //读取设置中sp储存的下x,y的值
        params.x = SharedPerferenceUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        params.y = SharedPerferenceUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);

        //从sp中获取色值文字的索引，匹配图片，用作展示
        icons = new int[]{ R.drawable.call_locate_white, R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,R.drawable.call_locate_gray, R.drawable.call_locate_green};
        int anInt = SharedPerferenceUtils.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
         toast_view.setBackgroundResource(icons[anInt]);

        //在窗体上挂载view
        mWindowManager.addView(toast_view,mParams);
          //获取到了来电号码以后,需要做来电号码查询
          query(phoneNumber);
    }

    private void query(final String phoneNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(phoneNumber);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        //销毁土司
        //取消对电话的监听状态
        if (mPhoneStateListener != null && telephonyManager != null){
            telephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        //注销广播
        if (innerOutCallRceiver != null){
            unregisterReceiver(innerOutCallRceiver);
        }
        super.onDestroy();
    }


    private class InnerOutCallRceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //接收到此广播后,需要显示自定义的土司，显示播出归属地号码
             //显示播出归属地号码getResultData();
            String phone = getResultData();
            PhoneStateToast(phone);
        }
    }
}
