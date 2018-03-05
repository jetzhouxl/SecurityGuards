package com.example.administrator.securityguards.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.securityguards.R;
import com.example.administrator.securityguards.utils.ConstantValue;
import com.example.administrator.securityguards.utils.SpUtils;
import com.example.administrator.securityguards.utils.ToastUtil;

public class SafeSetup4Activity extends AppCompatActivity {

    private CheckBox cb_box;
    //private TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setup4);

        initUI();
    }

    private void initUI() {
        cb_box= (CheckBox) findViewById(R.id.cb_box);
        boolean safe_setup=SpUtils.getCheck_State(getApplicationContext(),ConstantValue.SAFE_SETUP,false);
        if (safe_setup){
            cb_box.setChecked(true);
            cb_box.setText("您已开启防盗保护");
        }

        cb_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck=cb_box.isChecked();
                if (ischeck){
                    cb_box.setChecked(true);
                    cb_box.setText("您已开启防盗保护");
                }else{
                    cb_box.setChecked(false);
                    cb_box.setText("您没有开启防盗保护");
                }

                SpUtils.putCheck_State(getApplicationContext(),ConstantValue.SAFE_SETUP,ischeck);
            }
        });
    }

    public void nextPage(View view){
        boolean safe_setup=SpUtils.getCheck_State(getApplicationContext(),ConstantValue.SAFE_SETUP,false);
        if (safe_setup){
            Intent intent=new Intent(SafeSetup4Activity.this,SafeMobileActivity.class);
            startActivity(intent);

            //SpUtils.putCheck_State(getApplicationContext(), ConstantValue.SAFE_SETUP,true);

            finish();
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            ToastUtil.show(getApplicationContext(),"您没有完成设置");
        }

    }
    public void prePage(View view){
        Intent intent=new Intent(SafeSetup4Activity.this,SafeSetup3Activity.class);
        startActivity(intent);

        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }
}
