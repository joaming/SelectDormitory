package cn.eud.pku.joaming.selectdormitory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by joaming on 2017/11/16.
 */

public class LogIn extends Activity implements View.OnClickListener {
    private EditText mAccount;
    private EditText mPWD;
    private View mLogin;
    private int errcode=1;
    private String errmsg;
    private static final int LOGIN =1;
    private HttpURLConnection conn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        // 获取存储的数据
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);

        int logInFlag = sp.getInt("logInFlag", 1);
        if(logInFlag==0){
            //如果用户已经登录，直接跳转到MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //关闭当前界面
            finish();
        }
        //设置登陆监听
        mLogin=findViewById(R.id.login);
//        mLogin=findViewById(R.id.login);
        mLogin.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login) {
            mAccount=(EditText)findViewById(R.id.account);
            mPWD=(EditText)findViewById(R.id.pwd);
            final String name = mAccount.getText().toString();
            final String password = mPWD.getText().toString();
            //检验网络
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("mydenglu", "网络OK");
            } else {
                Log.d("mydenglu", "网络挂了");
                Toast.makeText(LogIn.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
                //进行验证
            if (name.length()<=0 && password.length()<=0) {

                Toast.makeText(LogIn.this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            } else if (name.length()<=0) {
                Toast.makeText(LogIn.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            } else if (password.length()<=0) {
                Toast.makeText(LogIn.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            } else if (name != null && password != null) {

                // 存储学号和姓名
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                editor.putString("usercode", name);
                editor.putString("password", password);
                editor.commit();
                Log.d("mima","xingmingmima"+name+password);
                loginByGet(name, password);
                // 调用loginByPost方

            }

        }
    }
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case LOGIN:
                    String responseStr=(String)msg.obj;
                    Log.d("result", "返回字符串"+responseStr);
                    try {
                        //          创建JSON解析对象(两条规则的体现:大括号用JSONObject,注意传入数据对象)
                        JSONObject obj = new JSONObject(responseStr);
                        errcode = obj.getInt("errcode");
                        errmsg = obj.getString("errmsg");
                        Log.d("errcode的数值","数值"+errcode);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //判断用户名与密码是否和保存的数据一致，进行提醒或者登录
                    if (errcode==0) {
                        //设置登陆标志为1
                        SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                        editor.putInt("logInFlag", 0);
                        editor.commit();
                        //实现界面的跳转
                        Intent intent = new Intent(LogIn.this, MainActivity.class);
                        startActivity(intent);
                        //关闭当前界面
                        finish();
                    } else {
                        Toast.makeText(LogIn.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    public void loginByGet(String userName, String userPass) {

        final  String ip = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?"+"username="
                + userName + "&password=" + userPass;
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url =new URL(ip);
                    if("https".equalsIgnoreCase(url.getProtocol())){
                        SslUtils.ignoreSsl();
                    }

                    conn=(HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("select", str);
                    }
                    String responseStr=response.toString();
                    Log.d("select", "返回成功"+responseStr);
                    //将结果传给主线程
                    Message msg = new Message();
                    msg.what=LOGIN;
                    msg.obj=responseStr;
                    mHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


}
