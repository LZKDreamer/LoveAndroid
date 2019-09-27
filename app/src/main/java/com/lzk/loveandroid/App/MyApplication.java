package com.lzk.loveandroid.App;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.hjq.toast.ToastUtils;
import com.lzk.loveandroid.Utils.CommonUtil;
import com.lzk.loveandroid.Utils.SPUtil;
import com.lzk.loveandroid.main.SplashActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;

import okhttp3.OkHttpClient;

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
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        OkGo.getInstance().setOkHttpClient(builder.build()).init(this);
        ToastUtils.init(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        LitePal.initialize(this);

        //设置夜间模式
        if (CommonUtil.isNightMode()){
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static synchronized Context getContext(){
        return sContext;
    }
}
