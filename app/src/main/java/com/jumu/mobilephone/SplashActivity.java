package com.jumu.mobilephone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jumu.mobilephone.com.mobile.utils.ConstantValue;
import com.jumu.mobilephone.com.mobile.utils.SharedPerferenceUtils;
import com.jumu.mobilephone.com.mobile.utils.StreamUtils;
import com.jumu.mobilephone.com.mobile.utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {
    TextView version_name;
    RelativeLayout rl_root;
    int mLocalVersionCode;
    private static final String TAG = "SplashActivity";

    final int UPDATE_VERSION = 100;
    final int ENTER_HOME = 101;
    final int URL_ERROR = 102;
    final int IO_ERROR = 103;
    final int JSON_ERROR = 104;
    String versionMessage;
    String downLoadeUrl;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    showUpDataDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtils.show(SplashActivity.this, "url连接异常!");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtils.show(SplashActivity.this, "IO流异常!");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtils.show(SplashActivity.this, "json解析异常!");
                    enterHome();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initUI();  //初始化UI
        initData();  //初始化数据
        initAnimation();  //设置动画效果
        initDB();  //初始化数据库
    }

    private void initDB() {
       //1.归属地数据库拷贝过程
       initAddressDB("address.db");
    }

    private void initAddressDB(String dbName) {
        InputStream stream = null;
        FileOutputStream fileOutputStream = null;
        //1.在files文件夹下创建同名dbName数据库文件过程
        File filesDir = getFilesDir();
        /* getCacheDir();
         Environment.getExternalStorageDirectory().getAbsolutePath();*/
        File file = new File(filesDir, dbName);
        if (file.exists()){ //exists()指文件是否存在
            return;
        }
       //2.输入流读取第三方资产目录下的文件
        try {
             stream = getAssets().open("address.db");
            //3.将读取的内容写入到指定文件夹的文件中去
             fileOutputStream = new FileOutputStream(file);
            //4,每次读取内容的大小
            byte[] bytes = new byte[1024];
            int temp=-1;
            while ((temp = stream.read(bytes) )!=-1){
                     fileOutputStream.write(bytes,0,temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (stream!=null && fileOutputStream!=null) {
                try {
                    stream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               
            }
        }

    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        rl_root.setAnimation(alphaAnimation);
    }

    private void initUI() {
        version_name = findViewById(R.id.tv_version_name);
        rl_root = findViewById(R.id.rl_root);
    }

    private void enterHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //下载新版apk
    private void showUpDataDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("版本更新");
        builder.setMessage(versionMessage);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载APK，url地址，download="";
                downloadAPK();
            }
        });
        builder.setNeutralButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框后跳转到主界面
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //点击回退时监听方法
                enterHome();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void downloadAPK() {
        //APK下载链接地址，防止apk的所在路径
        //1.判断sd卡是否可用，是否挂在上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2.获取SD卡路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mobilersafe.apk";
            //3.发送请求，获取apk，放到指定路径
            HttpUtils httpUtils = new HttpUtils();
            //4.发送请求，传递参数（下载地址，下载应用放置位置）
            httpUtils.download(downLoadeUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功(下载过后的放置在sd卡中的apk)
                    Log.d(TAG, "onSuccess: 下载成功");
                    File file = responseInfo.result;
                    //提示用户安装
                    installAPK(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                    Log.d(TAG, "onFailure: 下载失败");
                    enterHome();
                }

                //刚刚开始下载
                @Override
                public void onStart() {
                    super.onStart();
                    Log.d(TAG, "onStart: 开始下载");
                }
                //下载过程中的方法

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.d(TAG, "onLoading: 下载中");
                }

            });
        }

    }

    private void installAPK(File file) {
        //系统应用界面，源码，安装APK入口
        Intent install = new Intent();
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setAction(android.content.Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivityForResult(install, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initData() {
        //1.应用版本名称
        version_name.setText("版本名称:" + getVersionName());
        //检测（本地版本和服务器版本比对）是否有更新，如果有跟新，提示用户下载
        //2.获取本地版本号
        mLocalVersionCode = getVersionCode();
        Log.d(TAG, "getVersionCode: " + mLocalVersionCode);
        //3.获取服务器版本号（客户端发请求，服务端响应（json，xml））
        //http:www.pingxxx.updatejson.74?key=value  返回200表示成功，以流的形式读取数据
        //json中内容包含:
        /*更新版本的版本名称
          更新版本描述
          服务器的版本号
          新版本apk下载地址*/

        if (SharedPerferenceUtils.getBoolen(this, ConstantValue.OPEN_UPDATE, false)) {
            chcekAPK();
        } else {
            //为flash直接进入主界面
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
        }
    }

    private void chcekAPK() {
        new Thread() {
            @Override
            public void run() {
                //发送请求数据，参数则为请求json链接地址

                Message message = Message.obtain();
                Long startTime = System.currentTimeMillis();
                try {
                    //  1.封装url地址
                    URL url = new URL("http://10.0.2.2:8080/update1.json");
                    //2.开启一个连接
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    //3.设置常见请求参数
                    //连接超时
                    urlConnection.setConnectTimeout(2000);
                    //读取超时
                    urlConnection.setReadTimeout(2000);
                    //请求方式
                    urlConnection.setRequestMethod("GET");
                    //4.获取请求成功的响应码
                    if (urlConnection.getResponseCode() == 200) {
                        //5.以流的形式，将数据取下来
                        InputStream inputStream = urlConnection.getInputStream();
                        //6.将流转换成字符串(工具类实现)

                        String json = StreamUtils.streamToString(inputStream);
                        Log.d(TAG, "run: " + json);

                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        String versionCode = jsonObject.getString("versionCode");
                        versionMessage = jsonObject.getString("versionMessage");
                        downLoadeUrl = jsonObject.getString("downLoadeUrl");
                        Log.d(TAG, "+++++++run:++++++ " + versionName);
                        Log.d(TAG, "+++++++run:++++++ " + versionCode);
                        Log.d(TAG, "+++++++run:++++++ " + versionMessage);
                        Log.d(TAG, "+++++++run:++++++ " + downLoadeUrl);

                        //8.比对版本号（服务器版本号》本地版本号，弹出对话框）
                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            //弹出对话框
                            message.what = UPDATE_VERSION;
                        } else {
                            //进入程序主页面
                            message.what = ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = JSON_ERROR;
                } finally {
                    //读取数据结束时间
                    Long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 3000) {
                        try {
                            Thread.sleep(3000 - (endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(message);
                }
            }
        }.start();

    }

    public String getVersionName() {
        //1.包管理者对象
        PackageManager pm = getPackageManager();

        //2.从包的管理者对象中获取指定信息，(版本信息，版本号)，传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //3,获取版本名称
            //    Log.d(TAG, "getVersionName: "+packageInfo.versionName);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
