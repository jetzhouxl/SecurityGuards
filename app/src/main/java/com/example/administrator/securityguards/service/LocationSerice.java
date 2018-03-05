package com.example.administrator.securityguards.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.securityguards.utils.ConstantValue;
import com.example.administrator.securityguards.utils.SpUtils;

/**
 * Created by Administrator on 2018/2/8/008.
 */

public class LocationSerice extends Service {


    //第一次创建
    @Override
    public void onCreate() {
        super.onCreate();
        //获取手机经纬度坐标
        //1.获取位置管理者
        Log.e(" LocationSerice","kaiaiaiaiiaiai");
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //2.以最优的方式获取左边
        //3.网络定位
        Criteria criteria = new Criteria();
        //设置权限
        //允许花费
        criteria.setCostAllowed(true);
        //设置精度,设置最高的经纬度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        lm.getBestProvider(criteria, true);//只能传true
        //在一定时间间隔不断获取内容
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //获取经度
                 double longitude=location.getLongitude();
                //获取纬度
                double latitude=location.getLatitude();

                //获取位置之后发送短信
                String phone=SpUtils.getSafe_PD(getApplicationContext(), ConstantValue.PHONE_NUM,"");
                if (!TextUtils.isEmpty(phone)){
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(phone,null,"经度："+longitude+",纬度："+latitude,null,null);
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //其他的时候创建时使用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
