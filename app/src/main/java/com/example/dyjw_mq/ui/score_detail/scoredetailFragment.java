package com.example.dyjw_mq.ui.score_detail;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dyjw_mq.R;
import com.example.dyjw_mq.login;
import com.example.dyjw_mq.tools.PrefUtils;
import com.example.dyjw_mq.ui.gpa.gpa_datainfo;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class scoredetailFragment extends Fragment {

    private scoredetailViewModel coursedetailViewModel;

    private List<String> all_xuenian;
    private ArrayList<getscore_datainfo> all_score;
    private ArrayList<scoredetail_datainfo> all_list_score;
    private ArrayList<gpa_datainfo> all_credit;
    private ListView show_scoredetail_lv;
    private String url = "http://jwgl.nepu.edu.cn/xskccjxx!getDataList.action";
    private String url_jd = "http://jwgl.nepu.edu.cn/xskccjxx!getJdDataList.action?ckfs=1&ckjh=";
    private String logs = "scoredetailFragment_log";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        coursedetailViewModel =
                ViewModelProviders.of(this).get(scoredetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scoredetail, container, false);
        show_scoredetail_lv = root.findViewById(R.id.showscoredetail_lv);

        all_list_score = new ArrayList<scoredetail_datainfo>();
        all_credit = new ArrayList<gpa_datainfo>();
        all_score = new ArrayList<getscore_datainfo>();
        all_xuenian = new ArrayList<String>();

        String cookies = PrefUtils.getString(getActivity(), "cookies", "");//读取已经保存的数据
        if (cookies.equals(""))
            relogin();

        sendRequest(cookies);
        sendRequest_jd(cookies);
        return root;
    }

    private void sendRequest_jd(String cookies) {

        FormBody formBody = new FormBody.Builder()

                .add("page", "1")
                .add("rows", "200")
                .add("sort", "mc")
                .add("order", "asc")
                .build();
        OkHttpClient okhttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url_jd)
                .header("Cookie", cookies)
                .post(formBody)
                .build();
        Call call = okhttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "连接教务处服务器失败！", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String res = response.body().string();//获取到传过来的字符串
                if (res.contains("{\"total\":") && res.contains("\"rows\":")) {
                    Log.d(logs, res);
                    try {
                        JSONObject jsonObj = new JSONObject(res);

                        String total = jsonObj.getString("total");
                        Log.d(logs, total);

                        if (Integer.parseInt(total) > 0) {
                            String rows = jsonObj.getString("rows");
                            //转换成JSON数组
                            JSONArray jsonArray = new JSONArray(rows);


                            for (int i = 0; i < Integer.parseInt(total); i++) {
                                JSONObject jsonObj2 = jsonArray.getJSONObject(i);
                                String mc = jsonObj2.getString("mc");
                                String pjxfjd = jsonObj2.getString("pjxfjd");

                                gpa_datainfo mapx = new gpa_datainfo();
                                mapx.setMc(mc);
                                mapx.setPjxfjd(pjxfjd);
                                all_credit.add(mapx);
                            }
                            if (cookies.equals(""))
                                relogin();

                        }
//                            else if (Integer.parseInt(total) == 0) {
////                                show_score_Result("暂未查询到",  "", "0");
//                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else//重新登录
                {
                    relogin();
                }
            }

        });
    }

    private void sendRequest(String cookies) {


        FormBody formBody = new FormBody.Builder()
                .add("page", "1")
                .add("rows", "200").build();
        OkHttpClient okhttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .header("Cookie", cookies)
                .post(formBody)
                .build();
        Call call = okhttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "连接教务处服务器失败！", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String res = response.body().string();//获取到传过来的字符串
                if (res.contains("{\"total\":") && res.contains("\"rows\":")) {
//                    Log.d(logs, res);

                    try {

                        JSONObject jsonObj = new JSONObject(res);
                        String total = jsonObj.getString("total");
                        Log.d(logs, total);
                        {
                            if (Integer.parseInt(total) > 0) {
                                String rows = jsonObj.getString("rows");
                                //转换成JSON数组
                                JSONArray jsonArray = new JSONArray(rows);

                                for (int i = 0; i < Integer.parseInt(total); i++) {
                                    JSONObject jsonObj2 = jsonArray.getJSONObject(i);


                                    String gpa = jsonObj2.getString("cjjd");
                                    String credit = jsonObj2.getString("xf");
                                    String score = jsonObj2.getString("zcjfs");
                                    String xuenian = jsonObj2.getString("xnxqmc");

                                    getscore_datainfo mapx = new getscore_datainfo();
//                                    Log.d(logs,gpa);
                                    mapx.setGpa(gpa);
                                    mapx.setXuenian(xuenian);
                                    mapx.setCredit(credit);
                                    mapx.setScore(score);


                                    all_score.add(mapx);

                                }

                                //处理数据
                                changedata();
                            } else if (Integer.parseInt(total) == 0) {
//                                show_score_Result("当前学期暂未出分", "", "", "", "", "0");

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else//重新登录
                {
                    relogin();
                }
            }

        });
    }

    private void changedata() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //得到学期
                int len = all_score.size();
                for (int i = 0; i < len; i++) {
                    if (!all_xuenian.contains(all_score.get(i).getXuenian())) {
                        all_xuenian.add(all_score.get(i).getXuenian());
                    }

                }
                Collections.sort(all_xuenian);
                for (int i = 0; i < all_xuenian.size(); i++)

                    Log.d(logs, all_xuenian.get(i));
                createdata();
            }
        });
    }

    private void createdata() {
//        getActivity().runOnUiThread(new Runnable() {
//            @SuppressLint("DefaultLocale")
//            @Override
//            public void run() {

        //得到学期
        int len = all_xuenian.size();
        for (int i = 0; i < len; i++) {
            float max = 0, ssum = 0, sgpa = 0, meangpa = 0, means = 0, sc = 0;
            int lt60 = 0, aclass = 0;//小于60的数
            int rt60 = 0, rt70 = 0, rt80 = 0, rt90 = 0, rt100 = 0;
            for (int j = 0; j < all_score.size(); j++) {
                if (all_score.get(j).getXuenian().equals(all_xuenian.get(i))) {
                    float score = Float.parseFloat(all_score.get(j).getScore());
                    if (max < score)
                        max = score;
                    if (score >= 60)
                        lt60++;
                    if (score < 60)
                        rt60++;
                    else if (score < 70)
                        rt70++;
                    else if (score < 80)
                        rt80++;
                    else if (score < 90)
                        rt90++;
                    else if (score < 1000)
                        rt100++;
//                    Log.d(logs, all_score.get(j).getGpa());
                    sc += Float.parseFloat(all_score.get(j).getCredit());
                    ssum += score * Float.parseFloat(all_score.get(j).getCredit());


                    aclass++;

                }
            }

            means = ssum / sc;
            scoredetail_datainfo mapx = new scoredetail_datainfo();
            for (int m = 0; m < all_credit.size(); m++) {
                if (all_credit.get(m).getMc().equals(all_xuenian.get(i)))
                    mapx.setGpa(all_credit.get(m).getPjxfjd());
            }

            //成绩分布
            ArrayList<BarEntry> values = new ArrayList<>();
            values.add(new BarEntry(6, rt60));
            values.add(new BarEntry(7, rt70));
            values.add(new BarEntry(8, rt80));
            values.add(new BarEntry(9, rt90));
            values.add(new BarEntry(10, rt100));

            mapx.setXuenian(all_xuenian.get(i));
            mapx.setMaxscore(String.valueOf(max));
            mapx.setMean(String.format("%.2f", means));
            mapx.setValues(values);
            double percent = (double) lt60 / aclass;
            NumberFormat nt = NumberFormat.getPercentInstance();//获取格式化对象
            nt.setMinimumFractionDigits(0);//设置百分数精确度2即保留两位小数
            mapx.setPassrate(nt.format(percent));
            all_list_score.add(mapx);

            //可以显示一轮
            show_score_Result();
        }
//            }
//        });
    }

    public void show_score_Result()
    //封装遍历的数据
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                //数据适配器
                scoredetail_adapter score_data_adapter = new scoredetail_adapter(getActivity(), all_list_score);
                show_scoredetail_lv.setAdapter(score_data_adapter);
            }
        });
    }

    public void relogin() {

        Intent intent = new Intent(getActivity(), login.class);
        startActivity(intent);

    }
}