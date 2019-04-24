package com.jumu.mobilephone.com.mobile.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jumu.mobilephone.R;
import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.ServiceUtils;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;
import com.jumu.mobilephone.databasesql.dao.BlackNumberDao;
import com.jumu.mobilephone.service.AddressService;
import com.jumu.mobilephone.service.BlackNumberService;
import com.jumu.mobilephone.view.SettingChickView;
import com.jumu.mobilephone.view.SettingView;

public class SettingActivity extends AppCompatActivity {
    TextView auto_update;
    TextView tv_des;
    String[] mToastStyle;
    int toast_style;
    SettingChickView scv_address_toast;
    SettingChickView scv_locaiton_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUI();
        initAddress();
        initToastStyle();
        initLocation();
        initBlackNumber();
    }

    private void initBlackNumber() {
        final SettingView settingView_blacknumber = findViewById(R.id.settingView_blacknumber);
        boolean isRunning = ServiceUtils.isRunning(this, ".service.BlackNumberService");
    settingView_blacknumber.setCheck(isRunning);

    settingView_blacknumber.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isCheck = settingView_blacknumber.isChecks();
            settingView_blacknumber.setCheck(!isCheck);
            if (!isCheck){
                //开启服务
                    startService(new Intent(getApplicationContext(), BlackNumberService.class));
            }else {
                //关闭服务
                stopService(new Intent(getApplicationContext(),BlackNumberService.class));
            }
        }
    });
    }

    /**
     *
     */
    private void initLocation() {
        scv_locaiton_view = findViewById(R.id.scv_locaiton);
        scv_locaiton_view.setTitle("归属地提示框位置");
        scv_locaiton_view.setDes("设置归属地提示框位置");
            scv_locaiton_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),ToastLocationActivity.class));
                }
            });
    }

    private void initToastStyle() {
        scv_address_toast = findViewById(R.id.scv_address_toast);
        //适述
        scv_address_toast.setTitle("电话归属地样式选择");
        //1.创建描述文字所在的string类型数组
        mToastStyle = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        //2.SP获取土司显示样式的索引值（int），用于描述文字

        toast_style = SharedPerferenceUtils.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        //3,通过索引，获取字符串数组的文字，显示给内容描述控件
        scv_address_toast.setDes(mToastStyle[toast_style]);
         //4.监听点击事件，弹出对话框
          scv_address_toast.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //显示对话框
                 showToastStyleDialog();
                //  Toast.makeText(SettingActivity.this, "6666666", Toast.LENGTH_SHORT).show();
              }
          });
    }

    protected void showToastStyleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setIcon(R.mipmap.ic_launcher_round);
          builder.setTitle("请选择归属地样式");
          builder.setSingleChoiceItems(mToastStyle, toast_style, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                     SharedPerferenceUtils.putInt(getApplicationContext(),ConstantValue.TOAST_STYLE,which);
                     dialog.dismiss();
                     scv_address_toast.setDes(mToastStyle[which]);
              }
          });
          builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
              }
          });
          builder.show();
    }

    private void initAddress() {
        final SettingView settingView_address = findViewById(R.id.settingView_address);
        //对服务开启状态做显示
        boolean isRunning = ServiceUtils.isRunning(getApplicationContext(), "com.jumu.mobilephone.service.AddressService");
         settingView_address.setCheck(isRunning);

        //点击过程中，状态的切换过程(取反)
        settingView_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checks = settingView_address.isChecks();
                settingView_address.setCheck(!checks);
                if (!checks){
                    //开启服务,管理土司
                    startService(new Intent(getApplicationContext(),AddressService.class));
                }else {
                    //关闭服务，不需要显示土司
                    stopService(new Intent(getApplicationContext(),AddressService.class));
                }
            }
        });
    }

    private void initUI() {
         final SettingView settingView= findViewById(R.id.settingView_update);
        boolean open_update = SharedPerferenceUtils.getBoolen(this, ConstantValue.OPEN_UPDATE, false);
         settingView.setCheck(open_update);

        final CheckBox cb_box =findViewById(R.id.cb_box);
        auto_update = findViewById(R.id.auto_update);
        tv_des = findViewById(R.id.tv_des);

        settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void
            onClick(View v) {
           boolean   is=settingView.isChecks();
              settingView.setCheck(!is);

              SharedPerferenceUtils.putBoolen(getApplicationContext(),ConstantValue.OPEN_UPDATE,!is);
            }
        });
    }


}
