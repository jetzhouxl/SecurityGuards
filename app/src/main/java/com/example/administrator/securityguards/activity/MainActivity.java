package com.example.administrator.securityguards.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.securityguards.R;
import com.example.administrator.securityguards.utils.ConstantValue;
import com.example.administrator.securityguards.utils.MD5Util;
import com.example.administrator.securityguards.utils.SpUtils;
import com.example.administrator.securityguards.utils.ToastUtil;

public class MainActivity extends AppCompatActivity {

    private GridView gd_main;
    private  String[] titles;
    private int[] pics;
    private AlertDialog set_dialog;

    private EditText ed_set_psd;
    private EditText ed_confirm_psd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化UI
        initUI();
        //为gridview设置数据
        initData();
    }

    private void initData() {
        //需要一个适配器
        //然后为适配器准备数据（文字和图片，各9组）
        titles = new String[]{
              "手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"
        };

        pics=new int[]{
                R.drawable.home_callmsgsafe,R.drawable.home_netmanager,R.drawable.home_apps,R.drawable.home_taskmanager,R.drawable.home_safe
                ,R.drawable.home_sysoptimize,R.drawable.home_trojan,R.drawable.home_settings,R.drawable.home_settings
        };


        //为gridview设置数据
        gd_main.setAdapter(new MyAdapter());
        //为gridview设置监听事件
        gd_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           //position点中列表条目的索引
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                  switch(position){
                      case 0:
                          showDialod();
                          break;
                      case 1:
                          break;
                      case 2:
                          break;
                      case 3:
                          break;
                      case 4:
                          break;
                      case 5:
                          break;
                      case 6:
                          break;
                      case 7:
                          //Intent intent=new Intent(getApplication(),SettingsActivity.class);
                          startActivity(new Intent(getApplication(),AtoolsActivity.class));
                          break;
                      case 8:
                          Intent intent=new Intent(getApplication(),SettingsActivity.class);
                          startActivity(intent);
                          break;


                  }
            }
        });
    }

    private void showDialod() {
       //首先判断是否有密码，
       String psd= SpUtils.getSafe_PD(this, ConstantValue.SAFE_PSD,"");


        if (TextUtils.isEmpty(psd)){
            //如果psd为“”，说明是第一次进入
            showSetPSDDialog();

        }else{
            //如果有说明不是第一次进入
            showConfirmDialog();
        }
    }


    /*
    * 密码确认对话框
    * */
    private void showConfirmDialog() {
       AlertDialog.Builder builder=new AlertDialog.Builder(this);
       final AlertDialog confirm_dialog=builder.create();
        final View view=View.inflate(getApplicationContext(),R.layout.dialog_confirm_psd,null);
        confirm_dialog.setView(view);
        confirm_dialog.show();

        Button btn_cancel=view.findViewById(R.id.btn_cancel);
        Button btn_comfirm=view.findViewById(R.id.btn_confim);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_dialog.dismiss();
            }
        });

        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed_psd_confirm=view.findViewById(R.id.ed_set_psd);
                String edstr=ed_psd_confirm.getText().toString();
                String md_str=MD5Util.encoder(edstr);
                if (!TextUtils.isEmpty(edstr)){
                    String sp_psd=SpUtils.getSafe_PD(getApplicationContext(),ConstantValue.SAFE_PSD,"");
                    if (sp_psd.equals(md_str)){
                        boolean safe_setup=SpUtils.getCheck_State(getApplicationContext(),ConstantValue.SAFE_SETUP,false);
                        if (safe_setup){
                            Intent intent=new Intent(MainActivity.this,SafeMobileActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent=new Intent(MainActivity.this,SafeSetupActivity.class);
                            startActivity(intent);
                        }

                        confirm_dialog.dismiss();
                    }else {
                        ToastUtil.show(getApplicationContext(),"密码错误！");
                    }
                }else{
                    ToastUtil.show(getApplicationContext(),"密码不能为空");
                }
            }
        });
    }

    /*
    * 密码初始化对话框
    * */
    private void showSetPSDDialog() {
        //自定义对话框展示样式
        AlertDialog.Builder builer=new AlertDialog.Builder(this);
        set_dialog=builer.create();
        View view=View.inflate(this,R.layout.dialog_set_psd,null);
        //将对话框的内容与xml绑定
        set_dialog.setView(view);
        set_dialog.show();

        Button btn_cancel=view.findViewById(R.id.btn_cancel);
        Button btn_comfirm=view.findViewById(R.id.btn_confim);

       ed_set_psd=view.findViewById(R.id.ed_set_psd);
        ed_confirm_psd=view.findViewById(R.id.ed_condirm_psd);

        //当用户点击取消的时候，对话框消失即可
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_dialog.dismiss();
            }
        });

        //当用户点击确认的时候，向sp插入值，同时跳转Activity
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String str_set=ed_set_psd.getText().toString();
                String str_confirm=ed_confirm_psd.getText().toString();
                //密码首先判断是否为空
                if (!TextUtils.isEmpty(str_set)&&!TextUtils.isEmpty(str_confirm)){
                   //判断密码是否相等
                    if (str_set.equals(str_confirm)){
                        Intent intent=new Intent(MainActivity.this,SafeSetupActivity.class);
                        startActivity(intent);
                        set_dialog.dismiss();

                        SpUtils.putSafe_PD(getApplicationContext(),ConstantValue.SAFE_PSD, MD5Util.encoder(str_set));
                    }else{
                        Toast.makeText(getApplication(),"密码不相等",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    //否则做出提醒
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void initUI() {
        gd_main= (GridView) findViewById(R.id.gd_mian);

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            //条目的总数
            return titles.length;
        }

        @Override
        public Object getItem(int i) {

            return titles[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewitem=View.inflate(getApplication(),R.layout.gridview_item,null);
            ImageView image=viewitem.findViewById(R.id.gd_icon);
            TextView tv=viewitem.findViewById(R.id.gd_title);

            image.setImageResource(pics[i]);
            tv.setText(titles[i]);
            return viewitem;
        }
    }
}
