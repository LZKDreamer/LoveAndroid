package com.lzk.loveandroid.Utils;

import com.lzk.loveandroid.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * @author LiaoZhongKai
 * @date 2019/8/30.
 */
public class LogUtil {
    private static boolean isDebug = BuildConfig.DEBUG;

    public static void d(String tag,String msg){
        if (isDebug){
            Logger.t(tag).d(msg);
        }
    }

    public static void e(String tag,String msg){
        if (isDebug){
            Logger.t(tag).e(msg);
        }
    }

    public static void json(String json){
        Logger.json(json);
    }
}
