package com.example.administrator.securityguards.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2018/2/1/001.
 */

public class StringUtil {

    /**
     * 流转换成字符串
     * @param is	流对象
     * @return		流转换成的字符串	返回null代表异常
     */
    public static String stream2String(InputStream is) {
        BufferedReader br=new BufferedReader(new InputStreamReader(is));

        StringBuffer sb=new StringBuffer();
        String s=null;
        try {
            while((s=br.readLine())!=null){
                sb.append(s);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();

        }finally {
            try {
                is.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

}
