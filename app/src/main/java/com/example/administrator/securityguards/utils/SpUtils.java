package com.example.administrator.securityguards.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/2/3/003.
 */

public class SpUtils {

    private static SharedPreferences sp;

    //写
    public static void putCheck_State(Context context,String key,boolean value){
        //第一个参数是储存文件名称，第二个是读写模式
        if (sp==null){
            //创建sp；
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        //一定要提交
        sp.edit().putBoolean(key,value).commit();

    }

    //写
    public static boolean getCheck_State(Context context,String key,boolean defaultValue){
        //第一个参数是储存文件名称，第二个是读写模式
        if (sp==null){
            //创建sp；
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        //如果返回有的话就返回否则返回默认值
        return sp.getBoolean(key,defaultValue);

    }


    //写
    public static void putSafe_PD(Context context,String key,String value){
        //第一个参数是储存文件名称，第二个是读写模式
        if (sp==null){
            //创建sp；
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        //一定要提交
        sp.edit().putString(key,value).commit();

    }

    //读
    public static String  getSafe_PD(Context context,String key,String defaultValue){
        //第一个参数是储存文件名称，第二个是读写模式
        if (sp==null){
            //创建sp；
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        //如果返回有的话就返回否则返回默认值
        return sp.getString(key,defaultValue);

    }

    public static void putInt(Context context,String key,int value){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,value).commit();
    }

    public static int getInt(Context context,String key,int defaultValue){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getInt(key,defaultValue);
    }
    //删除节点
    public static void remove(Context context,String key){
        //第一个参数是储存文件名称，第二个是读写模式
        if (sp==null){
            //创建sp；
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        //一定要提交
        sp.edit().remove(key).commit();
    }
}
