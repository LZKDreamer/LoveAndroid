package com.lzk.loveandroid.App;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;

/**
 * @author LiaoZhongKai
 * @date 2019/8/30.
 */
public class MyApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        OkGo.getInstance().init(this);
    }

    public static synchronized Context getContext(){
        return sContext;
    }
}
