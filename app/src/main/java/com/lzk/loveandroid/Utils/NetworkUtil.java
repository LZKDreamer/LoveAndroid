package com.lzk.loveandroid.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.lzk.loveandroid.App.MyApplication;

/**
 * @author LiaoZhongKai
 * @date 2019/8/30.
 */
public class NetworkUtil {
    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
