package cn.eud.pku.joaming.selectdormitory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.R.color.black;

/**
 * Created by joaming on 2017/12/13.
 */

public class Dormitorys extends Activity implements View.OnClickListener{
    private ImageView backForward2,nextBtn3;
    private TextView dorm5,dorm8,dorm9,dorm13,dorm14;
    private  HttpURLConnection conn;
    private static final int SUCCESS =1;
    private int errcode=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dormitorys);

        backForward2=(ImageView)findViewById(R.id.backforward2);
        backForward2.setOnClickListener(this);
        nextBtn3=(ImageView)findViewById(R.id.next3);
        nextBtn3.setOnClickListener(this);

        dorm5=(TextView)findViewById(R.id.dormitory5);
        dorm5.setOnClickListener(this);
        dorm8=(TextView)findViewById(R.id.dormitory8);
        dorm8.setOnClickListener(this);
        dorm9=(TextView)findViewById(R.id.dormitory9);
        dorm9.setOnClickListener(this);
        dorm13=(TextView)findViewById(R.id.dormitory13);
        dorm13.setOnClickListener(this);
        dorm14=(TextView)findViewById(R.id.dormitory14);
        dorm14.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backforward2){
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",0);
            editor.commit();
            //跳转到页
            Intent intent = new Intent(Dormitorys.this, TogetherSlelect.class);
            startActivity(intent);
            //关闭当前界面
            finish();
        }
        if (v.getId()==R.id.dormitory5){
            dorm5.setTextColor(Color.rgb(0,0,0));
            dorm8.setTextColor(Color.rgb(225,225,225));
            dorm9.setTextColor(Color.rgb(225,225,225));
            dorm13.setTextColor(Color.rgb(225,225,225));
            dorm14.setTextColor(Color.rgb(225,225,225));
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",5);
            Log.d("myapp","选择楼号"+5);
            editor.commit();
        }
        if (v.getId()==R.id.dormitory8){
            dorm8.setTextColor(Color.rgb(0,0,0));
            dorm5.setTextColor(Color.rgb(225,225,225));
            dorm9.setTextColor(Color.rgb(225,225,225));
            dorm13.setTextColor(Color.rgb(225,225,225));
            dorm14.setTextColor(Color.rgb(225,225,225));
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",8);
            Log.d("myapp","选择楼号"+8);
            editor.commit();
        }
        if (v.getId()==R.id.dormitory9){
            dorm9.setTextColor(Color.rgb(0,0,0));
            dorm8.setTextColor(Color.rgb(225,225,225));
            dorm5.setTextColor(Color.rgb(225,225,225));
            dorm13.setTextColor(Color.rgb(225,225,225));
            dorm14.setTextColor(Color.rgb(225,225,225));
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",9);
            Log.d("myapp","选择楼号"+9);
            editor.commit();
        }
        if (v.getId()==R.id.dormitory13){
            dorm13.setTextColor(Color.rgb(0,0,0));
            dorm8.setTextColor(Color.rgb(225,225,225));
            dorm9.setTextColor(Color.rgb(225,225,225));
            dorm5.setTextColor(Color.rgb(225,225,225));
            dorm14.setTextColor(Color.rgb(225,225,225));
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",13);
            Log.d("myapp","选择楼号"+13);
            editor.commit();
        }
        if (v.getId()==R.id.dormitory14){
            dorm14.setTextColor(Color.rgb(0,0,0));
            dorm8.setTextColor(Color.rgb(225,225,225));
            dorm9.setTextColor(Color.rgb(225,225,225));
            dorm13.setTextColor(Color.rgb(225,225,225));
            dorm5.setTextColor(Color.rgb(225,225,225));
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("Dormitory",14);
            Log.d("myapp","选择楼号"+14);
            editor.commit();
        }
        if (v.getId()==R.id.next3){
            //创建map类型
            Map<String, Object> map = new HashMap<String, Object>();
            //获取总人数信息
            SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
            int classMateNumbe=sharedPreferences.getInt("classMateNumbe",0);
            int totalNM=classMateNumbe+1;
            int i=classMateNumbe;

            Log.d("myapp","办理人总数"+totalNM);
            map.put("num", totalNM);
            //获取办理人
            String usercode=sharedPreferences.getString("usercode","");
            Log.d("myapp","办理人id"+usercode);
            map.put("stuid", usercode);
            if(i>0){
                String xueHao1=sharedPreferences.getString("xueHao1","");
                map.put("stu1id", xueHao1);
                String yanZhengma1=sharedPreferences.getString("yanZhengma1","");
                map.put("v1code", yanZhengma1);
                i--;
            }
            if(i>0){
                String xueHao2=sharedPreferences.getString("xueHao2","");
                map.put("stu2id", xueHao2);
                String yanZhengma2=sharedPreferences.getString("yanZhengma2","");
                map.put("v2code", yanZhengma2);
                i--;
            }
            if(i>0){
                String xueHao3=sharedPreferences.getString("xueHao3","");
                map.put("stu3id", xueHao3);
                String yanZhengma3=sharedPreferences.getString("yanZhengma2","");
                map.put("v3code", yanZhengma3);
                i--;
            }
            if(i>0){
                String xueHao4=sharedPreferences.getString("xueHao4","");
                map.put("stu4id", xueHao4);
                String yanZhengma4=sharedPreferences.getString("yanZhengma4","");
                map.put("v4code", yanZhengma4);
                i--;
            }
            int DormitoryNum=sharedPreferences.getInt("Dormitory",0);
            map.put("buildingNo",DormitoryNum );
            Log.d("myapp","map里的值"+map);

            //连接网络
            StringBuffer sbRequest =new StringBuffer();
            if(map!=null&&map.size()>0){
                for (String key:map.keySet()){
                    sbRequest.append(key+"="+map.get(key)+"&");
                }
            }
            final String request = sbRequest.substring(0,sbRequest.length()-1);
            final String ip3="https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(ip3);
                                if("https".equalsIgnoreCase(url.getProtocol())){
                                    SslUtils.ignoreSsl();
                                }
                                conn=(HttpURLConnection)url.openConnection();
                                conn.setRequestMethod("POST");
                                //通过正文发送数据
                                OutputStream os =conn.getOutputStream();
                                os.write(request.getBytes());
                                os.flush();

                                InputStream in = conn.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                StringBuilder response = new StringBuilder();
                                String str;
                                while((str=reader.readLine()) != null){
                                    response.append(str);
                                    Log.d("myapp", str);
                                }
                                String responseStr=response.toString();
                                Log.d("myapp", "选宿舍结果"+responseStr);

                            //将结果传给主线程
                            Message msg = new Message();
                            msg.what=SUCCESS;
                            msg.obj=responseStr;
                            mHandler.sendMessage(msg);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
            ).start();

        }

    }
    private Handler mHandler =new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case SUCCESS:
                    String responseStr=(String)msg.obj;
                    Log.d("myapp","查询结果"+responseStr);
                    try {
                        //          创建JSON解析对象(两条规则的体现:大括号用JSONObject,注意传入数据对象)
                        JSONObject obj = new JSONObject(responseStr);
                        errcode = obj.getInt("errcode");
                        if(errcode==0){
                            Toast.makeText(Dormitorys.this, "选宿舍成功", Toast.LENGTH_LONG).show();
                            //跳转到页
                            Intent intent = new Intent(Dormitorys.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(Dormitorys.this, "选宿舍失败，请重新选宿舍", Toast.LENGTH_LONG).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };


}
