package com.jumu.mobilephone.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class AddressDao{
    private static final String TAG="AddressDao";
    private static String address="未知号码";
    //指定访问数据库的路径
    public static String path="data/data/com.jumu.mobilephone/files/address.db";

    //传递一个电话号码，开启数据库连接，进行访问，返回一个归属地
    public static String getAddress(String phone){
        address="未知号码";
        //正则表达式，匹配电话号码
           String regularExpressionPhone="[1][3-8]\\d{9}";   //[1][34578]\\d{9}
     //   phone = phone.substring(0, 7);
        //2.开启数据库连接（只读的形式打开），进行访问
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        if (phone.matches(regularExpressionPhone)) {
            //3.数据库查询
            Cursor cursor = sqLiteDatabase.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
            //4.查到即可
            if (cursor.moveToNext()) {
                String outkey = cursor.getString(0);
                Log.d(TAG, "outkey: " + outkey);
                //通过data1查询到的结果，作为外键查询data2
                Cursor cursor1 = sqLiteDatabase.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
                if (cursor1.moveToNext()) {
                    //6,获取查询到的电话归属地
                          address = cursor1.getString(0);
                    Log.d(TAG, "getAddress: " + address);
                }else {
                    address="未知号码";
                }
            }
        }else {
            int length = phone.length();
            switch (length){
                case 3:
                  address="报警电话";
                    break;
                case 4:
                    address="模拟器号码";
                    break;
                case 5:
                    address="服务电话";
                    break;
                case 6:
                    break;
                case 7:
                    address="本地电话";
                    break;
                case 8:
                    address="本地电话";
                    break;
                case 9:

                    break;
                case 10:

                    break;
                case 11:
                    String substring = phone.substring(1, 3);
                    Cursor data2 = sqLiteDatabase.query("data2", new String[]{"location"}, "id = ?", new String[]{substring}, null, null, null);
                     if (data2.moveToNext()){
                         address = data2.getString(0);
                     }else {
                         address="未知号码";
                     }
                    break;
                case 12:
                    String substring2 = phone.substring(1, 4);
                    Cursor cursor2 = sqLiteDatabase.query("data2", new String[]{"location"}, "id = ?", new String[]{substring2}, null, null, null);
                    if (cursor2.moveToNext()){
                        address = cursor2.getString(0);
                    }else {
                        address="未知号码";
                    }
                    break;
            }
        }
         return address;
    }
}
