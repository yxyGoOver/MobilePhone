package com.jumu.mobilephone.databasesql.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jumu.mobilephone.databasesql.BlackNumberOpenHelper;
import com.jumu.mobilephone.databasesql.doman.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

public class BlackNumberDao  {
    private BlackNumberOpenHelper blackNumberOpenHelper;

    //blacknumberDaodao单例模式
    //1.私有化够着方法
    private BlackNumberDao(Context context){
         //创建数据库以及其表结构
        blackNumberOpenHelper =new BlackNumberOpenHelper(context);
    }
    //2.声明一个当前类的对象
    private static BlackNumberDao blackNumberDao=null;
    //3.提供一个静态方法,如果当前类的对象为空，创建一个新的
    public static BlackNumberDao getInstance(Context context){
        if (blackNumberDao==null){
             blackNumberDao = new BlackNumberDao(context);
        }
        return blackNumberDao;
    }


    /**增加一个条目
     * @param phone
     * @param mode
     */
    public void insert(String phone,String mode){
        //开启数据库做写入操作
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone",phone);
        contentValues.put("mode",mode);
        writableDatabase.insert("blacknumber",null,contentValues);
        writableDatabase.close();
    }

    /**从数据库中删除电话号码的方法
     * @param phone
     */
  public void delete(String phone){
      SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();

      writableDatabase.delete("blacknumber","phone = ?",new String[]{phone});

      writableDatabase.close();
  }
  public void upDate(String phone,String mode){
      SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("mode",mode);

      writableDatabase.update("blacknumber",contentValues,"phone = ?",new String[]{phone});

      writableDatabase.close();
  }

    /**查询的结果
     * @return
     */
  public List<BlackNumberInfo> findAll(){
      SQLiteDatabase readableDatabase = blackNumberOpenHelper.getWritableDatabase();

      Cursor query = readableDatabase.query("blacknumber", new String[]{"phone", "mode"}, null, null, null, null, "_id desc");
      ArrayList<BlackNumberInfo> list = new ArrayList<>();
      while (query.moveToNext()){
         BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
         blackNumberInfo.phone = query.getString(0);
         blackNumberInfo.mode = query.getString(1);
                list.add(blackNumberInfo);
      }
      query.close();
      readableDatabase.close();
      return list;
  }

    /**每次查询20条数据
     * @param index
     */
  public List<BlackNumberInfo> Fenyefind(int index){
      SQLiteDatabase readableDatabase = blackNumberOpenHelper.getWritableDatabase();

      Cursor query = readableDatabase.rawQuery("select phone,mode from blacknumber order by _id desc limit ?,10;",
              new String[]{index +""});
      ArrayList<BlackNumberInfo> list = new ArrayList<>();
      while (query.moveToNext()){
          BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
          blackNumberInfo.phone = query.getString(0);
          blackNumberInfo.mode = query.getString(1);
          list.add(blackNumberInfo);
      }
      query.close();
      readableDatabase.close();
      return list;
  }
  public int getCount(){
      SQLiteDatabase writableDatabase = blackNumberOpenHelper.getReadableDatabase();
      Cursor cursor = writableDatabase.rawQuery("select count(*) from blacknumber;", null);
      int cursorInt = 0;
      if (cursor.moveToNext()){
          cursorInt = cursor.getInt(0);
      }
      cursor.close();
      writableDatabase.close();
      return cursorInt;
  }
  public int getMode(String phone){
      SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
      int mode=0;
      Cursor query = writableDatabase.query("blacknumber", new String[]{"mode"}, "phone = ?", new String[]{phone}, null, null, null);
       if (query.moveToNext()){
           mode = query.getInt(0);
       }
       query.close();
       writableDatabase.close();
      return mode;
  }
}
