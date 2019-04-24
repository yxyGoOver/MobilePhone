package com.jumu.mobilephone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jumu.mobilephone.base.BaseSetupActivity;
import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;
import com.jumu.mobilephone.com.mobile.utils.ToastUtils;
import com.jumu.mobilephone.view.SettingView;

/**
 * @author Administrator
 */
public class Setup2Activity extends BaseSetupActivity {
    SettingView siv_sim_bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup2_activity);
        initUI();
    }

    @Override
    protected void showPrePage() {
        Intent intent=new Intent(this,SetupActivity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        String simSerialNumbers = SharedPerferenceUtils.getString(getApplicationContext(), ConstantValue.SIM_NUMBER, "");
        if (!TextUtils.isEmpty(simSerialNumbers)){
            Intent intent=new Intent(getApplicationContext(),Setup3Activity.class);
            startActivity(intent);
            finish();
            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else {
            ToastUtils.show(this,"请绑定SIM卡");
        }

    }

    /**
     *
     */
    private void initUI() {
        siv_sim_bound = findViewById(R.id.siv_sim_bound);
        //1.回显
        String sim_number = SharedPerferenceUtils.getString(this, ConstantValue.SIM_NUMBER, "");
        //2.判断序列卡号是否为""
        if (TextUtils.isEmpty(sim_number)) {
            siv_sim_bound.setCheck(false);
        } else {
            siv_sim_bound.setCheck(true);
        }
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3.获取原有状态
                boolean checks = siv_sim_bound.isChecks();
                //4.将原有状态取反，
                //5.状态设置给当前条目
                siv_sim_bound.setCheck(!checks);
                if (!checks) {
                    //6.储存序列卡号
                    //6.1获取sim卡序列号TelephoneManager
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    //6.2获取sim卡的序列卡号

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    String simSerialNumber = manager.getSimSerialNumber();
                    Toast.makeText(Setup2Activity.this, simSerialNumber, Toast.LENGTH_SHORT).show();
                    //6.3储存
                    SharedPerferenceUtils.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,simSerialNumber);
                }else {
             SharedPerferenceUtils.remover(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }
            }
        });
    }

}
