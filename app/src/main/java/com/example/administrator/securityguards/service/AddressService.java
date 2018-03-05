package com.example.administrator.securityguards.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.administrator.securityguards.enginer.AddressDao;

public class AddressService extends Service {

    private TelephonyManager mTM;
    private PhoneStateListener listener;
    private String address;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showToast(address);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTM= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        mTM.listen(listener=new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state){
                    case TelephonyManager.CALL_STATE_IDLE:
                        // 空闲
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //摘机
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        //电话来电
                        //showToast(incomingNumber);
                        address=query(incomingNumber);
                        handler.sendEmptyMessage(0);
                        break;

                }
            }
        },PhoneStateListener.LISTEN_CALL_STATE);

    }

    private String query(String incomingNumber) {
        return AddressDao.getAddress(getApplicationContext(),incomingNumber);
    }

    private void showToast(String incomingAddress) {



        Toast.makeText(getApplicationContext(),incomingAddress,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTM.listen(listener,PhoneStateListener.LISTEN_NONE);
    }
}
