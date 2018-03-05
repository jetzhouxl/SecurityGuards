package com.example.administrator.securityguards.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.securityguards.R;
import com.example.administrator.securityguards.utils.ConstantValue;
import com.example.administrator.securityguards.utils.SpUtils;

public class SafeSetup3Activity extends AppCompatActivity {

    private EditText ed_phone_num;
    private Button btn_selector_phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setup3);

        initUI();
    }

    private void initUI() {
        //电话号码的对话框
        ed_phone_num= (EditText) findViewById(R.id.ed_phone_num);
        String phone=SpUtils.getSafe_PD(getApplicationContext(),ConstantValue.PHONE_NUM,"");
        if (!TextUtils.isEmpty(phone)){
            ed_phone_num.setText(phone);
        }
        //选择联系人的按钮
        btn_selector_phone_num= (Button) findViewById(R.id.btn_selector_phone_num);
        btn_selector_phone_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SafeSetup3Activity.this,ContractListActivity.class);
                startActivityForResult(intent,0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回回来的结果
        if (data!=null){
            String phone=data.getStringExtra("phone");
            phone = phone.replace("-", "").replace(" ", "").trim();
            ed_phone_num.setText(phone);

            SpUtils.putSafe_PD(getApplicationContext(), ConstantValue.PHONE_NUM,phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void nextPage(View view){
        Intent intent=new Intent(SafeSetup3Activity.this,SafeSetup4Activity.class);
        startActivity(intent);

        finish();

        //设置动画
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }
    public void prePage(View view){
        Intent intent=new Intent(SafeSetup3Activity.this,SafeSetup2Activity.class);
        startActivity(intent);

        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }
}
