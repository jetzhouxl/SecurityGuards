package com.example.administrator.securityguards.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.securityguards.R;
import com.example.administrator.securityguards.utils.ConstantValue;
import com.example.administrator.securityguards.utils.SpUtils;
import com.example.administrator.securityguards.utils.ToastUtil;

public class SafeSetup2Activity extends AppCompatActivity {

    private static final String TAG = "SafeSetup2Activity";
    private View view;
    private CheckBox sim_cb_box;
    private String sim_card;
    private TextView tv_des;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setup2);

        sim_card=SpUtils.getSafe_PD(getApplicationContext(), ConstantValue.SIM_CARD,"");

        //初始化控件
        initUI();
    }

    private void initUI() {
        view=findViewById(R.id.sim_cb_box);
        sim_cb_box=view.findViewById(R.id.cb_box);
        tv_des=view.findViewById(R.id.tv_des);
        tv_title=view.findViewById(R.id.tv_title);
        tv_title.setText("点击绑定sim卡");

        if (!TextUtils.isEmpty(sim_card)){
            sim_cb_box.setChecked(true);
            tv_des.setText("sim卡已绑定");
        }else{
            sim_cb_box.setChecked(false);
            tv_des.setText("sim卡未绑定");
        }

        sim_cb_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               boolean ischeck=sim_cb_box.isChecked();
               // Log.e(TAG,"ischeck"+ischeck);

                if (ischeck){
                    //如果现在被选中
                    //获取sim卡序列号,首先获取TelephonyManager
                   TelephonyManager tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    //获取sim卡的序列号
                    sim_card=tm.getSimSerialNumber();
                    //存储sim序列号
                    SpUtils.putSafe_PD(getApplicationContext(),ConstantValue.SIM_CARD,sim_card);
                    tv_des.setText("sim卡已绑定");
                    Log.e(TAG,"sim_card    "+sim_card);
                }else{
                    tv_des.setText("sim卡未绑定");
                    SpUtils.remove(getApplicationContext(),ConstantValue.SIM_CARD);
                }

            }
        });
    }


    public void nextPage(View view){

        if (sim_cb_box.isChecked()){
            Intent intent=new Intent(SafeSetup2Activity.this,SafeSetup3Activity.class);
            startActivity(intent);

            finish();
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            ToastUtil.show(getApplicationContext(),"未绑定sim卡！");
        }

    }
    public void prePage(View view){
        Intent intent=new Intent(SafeSetup2Activity.this,SafeSetupActivity.class);
        startActivity(intent);

        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }
}
