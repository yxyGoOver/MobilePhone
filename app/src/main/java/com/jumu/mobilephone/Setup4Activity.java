package com.jumu.mobilephone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jumu.mobilephone.base.BaseSetupActivity;
import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;
import com.jumu.mobilephone.com.mobile.utils.ToastUtils;

public class Setup4Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup4_activity);

        initUI();
    }

    @Override
    protected void showPrePage() {
        Intent intent=new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        boolean boolen = SharedPerferenceUtils.getBoolen(getApplicationContext(), ConstantValue.OPEN_SECURITY, false);
        if (boolen){
            Intent intent=new Intent(this,TextActivity.class);
            startActivity(intent);
            finish();
            SharedPerferenceUtils.putBoolen(this,ConstantValue.SET_UP_OVER,true);
            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else {
            ToastUtils.show(this,"请开启安全设置");
        }

    }

    private void initUI() {
        final CheckBox setup4_cb_box = findViewById(R.id.setup4_cb_box);

        boolean open_security = SharedPerferenceUtils.getBoolen(getApplicationContext(), ConstantValue.OPEN_SECURITY, false);
        setup4_cb_box.setChecked(open_security);
        if (open_security){
            setup4_cb_box.setText("安全设置已开启");
        }else {
            setup4_cb_box.setText("安全设置已关闭");
        }
        setup4_cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPerferenceUtils.putBoolen(getApplicationContext(),ConstantValue.OPEN_SECURITY,isChecked);
                if (isChecked){
                    setup4_cb_box.setText("安全设置已开启");
                }else {
                    setup4_cb_box.setText("安全设置已关闭");
                }
            }
        });

    }

}
