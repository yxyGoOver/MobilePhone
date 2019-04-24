package com.jumu.mobilephone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;

public class TextActivity extends Activity {
    private static final String TAG="TextActivity";
   private TextView tv_setup;
  private   TextView tv_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean up_over = SharedPerferenceUtils.getBoolen(this, ConstantValue.SET_UP_OVER, false);
        if (up_over) {
            //密码输入成功，四个导航界面输入完成，
            setContentView(R.layout.activity_text);
            inUI();
        } else {
            //密码输入成功，四个导航界面没有设置完成
            Intent intent = new Intent(this, SetupActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void inUI() {
         tv_phone = findViewById(R.id.tv_phone);
        tv_setup = findViewById(R.id.tv_setup);

        String phoneNumber = SharedPerferenceUtils.getString(getApplicationContext(), ConstantValue.CONTACT_NUMBER, "");

            tv_phone.setText(phoneNumber);


          tv_setup.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(TextActivity.this, SetupActivity.class);
                   startActivity(intent);
                   finish();
              }
          });
    }

}
