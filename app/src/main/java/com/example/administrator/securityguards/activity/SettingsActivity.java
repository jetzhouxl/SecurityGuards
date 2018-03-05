package com.example.administrator.securityguards.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.securityguards.R;
import com.example.administrator.securityguards.service.AddressService;
import com.example.administrator.securityguards.utils.ConstantValue;
import com.example.administrator.securityguards.utils.SpUtils;

public class SettingsActivity extends AppCompatActivity {

    private View view;
    private View address;
    private View toaststyle;
    private TextView tv_des;
    private CheckBox cb_box;
    private TextView tv_title_address;
    private TextView tv_des_address;
    private CheckBox cb_box_address;
    private TextView tv_title_toaststyle;
    private TextView tv_des_toaststyle;
    private ImageView img_toaststyle;
    private boolean open_updata;
    private boolean showAddress;
    private AlertDialog toastStyleDialog;
    private int index_style;

    //dialog风格字符串
    private String[] toastStyle=new String[]{"透明","橙色","蓝色","灰色","绿色"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        initAddress();
        initToastStyle();

    }

    //设置我们的toast风格
    private void initToastStyle() {
        toaststyle=findViewById(R.id.settings_toast_style);
        tv_title_toaststyle=toaststyle.findViewById(R.id.title);
        tv_des_toaststyle=toaststyle.findViewById(R.id.des_click);
        img_toaststyle=toaststyle.findViewById(R.id.jiantou_click);


        index_style=SpUtils.getInt(this,ConstantValue.TOAST_STYLE,0);
        tv_des_toaststyle.setText(toastStyle[index_style]);
        toaststyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToastStyleDialog();
            }
        });
    }

    //显示dialog，选择风格
    private void showToastStyleDialog() {

        //自定义对话框展示样式
        final AlertDialog.Builder builer=new AlertDialog.Builder(this);
        builer.setIcon(R.mipmap.ic_launcher);
        builer.setTitle("请选择主题风格");
        builer.setSingleChoiceItems(toastStyle, index_style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                index_style=i;
                SpUtils.putInt(getApplicationContext(),ConstantValue.TOAST_STYLE,i);
                dialogInterface.dismiss();
                tv_des_toaststyle.setText(toastStyle[i]);
            }
        });

        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        toastStyleDialog=builer.create();
        toastStyleDialog.show();

    }

    //关于来电地址显示
    private void initAddress() {
        address=findViewById(R.id.settings_address);
        tv_des_address=address.findViewById(R.id.tv_des);
        tv_title_address=address.findViewById(R.id.tv_title);
        cb_box_address=address.findViewById(R.id.cb_box);

        tv_title_address.setText("电话归属地显示设置");

         showAddress=SpUtils.getCheck_State(getApplicationContext(),ConstantValue.SHOW_ADDRESS,false);
        cb_box_address.setChecked(showAddress);
        if (showAddress){
            tv_des_address.setText("开启");
            startService(new Intent(getApplicationContext(), AddressService.class));
        }else{
            tv_des_address.setText("关闭");
            stopService(new Intent(getApplicationContext(),AddressService.class));
        }
        cb_box_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddress=cb_box_address.isChecked();
                if (showAddress){
                    tv_des_address.setText("开启");
                    //开启服务监听我们的电话，同时开启吐司
                    startService(new Intent(getApplicationContext(), AddressService.class));
                }else{
                    tv_des_address.setText("关闭");
                    stopService(new Intent(getApplicationContext(),AddressService.class));
                }
                SpUtils.putCheck_State(getApplicationContext(),ConstantValue.SHOW_ADDRESS,showAddress);

            }
        });
    }

    private void initView() {
        view=findViewById(R.id.settings_item);
        tv_des=view.findViewById(R.id.tv_des);
        cb_box=view.findViewById(R.id.cb_box);

        open_updata=SpUtils.getCheck_State(this, ConstantValue.OPEN_UPDATA,false);
        cb_box.setChecked(open_updata);
        if (open_updata){
            tv_des.setText("自动更新设置已开启");
        }else{
            tv_des.setText("自动更新设置已关闭");
        }
        cb_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_updata=!open_updata;
                SpUtils.putCheck_State(getApplicationContext(),ConstantValue.OPEN_UPDATA,open_updata);
                if (open_updata){
                    tv_des.setText("自动更新设置已开启");
                }else{
                    tv_des.setText("自动更新设置已关闭");
                }
            }
        });
    }
}
