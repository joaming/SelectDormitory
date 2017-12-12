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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by joaming on 2017/12/12.
 */

public class ChuangPu extends Activity implements View.OnClickListener{
    private ImageView titleBack;
    private int gender;
    private HttpURLConnection conn;
    private static final int CHUANGWEISHU =1;
    private int errcode=1;
    private String data;
    ArrayAdapter<String> adapter;
    private ListView mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chuangpu);

        titleBack=(ImageView)findViewById(R.id.title_back);
        titleBack.setOnClickListener(this);

        initViews();


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.title_back){
            //跳转到页
            Intent intent = new Intent(ChuangPu.this, MainActivity.class);
            startActivity(intent);
            //关闭当前界面
            finish();
        }
    }
    private void initViews(){
        //获取登陆人性别
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String xingbie=sharedPreferences.getString("xingBie","");
        Log.d("myapp","xingbie"+xingbie);
        if(xingbie.equals("男")) {
             gender =1;
        }else if(xingbie.equals("女")){
             gender=2;
        }
        final String ip2 = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender="+gender;
        Log.d("myapp","查询床位数"+ip2);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url=new URL(ip2);
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
                            }
                            String responseStr=response.toString();
                            Log.d("查询", "查询床位数"+responseStr);

                            //将结果传给主线程
                            Message msg = new Message();
                            msg.what=CHUANGWEISHU;
                            msg.obj=responseStr;
                            mHandler.sendMessage(msg);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).start();
    }

    private Handler mHandler =new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case CHUANGWEISHU:
                    String responseStr=(String)msg.obj;
                    Log.d("myapp","查询结果"+responseStr);
                    try {
                        //          创建JSON解析对象(两条规则的体现:大括号用JSONObject,注意传入数据对象)
                        JSONObject obj = new JSONObject(responseStr);
                        errcode = obj.getInt("errcode");
                        data = obj.getString("data");
                        //第二层解析
                        JSONObject obj1 = new JSONObject(data);
                        Log.d("myapp","床位数解析结果"+data);

                        String[] chuanweidata=new String[5];
                        chuanweidata[0]="5号楼剩余床位数："+obj1.getString("5");
                        chuanweidata[1]="13号楼剩余床位数："+obj1.getString("13");
                        chuanweidata[2]="14号楼剩余床位数："+obj1.getString("14");
                        chuanweidata[3]="8号楼剩余床位数："+obj1.getString("8");
                        chuanweidata[4]="9号楼剩余床位数："+obj1.getString("9");
                        Log.d("myapp","床位数解析结果"+chuanweidata[0]);

                        mList=(ListView)findViewById(R.id.title_list);
                        adapter=new ArrayAdapter<String>(
                                ChuangPu.this,android.R.layout.simple_list_item_1,chuanweidata);
                        mList.setAdapter(adapter);





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };


}
