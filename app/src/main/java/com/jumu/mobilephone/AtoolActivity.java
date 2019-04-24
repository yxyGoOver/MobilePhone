package com.jumu.mobilephone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jumu.mobilephone.com.mobile.activity.QueryAddress;

public class AtoolActivity extends Activity {
    private TextView tv_query_phone_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools_activity);


        SelectorPhoneAddress();
    }

    private void SelectorPhoneAddress() {
        tv_query_phone_address = findViewById(R.id.tv_query_phone_address);

        tv_query_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),QueryAddress.class));

            }
        });
    }


}
