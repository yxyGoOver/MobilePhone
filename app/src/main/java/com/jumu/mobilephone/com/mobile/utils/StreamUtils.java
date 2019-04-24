package com.jumu.mobilephone.com.mobile.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public  class StreamUtils {

    public static String streamToString(InputStream is)  {
        //1.在读取的过程中，将读取的内容存储至缓存中，然后一次性转换成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //2.读流操作，读到没有为止(循环)
        byte[] bytes = new byte[1024];
        //3.记录读取数据的临时变量
        int temp = -1;
        try {
            while ((temp = is.read(bytes)) != -1) {
                bos.write(bytes,0,temp);
            }
            return bos.toString();
        }catch (Exception e){
            try {
                is.close();
                bos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}