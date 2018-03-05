package com.example.administrator.securityguards.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2018/2/4/004.
 */

public class MD5Util {

    public static String encoder(String psd){

        try {
            // 给密码加盐
            psd+="SecurityGuards";
            //1.指定加密算法类型
            MessageDigest digest=MessageDigest.getInstance("MD5");
            //2.将需要加密的字符串转化为byte数组，然后进行随机哈希过程
            byte[] bytes=digest.digest(psd.getBytes());
            //循环bytes，生成32位的MD5字符串
            StringBuffer sb=new StringBuffer();
            for (byte b:bytes
                 ) {
                int i=b & 0xff;
                String s=Integer.toHexString(i);
                if (s.length()<2){
                    s="0"+s;
                }
                sb.append(s);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
