package com.example.dyjw_mq;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {//加载网络成功进行UI的更新,处理得到的图片资源
                //通过message，拿到字节数组
                byte[] Picture = (byte[]) msg.obj;
                //使用BitmapFactory工厂，把字节数组转化为bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                //通过imageview，设置图片
                capt_img.setImageBitmap(bitmap);//ImageView 显示验证码的组件
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        capt_img = findViewById(R.id.capt_img);
        username_ed = findViewById(R.id.username);
        password_ed = findViewById(R.id.password);
        capt_ed = findViewById(R.id.capt);

//自动管理cookie
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {

                        cookieStore.put(httpUrl.host(), list);
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
                Log.d(logs, "onResponse: " + response.body().string().toString());
            }
        });
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
//                showToast("验证码加载失败");
                Log.d(logs, "验证码加载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] byte_image = response.body().bytes();
                //通过handler更新UI
                Message message = handler.obtainMessage();
                message.obj = byte_image;
                message.what = 1;
                handler.sendMessage(message);
                //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是JSESSIONID）
                Headers headers = response.headers();
//                Log.d(logs, "header " + headers);
//                List<String> cookies = headers.values("Set-Cookie");
//                String session = cookies.get(0);
//                Log.d(logs, "onResponse-size: " + cookies);
//                String sessionID = session.substring(0, session.indexOf(";"));
//                Log.i(logs, "session is  :" + sessionID);
//                cookie = sessionID;
            }
        });
    }

    public void reget_capt(View view) {
        get_capt_img();
    }
}
