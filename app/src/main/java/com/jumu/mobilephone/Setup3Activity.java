package com.jumu.mobilephone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jumu.mobilephone.base.BaseSetupActivity;
import com.jumu.mobilephone.com.mobile.activity.ContextListActivity;
import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;
import com.jumu.mobilephone.com.mobile.utils.ToastUtils;

public class Setup3Activity extends BaseSetupActivity {
    EditText et_phone_number;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup3_activity);

        initUI();
    }

    @Override
    protected void showPrePage() {
        Intent intent=new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();

        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        String numbers = et_phone_number.getText().toString();
        if (TextUtils.isEmpty(numbers)){
            ToastUtils.show(this,"请填写电话号码");
        }else {
            Intent intent=new Intent(this,Setup4Activity.class);
            startActivity(intent);
            finish();
            SharedPerferenceUtils.putString(getApplicationContext(),ConstantValue.CONTACT_NUMBER,numbers);
            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }
    }

    private void initUI() {
         et_phone_number = findViewById(R.id.et_phone_number);
        Button bt_select_number = findViewById(R.id.bt_select_number);
     et_phone_number.setText( SharedPerferenceUtils.getString(getApplicationContext(),ConstantValue.CONTACT_NUMBER,""));
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContextListActivity.class);
                    startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回到当前界面的时候，接收结果的方法
        if (data!=null) {
            String number = data.getStringExtra("number");
            et_phone_number.setText(number);

            SharedPerferenceUtils.putString(getApplicationContext(),ConstantValue.CONTACT_NUMBER,number);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
