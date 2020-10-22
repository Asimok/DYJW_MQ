package com.example.dyjw_mq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dyjw_mq.tools.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class login extends AppCompatActivity {
    private EditText username_ed, password_ed, capt_ed;
    private String username, password, capt;
    private ImageView capt_img;
    private String capt_url = "http://jwgl.nepu.edu.cn/yzm?d=";
    private String login_url = "http://jwgl.nepu.edu.cn/new/login";
    private OkHttpClient okHttpClient;
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    private String logs = "asdfghj";
    private String cookie = "";
    private String httpUrl_host="jwgl.nepu.edu.cn";


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
//                Toast.makeText(getApplicationContext(),"验证码获取成功",Toast.LENGTH_SHORT).show();
                //通过message，拿到字节数组
                byte[] Picture = (byte[]) msg.obj;
                //使用BitmapFactory工厂，把字节数组转化为bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                //通过imageview，设置图片
                capt_img.setImageBitmap(bitmap);//ImageView 显示验证码的组件
            } else if (msg.what == 2) {
                Toast.makeText(getApplicationContext(), "验证码获取失败", Toast.LENGTH_SHORT).show();
                get_capt_img();
            }
            else if (msg.what == 3) {
                //登录成功
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                //保存 账号密码 cookie
                PrefUtils.setString(getApplicationContext(), "username", username);
                PrefUtils.setString(getApplicationContext(), "password", password);
                String sessionID = cookieStore.get(httpUrl_host).toString().substring(cookieStore.get(httpUrl_host).toString().indexOf("J"), cookieStore.get(httpUrl_host).toString().indexOf(";"));
                PrefUtils.setString(getApplicationContext(), "cookies",sessionID );
//                记录账号
//                "username": "2020-10-21T13:42:43.178+00:00"
               savename(username);


                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (msg.what == 4) {
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                get_capt_img();
            }
            else if (msg.what == 5) {
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    private void savename(String username) {
        Log.d(logs, "进入了"  );
        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .build();
//        String data = "{\"username:\""+username+"}";
        Request request = new Request.Builder().post(formBody).url("http://39.96.68.13:8082/login").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data =response.body().string();
                Log.d(logs, "onResponse: " +data );

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        capt_img = findViewById(R.id.capt_img);
        username_ed = findViewById(R.id.username);
        password_ed = findViewById(R.id.password);
        capt_ed = findViewById(R.id.capt);
        //读取已经保存的数据
        username_ed.setText( PrefUtils.getString(getApplicationContext(),"username",""));

        password_ed.setText( PrefUtils.getString(getApplicationContext(),"password",""));

//自动管理cookie
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {

                        cookieStore.put(httpUrl.host(), list);
                        Log.d(logs,httpUrl.host());
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();


        get_capt_img();


    }

    public void login(View view) {
        username = username_ed.getText().toString();
        password = password_ed.getText().toString();
        capt = capt_ed.getText().toString();
        if (username.equals("")){
            Toast.makeText(getApplicationContext(), "请输入学号！", Toast.LENGTH_SHORT).show();
        }
        else if (password.equals("")){
            Toast.makeText(getApplicationContext(), "请输入密码！", Toast.LENGTH_SHORT).show();
        }
        else if (capt.equals("")){
            Toast.makeText(getApplicationContext(), "请输入验证码！", Toast.LENGTH_SHORT).show();
        }
        else {
            FormBody formBody = new FormBody.Builder()
                    .add("account", username)
                    .add("pwd", password)
                    .add("verifycode", capt)
                    .build();
            Request request = new Request.Builder().post(formBody).url(login_url).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String data =response.body().string();
                    Log.d(logs, "onResponse: " +data );
                    decode(data);
                }
            });
        }
    }

    public void logon(View view) {
        cookieStore.clear();
    }

    public void get_capt_img() {
        capt_url = "http://jwgl.nepu.edu.cn/yzm?d=";
//获取时间戳
        Long startTs = System.currentTimeMillis();
//获取0-999随机数
        int a = 1, b = 999;
        int num = a + (int) (Math.random() * (b - a + 1));
        @SuppressLint("DefaultLocale") String num_str = String.format("%03d", num);
        String capt_d = startTs.toString() + num_str;
        capt_url = capt_url + capt_d;

        initCode(capt_url);
    }

    /**
     * 初始化验证码
     */
    public void initCode(String temp_capt_url) {
        Log.d(logs, temp_capt_url);
        Request request = new Request.Builder().url(temp_capt_url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.obj = "验证码获取失败";
                message.what = 2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] byte_image = response.body().bytes();
                //通过handler更新UI
                Message message = handler.obtainMessage();
                message.obj = byte_image;
                message.what = 1;
                handler.sendMessage(message);

            }
        });
    }

    public void reget_capt(View view) {
        get_capt_img();
    }
    private void decode(String temp_code)
    {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(temp_code);
            int code = jsonObject.getInt("code");//0成功 -1失败
            String message = jsonObject.getString("message");
            if (code==0)
            {
                Message to_message = handler.obtainMessage();
                to_message.obj = message;
                to_message.what = 3;
                handler.sendMessage(to_message);
            }
            else  if (code==-1)
            {
                Message to_message = handler.obtainMessage();
                to_message.obj = message;
                to_message.what = 4;
                handler.sendMessage(to_message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void logout(View view) {
        PrefUtils.setString(getApplicationContext(), "username", "");
        PrefUtils.setString(getApplicationContext(), "password", "");
        PrefUtils.setString(getApplicationContext(), "cookies","" );
        Message message = handler.obtainMessage();
        message.obj = "注销成功";
        message.what = 5;
        handler.sendMessage(message);

    }
}
