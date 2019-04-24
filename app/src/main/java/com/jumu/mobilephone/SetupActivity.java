package com.jumu.mobilephone;

import android.content.Intent;
import android.os.Bundle;

import com.jumu.mobilephone.base.BaseSetupActivity;

public class SetupActivity extends BaseSetupActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity);

    }

    @Override
    protected void showPrePage() {

    }

    @Override
    protected void showNextPage() {
        Intent intent=new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();

        //开启平移动画
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

}
