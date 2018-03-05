package com.example.administrator.securityguards.enginer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.securityguards.utils.ToastUtil;

/**
 * Created by Administrator on 2018/2/9/009.
 */

public class AddressDao {
    //数据库访问路径
    public static String path="data/data/com.example.administrator.securityguards/files/address.db";


    //开启数据库 ，进行访问
    public static String getAddress(Context context,String phone){

        String location="未知号码";
       // Log.e("AddressDao"," "+phone);
        String regex="^1[3-8]\\d{9}";
        if (phone.matches(regex)){
            SQLiteDatabase db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
            Cursor cursor=db.query("data1",new String[]{
                    "outkey"
            },"id = ?",new String[]{
                    phone.substring(0,7)
            },null,null,"");

            if(cursor.moveToNext()){
                String outkey=cursor.getString(0);
                Log.e("AddressDao"," "+outkey);
                Cursor index=db.query("data2",new String[]{"location"},"id=?",new String[]{outkey},null,null,null);

                if (index.moveToNext()){
                    location=index.getString(0);
                    Log.e("AddressDao"," "+location);
                }
            }

            return location;
        }else{
            //ToastUtil.show(context,"不是电话号码！");
            int len=phone.length();
            switch (len){
                case 3:
                    location = "报警电话";
                    break;
                case 4:
                    location ="模拟器";
                    break;
                case 5:
                    location ="企业服务电话";
                    break;
                case 7:
                    ;
                case 8:
                    location = "本地电话";
                    break;
                default:
                    location = "未知号码";
                    break;
            }
            return location;
        }

    }

}
