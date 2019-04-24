package com.jumu.mobilephone.com.mobile.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jumu.mobilephone.R;
import com.jumu.mobilephone.engine.AddressDao;

public class QueryAddress extends Activity {
    private String address;
    private EditText et_phone;
    private Button bt_query;
    private TextView tv_query_result;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //控件使用查询结果
            tv_query_result.setText(address);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_address_activity);

       // AddressDao.getAddress("13000201234");
       initUI();
    }

    private void initUI() {
         et_phone = findViewById(R.id.et_phone);
       bt_query = findViewById(R.id.bt_query);
        tv_query_result = findViewById(R.id.tv_query_result);

        //1.查询按钮的点击事件
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phoneNumber)){
                //2,查询是耗时操作
                query(phoneNumber);
                }else {
                    //为空的话，抖动
                               //抖动
                    Animation shake = AnimationUtils.loadAnimation(
                            getApplicationContext(), R.anim.shake);
                    //interpolator插补器,数学函数
                    //自定义插补器
//					shake.setInterpolator(new Interpolator() {
//						//y = 2x+1
//						@Override
//						public float getInterpolation(float arg0) {
//							return 0;
//						}
//					});
//					Interpolator
//					CycleInterpolator
                    et_phone.startAnimation(shake);
                 //手机震动效果
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                 //震动毫秒值
                    vibrator.vibrate(2000);
                    //规律震动(震动规则(不震动时间，震动时间，不震动时间，震动时间......)，重复次数)
                    vibrator.vibrate(new long[]{2000,5000,2000,5000},-1);

                }
            }
        });
        //5.实时查询，监听输入框中文本变化
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
             String   afterTextChangeds  =et_phone.getText().toString();
             query(afterTextChangeds);
            }
        });
    }
    /**
     * 耗时操作
     * 获取电话号码归属地
     *查询电话号码
     * @param phoneNumber
     */
    private void query(final String phoneNumber) {
         new Thread(new Runnable() {
           @Override
               public void run() {
                address = AddressDao.getAddress(phoneNumber);
               //3,消息机制，告知主线程查询结束，可以去使用查询结果
              mHandler.sendEmptyMessage(0);
           }
        }).start();
    }
}
