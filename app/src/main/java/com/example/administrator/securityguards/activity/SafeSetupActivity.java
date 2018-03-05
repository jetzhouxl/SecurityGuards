package com.example.administrator.securityguards.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.securityguards.R;

public class SafeSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setup);
    }

    public void nextPage(View view){
        Intent intent=new Intent(SafeSetupActivity.this,SafeSetup2Activity.class);
        startActivity(intent);

        finish();
        //设置动画
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

}
