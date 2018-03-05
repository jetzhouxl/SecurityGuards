package com.example.administrator.securityguards.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.securityguards.R;

public class AtoolsActivity extends AppCompatActivity {

    private TextView tv_query_phone_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);

        initUI();
    }

    private void initUI() {
        tv_query_phone_address= (TextView) findViewById(R.id.tv_query_phone_address);
        tv_query_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AtoolsActivity.this,QueryPhoneAddressActivity.class));
            }
        });
    }
}
