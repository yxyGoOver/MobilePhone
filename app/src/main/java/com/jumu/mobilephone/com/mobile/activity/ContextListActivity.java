package com.jumu.mobilephone.com.mobile.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jumu.mobilephone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextListActivity extends Activity {
    private static final String TAG = "ContextListActivity";
     ListView list_contact;
   List<Map<String,String>> ContactsList=new ArrayList<Map<String,String>>();
    Adapter adapter;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter= new ContactsAdappter();
          list_contact.setAdapter((ListAdapter) adapter);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contexlist_activity);

        initUI();
        initData();
    }
     class ContactsAdappter extends BaseAdapter{

         @Override
         public int getCount() {
             return ContactsList.size();
         }

         @Override
         public HashMap<String,String> getItem(int position) {
             return (HashMap<String, String>) ContactsList.get(position);
         }

         @Override
         public long getItemId(int position) {
             return position;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             if (convertView==null){
                 convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.contacts_list_item, null);
                 TextView et_contacts_name = convertView.findViewById(R.id.et_contacts_name);
                 TextView et_contacts_phone = convertView.findViewById(R.id.et_contacts_phone);
                 et_contacts_name.setText(getItem(position).get("name"));
                 et_contacts_phone.setText(getItem(position).get("number"));
             }
             return convertView;
         }
     }
    private void initUI() {
        list_contact = findViewById(R.id.list_contact);

        list_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     if (adapter!=null){
                         HashMap<String,String> map  = (HashMap<String, String>) adapter.getItem(position);
                         String number = map.get("number");
                         Intent intent = new Intent();
                         intent.putExtra("number",number);
                         setResult(0,intent);

                         finish();
                     }
            }
        });
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ContentResolver contentResolver = getContentResolver();
                Cursor query = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"}, null, null, null);
                //清除列表数据
                ContactsList.clear();
                while (query.moveToNext()) {
                    String id = query.getString(0);
                    Log.d(TAG, "id------" + id);

                    Cursor cursor = getContentResolver().query(Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1","mimetype"}, "raw_contact_id = ?", new String[]{id}, null);
                    HashMap<String, String> map = new HashMap<String, String>();
                    while (cursor.moveToNext()){
                            String data = cursor.getString(0);
                          Log.d(TAG, "data-------: "+data);
                             String mimetype = cursor.getString(1);
                          Log.d(TAG, "mimetype-----: "+mimetype);
                          if (mimetype.equals("vnd.android.cursor.item/name")){
                              if (!TextUtils.isEmpty(data)){
                              map.put("name",data);
                              }
                          }else if (mimetype.equals("vnd.android.cursor.item/phone_v2")){
                              if (!TextUtils.isEmpty(data)){
                                  map.put("number",data);
                              }
                          }
                      }
                      cursor.close();
                    ContactsList.add(map);
                }
                query.close();

                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
