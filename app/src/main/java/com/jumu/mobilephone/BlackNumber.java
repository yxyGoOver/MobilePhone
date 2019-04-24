package com.jumu.mobilephone;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jumu.mobilephone.com.mobile.utils.ToastUtils;
import com.jumu.mobilephone.databasesql.BlackNumberOpenHelper;
import com.jumu.mobilephone.databasesql.dao.BlackNumberDao;
import com.jumu.mobilephone.databasesql.doman.BlackNumberInfo;

import java.util.List;
import java.util.Random;

public class BlackNumber extends Activity {
    private static final String TAG="BlackNumber";
    private  ListView lv_blackNumberList;
    private  BlackNumberAdapter blackNumberAdapter;
    private    Button bt_add;
    private      BlackNumberDao mDao;
   private  List<BlackNumberInfo> mDaoAll;
   private int mode=1;
   private boolean isLoad=false;
   private int count;
 //  private     ImageView iv_delete;

    private Handler mHandler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
          //4.告知listview可以去设置数据适配器
           if (blackNumberAdapter == null) {
               blackNumberAdapter = new BlackNumberAdapter();
               lv_blackNumberList.setAdapter(blackNumberAdapter);
           }else {
               blackNumberAdapter.notifyDataSetChanged();
           }
           Toast.makeText(BlackNumber.this, "++++++", Toast.LENGTH_SHORT).show();
       }
   };
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_number_activity);
        initUI();
        initData();
      //   Toast.makeText(this, String.valueOf(mDao.getCount()), Toast.LENGTH_SHORT).show();
    }

     //nininininiini

    private void initUI() {
        bt_add = findViewById(R.id.bt_add);
        lv_blackNumberList = findViewById(R.id.lv_backNumberList);
        lv_blackNumberList.setAdapter(blackNumberAdapter);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
       lv_blackNumberList.setOnScrollListener(new AbsListView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(AbsListView absListView, int i) {
             //  AbsListView.OnScrollListener.SCROLL_STATE_FLING  飞速滚动状态
             //  AbsListView.OnScrollListener.SCROLL_STATE_IDLE  空闲状态
             //  AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL   拿手去触摸着滚动状态
               if (mDaoAll != null) {
                   if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                           && lv_blackNumberList.getLastVisiblePosition() >= mDaoAll.size() - 1
                           && !isLoad) { //防止重复加载的变量
                       //加载下一页数据
                       if (count>mDaoAll.size()){
                           new Thread() {
                           @Override
                           public void run() {
                               mDao = BlackNumberDao.getInstance(getApplicationContext());
                               mDao.Fenyefind(mDaoAll.size());
                               mDaoAll.addAll(mDao.Fenyefind(mDaoAll.size()));
                               mHandler.sendEmptyMessage(0);
                           }
                       }.start();
                   }
               }
               }
           }

           @Override
           public void onScroll(AbsListView absListView, int i, int i1, int i2) {

           }
       });
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //自己定义绘画的view对象
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getApplicationContext(), R.layout.dialog_blacknumber, null);
         dialog.setView(view,0,0,0,0);
        final EditText et_phone_dialog = view.findViewById(R.id.et_phone);
        RadioGroup rg_dialog = view.findViewById(R.id.rg_dialog);
        Button btn_ok_dialog = view.findViewById(R.id.btn_ok);
        Button btn_dis_dialog = view.findViewById(R.id.btn_dis);

         rg_dialog.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 switch (checkedId){
                     case R.id.rb_sms:
                            mode=1;
                         break;
                     case R.id.rb_phone:
                         mode=2;
                         break;
                     case R.id.rb_all:
                         mode=3;
                         break;
                 }
             }
         });
            btn_ok_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.获取输入框中的号码
                    String phone = et_phone_dialog.getText().toString();
                 if (TextUtils.isEmpty(phone)){
                     ToastUtils.show(getApplicationContext(),"请输入要拦截的号码");
                 }else {
                    //2.数据库插入当前拦截的电话号码
                     for (int i = 0; i <10 ; i++) {
                         mDao.insert(phone+i,String.valueOf(1+new Random().nextInt(3)));
                     }
                     Log.d(TAG, "onClick: 2");
                    // mDao.insert(phone,mode+"");
                     //3.让数据库和集合保持同步（1.数据库中数据重新读一遍，2.手动向集合中添加一个对象）
                     BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                     blackNumberInfo.phone=phone;
                     blackNumberInfo.mode=mode+"";
                     //4,将对象插入到集合的最顶部
                     mDaoAll.add(0,blackNumberInfo);
                     //5,通知数据适配器刷新（数据适配器中的数据有改变了）
              if (blackNumberAdapter != null){
                  blackNumberAdapter.notifyDataSetChanged();
              }
              //隐藏对话框
                     dialog.dismiss();
                 }
                }
            });
            btn_dis_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        dialog.show();
    }

    private void initData() {
        //获取数据库中所有号码
        new Thread(){
            @Override
            public void run() {
                //1.获取操作数据库的黑名单对象
                mDao = BlackNumberDao.getInstance(getApplicationContext());
                //2.查询所有数据
                mDaoAll = mDao.Fenyefind(0);
                count=mDao.getCount();
               //3.通过消息机制告诉主线程可以去使用包含数据的集合
               mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private class BlackNumberAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDaoAll.size();
        }

        @Override
        public Object getItem(int position) {
            return mDaoAll.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
         //   TextView tv_phone = null,tv_mode = null;
         //   View view = null;
            //1.复用convertView
            //复用viewholder步骤一
            ViewHolder viewHolder=null;
            if (convertView == null){
                convertView = View.inflate(getApplicationContext(), R.layout.listview_blacknumber_view, null);
                //服用viewHolder步骤三
                 viewHolder = new ViewHolder();
                //服用viewHolder步骤四
                viewHolder.vh_phone= convertView.findViewById(R.id.tv_phone);
                 viewHolder.vh_mode = convertView.findViewById(R.id.tv_mode);
               viewHolder.vh_delete=convertView.findViewById(R.id.iv_delete);
                //服用viewHolder步骤五
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.vh_phone.setText(mDaoAll.get(position).phone);
            final int mode = Integer.parseInt(mDaoAll.get(position).mode);
            switch (mode){
                case 1:
                    viewHolder.vh_mode.setText("拦截短信");
                    break;
                case 2:
                    viewHolder.vh_mode.setText("拦截电话");
                    break;
                case 3:
                    viewHolder.vh_mode.setText("拦截所有");
            }
             viewHolder.vh_delete.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     //1.数据库删除号码
                     mDao.delete(mDaoAll.get(position).phone);
                     //2.集合中的删除，通知数据适配器刷新
                     mDaoAll.remove(mDaoAll.get(position));
                     //3.通知数据适配器集合有改变，刷新
                    /* if (blackNumberAdapter != null){
                         blackNumberAdapter.notifyDataSetChanged();
                     }*/
                     blackNumberAdapter.notifyDataSetChanged();
                 }
             });
            return convertView;
        }

    }
    //服用viewHolder步骤二
  static   class ViewHolder{
        TextView vh_phone,vh_mode;
        ImageView vh_delete;
    }
}
