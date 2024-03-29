package com.lzk.loveandroid.App;

import androidx.core.content.ContextCompat;

import com.lzk.loveandroid.R;

/**
 * @author LiaoZhongKai
 * @date 2019/9/3.
 */
public class AppConstant {
    //用户信息
    public static final String USERNAME="username";
    public static final String PASSWORD="password";
    public static final String USER_ID="user_id";
    public static final String USER_LOGIN_STATUS="user_login_status";

    //常用网站标签背景色
    public static final int[] TAG_COLORS = new int[]{
            ContextCompat.getColor(MyApplication.getContext(),R.color.flow_tag_one),
            ContextCompat.getColor(MyApplication.getContext(),R.color.flow_tag_two),
            ContextCompat.getColor(MyApplication.getContext(),R.color.flow_tag_three),
            ContextCompat.getColor(MyApplication.getContext(),R.color.flow_tag_four),
            ContextCompat.getColor(MyApplication.getContext(),R.color.flow_tag_five),
    };

    //夜间模式
    public static final String NIGHT_MODE = "night_mode";
}
