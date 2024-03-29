package com.lzk.loveandroid.Utils;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzk.loveandroid.App.AppConstant;
import com.lzk.loveandroid.Base.BaseActivity;

import java.util.Random;

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

    /**
     * 获取标签随机背景色
     * @return
     */
    public static int getFlowTagBackgroudColor(){
        int randomNum = new Random().nextInt();
        int position = randomNum % AppConstant.TAG_COLORS.length;
        if (position < 0) {
            position = -position;
        }
        return AppConstant.TAG_COLORS[position];
    }

    /**
     * 是否是夜间模式
     * @return
     */
    public static boolean isNightMode(){
        return SPUtil.getInstance().getBoolean(AppConstant.NIGHT_MODE,false);
    }

}
