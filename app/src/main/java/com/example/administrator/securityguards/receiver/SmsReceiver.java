package com.example.administrator.securityguards.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.securityguards.R;
import com.example.administrator.securityguards.service.LocationSerice;
import com.example.administrator.securityguards.utils.ConstantValue;
import com.example.administrator.securityguards.utils.SpUtils;

/**
 * Created by Administrator on 2018/2/8/008.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static  final String TAG="SmsReceiver";

    private ComponentName mSecurityGuards;
    private DevicePolicyManager dpm;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG,"短信接收到了");

        mSecurityGuards=new ComponentName(context,DeviceAdmin.class);
        dpm= (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
       //1.判断是否设置了防盗
        boolean safe_setup= SpUtils.getCheck_State(context, ConstantValue.SAFE_SETUP,false);
        if (safe_setup){
            //2.获取短信内容
            Object[] objects= (Object[]) intent.getExtras().get("pdus");
            //3.遍历短信内容
            for (Object o:objects
                 ) {
                //获取短信对象
                SmsMessage sms=SmsMessage.createFromPdu((byte[]) o);
                //5.获取短信信息，短信来源
                String from=sms.getOriginatingAddress();
                //获取短信内容
                String body=sms.getMessageBody();

                if (body.contains("alarm")){
                    //7.播放音乐（准备音乐,MediaPlayer）
                    MediaPlayer player=MediaPlayer.create(context, R.raw.ylzs);
                    //设置循环播放
                    player.setLooping(true);
                    //进行播放
                    player.start();
                }

                if (body.contains("location")){
                    //9.如果短信包含location时，对手机进行定位
                    //开启定位服务
                    Intent intent1=new Intent(context, LocationSerice.class);
                    context.startService(intent1);
                }

                if (body.contains("lock")){
                    //是否开启的判断
                    if(dpm.isAdminActive(mSecurityGuards)){
                        //激活--->锁屏
                        dpm.lockNow();
                        //锁屏同时去设置密码
                        dpm.resetPassword("123", 0);
                    }else{
                        Toast.makeText(context, "请先激活", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
