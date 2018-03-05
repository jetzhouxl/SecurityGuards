package com.example.administrator.securityguards.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.securityguards.R;
import com.example.administrator.securityguards.enginer.AddressDao;

public class QueryPhoneAddressActivity extends AppCompatActivity {

    private EditText ed_phone_num_query;
    private TextView tv_address_phone;
    private String address="未知号码";
    private String phone;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_address_phone.setText(address);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_phone_address);

        initUI();
    }

    private void initUI() {
        ed_phone_num_query= (EditText) findViewById(R.id.ed_phone_num_query);
        tv_address_phone= (TextView) findViewById(R.id.tv_address_phone);

        ed_phone_num_query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                queryphone(QueryPhoneAddressActivity.this);
            }
        });

    }

    public void query(View view){


        if (!TextUtils.isEmpty(phone)){
              queryphone(QueryPhoneAddressActivity.this);
        }else {
            Animation shake= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
            ed_phone_num_query.startAnimation(shake);
            //手机震动效果
            Vibrator vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
        }
    }

    //这是一个耗时操作,所以开启一个消息机制
    public void queryphone(Context context){
        phone=ed_phone_num_query.getText().toString();
        address=AddressDao.getAddress(this,phone);

        handler.sendEmptyMessage(0);

    }

}
