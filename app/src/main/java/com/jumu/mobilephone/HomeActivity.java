package com.jumu.mobilephone;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jumu.mobilephone.com.mobile.activity.SettingActivity;
import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.Md5Utils;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;
import com.jumu.mobilephone.com.mobile.utils.ToastUtils;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG="HomeActivity";
    GridView gv_home;
    String[]  titleStr;
    int[] icons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();  //初始化控件
        initData(); //初始化数据


    }

    private void init() {
        gv_home=findViewById(R.id.gv_home);
    }
    private void initData() {

        titleStr=new String[]{"手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
        icons=new int[]{R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
                R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings};
        gv_home.setAdapter(new GridViewAdapter());
          gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  switch (position){
                      case 0:
                          showDialog();
                          break;
                      case 1:
                          //跳转到通讯卫士模块
                          startActivity(new Intent(getApplicationContext(),BlackNumber.class));
                          break;
                      case 2:
                          break;
                      case 3:
                          break;
                      case 4:
                          break;
                      case 5:
                          break;
                      case 6:

                          break;
                      case 7:
                          //跳转到高级工具功能列表界面
                          startActivity(new Intent(getApplicationContext(),AtoolActivity.class));
                          break;
                      case 8:
                          Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                     startActivity(intent);
                          break;

                  }
              }
          });
    }
      class GridViewAdapter extends BaseAdapter {

          @Override
          public int getCount() {
              return titleStr.length;
          }

          @Override
          public Object getItem(int position) {
              return titleStr[position];
          }

          @Override
          public long getItemId(int position) {
              return position;
          }

          @Override
          public View getView(int position, View convertView, ViewGroup parent) {
                 View view=View.inflate(getApplicationContext(),R.layout.gridview_item,null);
                  TextView tv_title=view.findViewById(R.id.tv_title);
                  ImageView icon=view.findViewById(R.id.icon);

                  tv_title.setText(titleStr[position]);
                icon.setBackgroundResource(icons[position]);

              return view;
          }
      }

    private void showDialog() {
        //判断本地是否有存储密码（sp 字符串）
        String pwd = SharedPerferenceUtils.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PWD, "");
      if (TextUtils.isEmpty(pwd)){
         //1.初始设置密码对话框
            showSetPwdDialog();
      }else {
          //2.确认密码对话框
          showConfirmPwdDialog();
      }
    }

    private void showConfirmPwdDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final View view=View.inflate(getApplicationContext(),R.layout.dialog_confirm_pwd,null);
        dialog.setView(view);
        dialog.show();

        Button dialog_btn_ok2 = view.findViewById(R.id.dialog_btn_ok2);
        Button dialog_btn_deselect2 = view.findViewById(R.id.dialog_btn_deselect2);

        dialog_btn_ok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView confirm_pwd2 = view.findViewById(R.id.confirm_pwd2);
                String confirmPwd2 = confirm_pwd2.getText().toString();
              if (!TextUtils.isEmpty(confirmPwd2)) {
                  String sharedPwd = SharedPerferenceUtils.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PWD, "");
                  if (sharedPwd.equals(Md5Utils.encoder(confirmPwd2))) {
                      Toast.makeText(HomeActivity.this, "确认密码:" + Md5Utils.encoder(confirmPwd2), Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(getApplicationContext(), TextActivity.class);
                      startActivity(intent);
                      dialog.dismiss();
                  } else {
                      ToastUtils.show(getApplicationContext(), "密码不正确!");
                  }
              }else {
                  ToastUtils.show(getApplicationContext(),"密码不能为空");
              }
            }
        });
        dialog_btn_deselect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showSetPwdDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final View view=View.inflate(this,R.layout.dialog_set_pwd,null);
        dialog.setView(view);
        dialog.show();

        Button dialog_btn_ok = view.findViewById(R.id.dialog_btn_ok);
        Button dialog_btn_deselect = view.findViewById(R.id.dialog_btn_deselect);

        dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText set_pwd = view.findViewById(R.id.set_pwd);
                EditText  confirm_pwd = view.findViewById(R.id.confirm_pwd);
                 String pwd = set_pwd.getText().toString();
                 String confirmPwd = confirm_pwd.getText().toString();
                 if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirmPwd)){
                     if (pwd.equals(confirmPwd)){

                         Intent   intent=new Intent(getApplicationContext(),TextActivity.class);
                         startActivity(intent);
                         dialog.dismiss();

                         SharedPerferenceUtils.putString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PWD,Md5Utils.encoder(pwd));
                         Toast.makeText(HomeActivity.this, Md5Utils.encoder(pwd), Toast.LENGTH_SHORT).show();
                     }else {
                         ToastUtils.show(getApplicationContext(),"确认密码输入错误！");
                     }
                 }else {
                     ToastUtils.show(getApplicationContext(),"请输入密码或确认密码");
                 }
            }
        });
        dialog_btn_deselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        });
    }


}
