package com.example.dyjw_mq.ui.timetable;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dyjw_mq.R;
import com.example.dyjw_mq.tools.PrefUtils;
import com.example.dyjw_mq.ui.news.BackHandledFragment;
import com.example.dyjw_mq.ui.news.newsFragment;


public class timetableFragment extends BackHandledFragment {
    private WebView webView;
    private WebSettings webSettings;
    private timetableViewModel timetableViewModel;
    private String url = "http://jwgl.nepu.edu.cn/login!welcome.action";
    private String logs="timetableFragment_logs";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        timetableViewModel =
                ViewModelProviders.of(this).get(timetableViewModel.class);
        View root = inflater.inflate(R.layout.frament_timetable, container, false);


        webView = (WebView)root.findViewById(R.id.timetableWebView);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

// 设置可以支持缩放
        webSettings.setSupportZoom(true);
// 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);

        webView.setWebViewClient(new timetableFragment.MyWebViewClient());
        String cookies = PrefUtils.getString(getActivity(), "cookies", "");//读取已经保存的数据
        setCookie(cookies,url);
        webView.loadUrl(url);
        return root;
    }
    private void setCookie(String cookie,String url) {
        String StringCookie = cookie;
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookie();
            CookieSyncManager.getInstance().sync();
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, StringCookie);
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //    return super.shouldOverrideUrlLoading(view, url);
//            view.loadUrl(url);
            openApp(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            //     super.onReceivedError(view, errorCode, description, failingUrl);
            //  Toast.makeText(this,"网页加载错误！",0).show();
        }


    }
    @Override
    public  boolean onBackPressed(){

        if(webView.canGoBack()){
            webView.goBack();
            Log.d(logs, "webView.goBack()");
            return true;

        }else{
            Log.d(logs,"Conversatio退出");
            return false;
        }

    }

    //判断app是否安装
    private boolean isInstall(Intent intent) {
        return this.getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    //打开app
    private boolean openApp(String url) {
        if (TextUtils.isEmpty(url)) return false;
        try {
            if (!url.startsWith("http") || !url.startsWith("https") || !url.startsWith("ftp")) {
                Uri uri = Uri.parse(url);
                String host = uri.getHost();
                String scheme = uri.getScheme();
                //host 和 scheme 都不能为null
                if (!TextUtils.isEmpty(host) && !TextUtils.isEmpty(scheme)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    再跳回来
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (isInstall(intent)) {
                        startActivity(intent);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}