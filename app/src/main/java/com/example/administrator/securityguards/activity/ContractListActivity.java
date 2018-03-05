package com.example.administrator.securityguards.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.securityguards.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContractListActivity extends AppCompatActivity {

    private static final String TAG = "ContractListActivity";
    private ListView lv_contact;
    private MyAdapter myAdapter;
    //存储联系人的数据结构
    private List<HashMap<String,String>> contactlist=new ArrayList<HashMap<String,String>>();

    //使用适配器来装配数据
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myAdapter=new MyAdapter();
            lv_contact.setAdapter(myAdapter);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_list);

        initUI();
    }

    //寻找控件
    private void initUI() {
        lv_contact= (ListView) findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Intent intent=new Intent();
                //首先判断内容是否为空
                if (myAdapter!=null){
                    HashMap<String,String> hashMap=myAdapter.getItem(i);
                    String phone=hashMap.get("phone");
                    //3,此电话号码需要给第三个导航界面使用

                    //4,在结束此界面回到前一个导航界面的时候,需要将数据返回过去
                    Intent intent=new Intent();
                    intent.putExtra("phone",phone);
                    setResult(0,intent);

                    //最后结束界面
                    finish();
                }

            }
        });
        initData();
    }

    //获取适配器的数据，即联系人内容
    private void initData() {

        //考虑到联系人可能很多，所以是一个可能的耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                //要获取联系人列表，通过contentprovider来获取
                //1.获得内容解析器
                ContentResolver contentResolver=getContentResolver();
                //2.利用内容解析器进行查询,返回一个集合（读取联系人权限）
                Cursor cursor=contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null,
                        null,
                        "");//这个句子就是sql语句的抽象
                //cursor.moveToNext()相当于移动游标，同时判断游标是否为空
                contactlist.clear();
                while(cursor.moveToNext()){
                    //获取此游标的第i列内容,即用户id
                    String id=cursor.getString(0);
                    //Log.e(TAG,"     "+id);
                   //3.根据用户唯一性id来获取用户的信息
//                    try{
                    Cursor indexCursor = contentResolver.query(
                            Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1","mimetype"},
                            "raw_contact_id = ?", new String[]{id}, null);

                        //通过循环获取用户的内容
                    //每次获取联系人的时候都清空数据结构，不然会有很多重复的值

                    HashMap<String,String> hashMap=new HashMap<String, String>();
                 //   Log.e(TAG,indexCursor.toString());
                        while(indexCursor.moveToNext()){
                            //获取data1
                          //  String data1=indexCursor.getString(0);
                            //Log.e(TAG,data1);
                            //获取mimetype
                           // Log.e(TAG,"data="+indexCursor.getString(0));
                            //Log.e(TAG,indexCursor.getString(1));
                            //注意两者的内容是根据上面的语句
                            String data=indexCursor.getString(0);
                            String type=indexCursor.getString(1);

                            //判断data数据类型,前面是电话号码，后者是名字
                            if("vnd.android.cursor.item/phone_v2".equals(type)){
                                   //判断数据是否为空，如果不为空放入
                                    if (!TextUtils.isEmpty(data)){
                                        hashMap.put("phone",data);
                                    }
                            }else if ("vnd.android.cursor.item/name".equals(type)){
                                    if (!TextUtils.isEmpty(data)){
                                        hashMap.put("name",data);
                                    }

                            }

                        }
                    //关闭资源，添加数据
                        indexCursor.close();
                        contactlist.add(hashMap);




                }
                //关闭资源
                cursor.close();
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    //listview的适配器
    class MyAdapter extends BaseAdapter {
        //获取数据个数
        @Override
        public int getCount() {
            return contactlist.size();
        }

        //获取指定位置数据
        @Override
        public HashMap<String,String> getItem(int i) {
            return contactlist.get(i);
        }

        //获取指定位置
        @Override
        public long getItemId(int position) {
            return position;
        }

        //以上是数据操作，这个方法是对于视图的操作
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v=View.inflate(getApplicationContext(),R.layout.listview_contact_item,null);

            TextView tv_name=v.findViewById(R.id.tv_name);
            TextView tv_phone=v.findViewById(R.id.tv_phone);

            tv_name.setText(getItem(i).get("name"));
            tv_phone.setText(getItem(i).get("phone"));
            return v;
        }
    }

}
