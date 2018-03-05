package com.example.administrator.securityguards.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/2/1/001.
 */

public class ToastUtil {

       public static void show(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
