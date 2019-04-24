package com.jumu.mobilephone.com.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jumu.mobilephone.R;
import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;

public  class ToastLocationActivity extends Activity {
    private   ImageView drag;
    private  Button bt_top;
    private  Button bt_bottom;
    private WindowManager window_service;
    int height,width;
   private long[]  mHits= new long[2];
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_location_activity);

        initUI();
    }

    private void initUI() {
        //可拖拽双击居中的图片控件
        drag = findViewById(R.id.drag);
        bt_top = findViewById(R.id.bt_top);
        bt_bottom = findViewById(R.id.bt_bottom);

        window_service = (WindowManager) getSystemService(WINDOW_SERVICE);

        height = window_service.getDefaultDisplay().getHeight();
        width = window_service.getDefaultDisplay().getWidth();

        final int loctionX = SharedPerferenceUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        final int loctionY = SharedPerferenceUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);

        //左上角坐标作用在drag上
        //drag在相对布局中，所以所在位置的规则需要由相对布局提供

        //指定宽度都为WRAP_CONTENT
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //将左上角的坐标作用在drag对应规则参数上
        layoutParams.leftMargin= loctionX;
        layoutParams.topMargin=loctionY;
        //将以上规则作用在drag上
        drag.setLayoutParams(layoutParams);

        if (loctionY>height/2){
            bt_bottom.setVisibility(View.INVISIBLE);
            bt_top.setVisibility(View.VISIBLE);
        }else {
            bt_bottom.setVisibility(View.VISIBLE);
            bt_top.setVisibility(View.INVISIBLE);
        }
        //监听按钮单击事件
        drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            System.arraycopy(mHits,1,mHits,0,mHits.length-1);
            mHits[mHits.length-1]=SystemClock.uptimeMillis();
            if (mHits[mHits.length-1]-mHits[0]<500){
                //满足双击后对应的代码
                int leftCententX = width / 2 - loctionX / 2;
                int rightCententX = width / 2 + loctionX / 2;
                int topCententY = height / 2 - loctionY / 2;
                int bottomCententY = height / 2 + loctionY / 2;
                //控件按以上规则显示
                drag.layout(leftCententX,topCententY,rightCententX,bottomCententY);
                //存储最终位置
                SharedPerferenceUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_X,drag.getLeft());
                SharedPerferenceUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_Y,drag.getTop());
            }
            }
        });
         //监听控件拖拽事件
        drag.setOnTouchListener(new View.OnTouchListener() {
          private   int rawX;
           private int rawY;
           private int moveX,moveY;
           private int endX,endY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        rawX = (int) event.getRawX();
                        rawY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveX = (int) event.getRawX();
                        moveY = (int) event.getRawY();

                        endX = moveX - rawX;
                        endY = moveY-rawY;
                     //1.当前空间所在屏幕的（左，上）角位置
                        int leftX = drag.getLeft() + endX;
                        int topY = drag.getTop() + endY;
                        int RightX = drag.getRight() + endX;
                        int BottomY = drag.getBottom() + endY;
                        //容错处理
                           if (leftX<0){
                               return true;
                           }
                           if (topY<0){
                               return true;
                           }
                           if (RightX>width){
                               return true;
                           }
                           //pingmu1
                           if (BottomY>height-30){
                               return true;
                           }
                         if (topY>height/2){
                                bt_bottom.setVisibility(View.INVISIBLE);
                                bt_top.setVisibility(View.VISIBLE);
                         }else {
                             bt_bottom.setVisibility(View.VISIBLE);
                             bt_top.setVisibility(View.INVISIBLE);
                         }
                        //2.告知移动的控件，按计算出来的坐标去做展示
                        drag.layout(leftX,topY,RightX,BottomY);
                        //3.重新赋值一次控件其实位置
                        rawX= (int) event.getRawX();
                        rawY= (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //4,储存移动的位置
                        SharedPerferenceUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_X,drag.getLeft());
                        SharedPerferenceUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_Y,drag.getTop());

                        break;
                }
                //false即响应点击也响应拖拽，true只响应点击
                return false;
            }
        });
    }
}
