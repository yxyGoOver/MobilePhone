package com.jumu.mobilephone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jumu.mobilephone.R;



public class SettingView extends RelativeLayout {
   private static final String TAG="SettingView";
   private static final String NAMESPACE="http://schemas.android.com/apk/res/com.jumu.mobilephone";
    CheckBox cb_box;
    TextView tv_des;
    String destitle,desoff,deson;
    public SettingView(Context context) {
        this(context,null);
    }

    public SettingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.home_item_setting,this);

        TextView auto_update = findViewById(R.id.auto_update);
         tv_des = findViewById(R.id.tv_des);
         cb_box = findViewById(R.id.cb_box);

          initAttributeSet(attrs);
         destitle = attrs.getAttributeValue(NAMESPACE, "destitle");
         desoff = attrs.getAttributeValue(NAMESPACE, "desoff");
         deson = attrs.getAttributeValue(NAMESPACE, "deson");
        auto_update.setText(destitle);
    }

    private void initAttributeSet(AttributeSet attributeSet) {
        Log.d(TAG, "当前节点的属性个数: "+attributeSet.getAttributeCount());
        for (int i = 0; i <attributeSet.getAttributeCount(); i++) {
            Log.d(TAG, "属性值: "+attributeSet.getAttributeValue(i));
            Log.d(TAG, "属性名称: "+attributeSet.getAttributeName(i));
        }
    }


    public boolean isChecks(){
        return cb_box.isChecked();
  }
  public void setCheck(boolean ischeck){
         cb_box.setChecked(ischeck);
         if (ischeck){
             tv_des.setText(desoff);
         }else {
             tv_des.setText(deson);
         }
  }
}
