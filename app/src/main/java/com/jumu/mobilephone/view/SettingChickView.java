package com.jumu.mobilephone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jumu.mobilephone.R;

public class SettingChickView extends RelativeLayout {
   private  TextView tv_title;
   private TextView tv_des;
    public SettingChickView(Context context) {
        this(context,null);
    }

    public SettingChickView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingChickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_chick_view,this);

        tv_title = findViewById(R.id.tv_title);
        tv_des = findViewById(R.id.tv_des);

    }
         public void  setTitle(String title){
                    tv_title.setText(title);
         }
         public void  setDes(String des){
               tv_des.setText(des);
         }
}
