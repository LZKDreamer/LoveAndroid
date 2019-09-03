package com.lzk.loveandroid.Utils;

import android.content.Context;

/**
 * @author LiaoZhongKai
 * @date 2019/9/3.
 */
public class CommonUtil {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }
}
