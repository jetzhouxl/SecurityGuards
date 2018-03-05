package com.example.administrator.securityguards;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;


import com.example.administrator.securityguards.activity.MainActivity;
import com.example.administrator.securityguards.utils.ConstantValue;
import com.example.administrator.securityguards.utils.SpUtils;
import com.example.administrator.securityguards.utils.StringUtil;
import com.example.administrator.securityguards.utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
* slashActivity的作用
* 1.展示app的logo和名字
* 2.app版本
* 3.程序的初始化
* 4.校验程序的合法性，比如对相关的权限或者网络连接情况进行判断
*
* */
public class SlashActivity extends AppCompatActivity {


    private static final String TAG = "SlashActivity";

    /*
    * 更新代码
    * */
    private static final int UPDATA_VERSION=0x123;

    /*
    * 进入主界面
    * */
    private static final int ENTER_MAIN = 0X234;
    /*
    * 服务器url出现问题
    * */
    private static final int URL_ERROR = 0x345;
    /*
    * IO出现异常
    * */
    private static final int IO_ERROR = 0x456;
    /*
    * json解析异常
    * */
    private static final int JSON_ERROR = 0x567;
    //版本信息
    private String versionName;
    private int versionCode;
    private TextView tv_version;

    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA_VERSION:
                    updatashowDialog();
                    break;
                case ENTER_MAIN:
                    enter_Home();
                    //在进入主界面时，关闭导航界面
                    finish();
                    break;
                case URL_ERROR:
                    ToastUtil.show(getApplication(),"URL处理出错");
                    enter_Home();
                    //在进入主界面时，关闭导航界面
                    finish();
                    break;
                case IO_ERROR:
                    ToastUtil.show(getApplication(),"IO处理出错");
                    enter_Home();
                    //在进入主界面时，关闭导航界面
                    finish();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(getApplication(),"JSON处理出错");
                    enter_Home();
                    //在进入主界面时，关闭导航界面
                    finish();
                    break;
            }
        }
    };
    private String versionDer_json;
    private String downloadURL_json;

    /*
    * 进入应用程序主界面
    * */
    private void enter_Home(){
        Intent intent=new Intent(SlashActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /*
    * 当服务器代码高于本地代码是，弹出对话框，进行版本更新
    * */
    private void updatashowDialog(){
        //对话框是绑定到Activity上的所以将上下文智能是this，而不是getApplication()展示对话框
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("版本更新");
        builder.setMessage(versionDer_json);
        //builder.setCancelable(true);
        //积极按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载最新的apk
                downloadApk();
            }
        });
        //点击取消
        builder.setNegativeButton("取消更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enter_Home();

                finish();
            }
        });

        //点击手机返回键
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                enter_Home();

                finish();
            }
        });
        builder.show();
    }

    /*
    * 当确定更新的时候下载最新apk
    * */
    private void downloadApk() {
         //apk下载地址，选择apk挂载地址

        //判断手机sdcard是否有
        Log.e(TAG,"判断手机sdcard是否有");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //如果有sdcard，获取路径,其中的file.separator是文件分割符
            Log.e(TAG,"判断手机sdcard有");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"SecutityGuards.apk";
            //发送请求，获取apk，放置在到指定路径

            HttpUtils httpUtils= new HttpUtils();
            //参数传递，下载的url,下载文件存储路径
            httpUtils.download(downloadURL_json, path, new RequestCallBack<File>() {

                //当文件下载成功
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.e(TAG,"下载成功");

                    //获取下载文件
                    File file=responseInfo.result;
                    //安装apk
                    installApk(file);
                }

                //当文件下载失败
                @Override
                public void onFailure(HttpException e, String s) {
                    e.printStackTrace();
                    Log.e(TAG,"下载失败");
                }

                //开始下载
                @Override
                public void onStart() {
                    super.onStart();
                    Log.e(TAG,"开始下载");
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.e(TAG,"total="+total);
                    Log.e(TAG,"current="+current);

                }
            });
        }

    }

    /*
    * 更新成功以后安装apk
    * file指安装的apk
    * */
    private void installApk(File file) {
          //利用系统安装apk界面进行安装
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        //当intent匹配时，开启安装的界面
        startActivityForResult(intent,0);
    }

    //当一个activity从栈中醒来时，获取从上一个activity返回的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enter_Home();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除activity的title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_slash);
        initUI();
        initData();
        //设置动画
        initAnimation();
        //初始化数据库
        initDB("address.db");
    }

    private void initDB(String dbName) {
        //在files文件夹里面创建文件
        File files=getFilesDir();
        File file=new File(files,dbName);
        if (file.exists()){
            //如果文件存在直接返回
            return;
        }

        InputStream stream=null;
        FileOutputStream fos=null;
        //读取资产目录的文件
        try {
            stream=getAssets().open(dbName);
            fos=new FileOutputStream(file);
            int len=-1;
            byte[] bs=new byte[1024];

            while((len=stream.read(bs))!=-1){
                fos.write(bs,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            if (stream!=null&&fos!=null){
                try {
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    //设置动画
    private void initAnimation() {

    }

    public void initUI(){
        tv_version= (TextView) findViewById(R.id.tv_version);
    }

    public void initData(){
        //1.设置应用版本号
        versionName =getVersion();
        tv_version.setText(versionName);
        //2.检测是否有更新（对比本地应用版本号和服务器版本号），如果有更新，提示用户下载
        versionCode=getVersionCode();
        //3.获取服务端的版本号
        //客户端和服务器的交互使用json
        //根据常见的情况json返回的内容应该有：
        //1.更新版本号
        //2.更新版本的新功能，描述
        //3.版本号
        //4.版本下载地址
        //确定是否有自动更新
        if (SpUtils.getCheck_State(this, ConstantValue.OPEN_UPDATA,false)){
            checkVersion();
        }else{

            //handler延时发送,下面就是4秒后进入界面
            mhandler.sendEmptyMessageDelayed(ENTER_MAIN,4000);
        }

    }

    //检测版本号
    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg=Message.obtain();
                long startTime=System.currentTimeMillis();
                //在此发送一个请求
                try {
                    //封装ip到url
                    URL url=new URL("http://zhouxl.cn:8080/updata1.json");
                    //根据url获取连接
                    HttpURLConnection con= (HttpURLConnection) url.openConnection();
                    //对连接进行设置
                    //请求超时
                    con.setConnectTimeout(2000);
                    //读取超时
                    con.setReadTimeout(3000);
                    //设置请求方式
                    con.setRequestMethod("GET");

                    //获取相应码,如果没有问题解析内容
                    if(con.getResponseCode()==200){
                        InputStream is=con.getInputStream();
                        //获取json内容
                        String json=StringUtil.stream2String(is);
                        Log.e(TAG,"JSON="+json);
                        //json解析
                        try {
                            JSONObject object=new JSONObject(json);

                            //json中的内容
                            String versionName_json=object.getString("versionName");
                            versionDer_json=object.getString("versionDer");
                            String versionCode_json=object.getString("versionCode");
                            downloadURL_json=object.getString("downloadURL");
                          //  Log.e(TAG,"versionName_json="+versionName_json+" versionDer_json="+versionDer_json+" versionCode_json="+versionCode_json+" downloadURL_json="+downloadURL_json);
//
                            if (Integer.parseInt(versionCode_json)>versionCode){
                                //如果两个版本不同，提示用户进行更新
                                //mhandler.sendEmptyMessage(0x123);
                               // msg=Message.obtain();
                                Log.e(TAG,"UPDATA_VERSION");
                                msg.what=UPDATA_VERSION;

                            }else{
                                //如果两个版本相同
                                //msg=Message.obtain();
                                msg.what=ENTER_MAIN;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //msg=Message.obtain();
                            msg.what=JSON_ERROR;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    //msg=Message.obtain();
                    msg.what=URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    //msg=Message.obtain();
                    msg.what=IO_ERROR;
                }finally {
                    long endTime= System.currentTimeMillis();
                    if(endTime-startTime<4000){
                        try {
                            Thread.sleep(4000-(endTime-startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mhandler.sendMessage(msg);
                }
            }
        }).start();
    }

    //获取版本信息
    public String getVersion(){
        PackageManager pm=getPackageManager();
        try {
            //利用包管理来获取包信息,其中表示获取基本信息
            PackageInfo info=pm.getPackageInfo(getPackageName(),0);
            //获取版本名字
            String versionName=info.versionName;

            Log.e(TAG,"versionName="+versionName+"versionCode="+versionCode);
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        //return null;
    }

    public int getVersionCode() {
        PackageManager pm=getPackageManager();
        try {
            //利用包管理来获取包信息,其中表示获取基本信息
            PackageInfo info=pm.getPackageInfo(getPackageName(),0);
            //获取版本代码
            versionCode=info.versionCode;
            Log.e(TAG,"versionName="+versionName+"versionCode="+versionCode);
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
        //return null;
    }
}
