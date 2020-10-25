package com.example.dyjw_mq.ui.update;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dyjw_mq.R;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class updateFragment extends Fragment {

    private updateViewModel updateViewModel;
    private TextView tv;
    private TextView title;
    private TextView version;
    private TextView size;
    private TextView time;
    private TextView content;
    private Button cancel;
    private TextView info;
    private Button start;
    private String logs = "updateFragment_logs";

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        updateViewModel =
                ViewModelProviders.of(this).get(updateViewModel.class);
        View root = inflater.inflate(R.layout.activity_upgrade, container, false);

        tv = root.findViewById(R.id.tv);
        title = root.findViewById(R.id.title);
        version = root.findViewById(R.id.version);
        size = root.findViewById(R.id.size);
        time = root.findViewById(R.id.time);
        content = root.findViewById(R.id.content);
        cancel = root.findViewById(R.id.cancel);
        start = root.findViewById(R.id.start);
        info = root.findViewById(R.id.info);
        if (!(Beta.getStrategyTask() == null)) {
            updateBtn(Beta.getStrategyTask());
            tv.setText(tv.getText().toString() + Beta.getStrategyTask().getSavedLength() + "");
            title.setText(title.getText().toString() + Beta.getUpgradeInfo().title);
            version.setText(version.getText().toString() + Beta.getUpgradeInfo().versionName);
            size.setText(size.getText().toString() + readableFileSize(Beta.getUpgradeInfo().fileSize) + "");

            String res;
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date(Beta.getUpgradeInfo().publishTime);

            res = simpleDateFormat.format(date);

            time.setText(time.getText().toString() + res + "");

            content.setText(Beta.getUpgradeInfo().newFeature);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadTask task = Beta.startDownload();
                    updateBtn(task);
//                if (task.getStatus() == DownloadTask.DOWNLOADING) {
//                    finish();
//                }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Beta.cancelDownload();
                    //跳转fragment
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_home);
                }
            });

            Beta.registerDownloadListener(new DownloadListener() {
                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                @Override
                public void onReceive(DownloadTask task) {
                    updateBtn(task);
                    Log.d(logs, String.valueOf(task.getSavedLength()));
                    Log.d(logs, String.valueOf(Beta.getUpgradeInfo().fileSize));
                    tv.setText("进度：" + String.format("%.2f", (task.getSavedLength() * 100.0) / Beta.getUpgradeInfo().fileSize) + "%");
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onCompleted(DownloadTask task) {
                    updateBtn(task);
                    tv.setText("进度：" + task.getSavedLength() + "");
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onFailed(DownloadTask task, int code, String extMsg) {
                    updateBtn(task);
                    tv.setText("failed");

                }
            });
        } else {
            tv.setText("");
            title.setText("暂无更新");
            version.setText("");
            size.setText("");
            time.setText("");
            info.setText("");
            content.setText("");
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Beta.cancelDownload();
                    //跳转fragment
//                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
//                    navController.navigate(R.id.nav_home);
                    requireActivity().onBackPressed();//销毁自己
                }
            });
        }


        return root;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void updateBtn(DownloadTask task) {
        switch (task.getStatus()) {
            case DownloadTask.INIT:
            case DownloadTask.DELETED:
            case DownloadTask.FAILED: {
                start.setText("开始下载");
            }
            break;
            case DownloadTask.COMPLETE: {
                start.setText("安装");
            }
            break;
            case DownloadTask.DOWNLOADING: {
                start.setText("暂停");
            }
            break;
            case DownloadTask.PAUSED: {
                start.setText("继续下载");
            }
            break;
            default:
                break;
        }
    }
}