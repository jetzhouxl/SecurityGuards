package com.example.administrator.securityguards.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.securityguards.R;
import com.example.administrator.securityguards.utils.ConstantValue;
import com.example.administrator.securityguards.utils.SpUtils;

public class SafeMobileActivity extends AppCompatActivity {

    private TextView tv_phone;
    private TextView tv_reset_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_mobile);

        initUI();
    }

    private void initUI() {
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        String phone= SpUtils.getSafe_PD(getApplicationContext(), ConstantValue.PHONE_NUM,"");

        tv_phone.setText(phone);

        tv_reset_setup= (TextView) findViewById(R.id.tv_reset_setup);
        tv_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SafeMobileActivity.this,SafeSetupActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}
