package com.lzk.loveandroid.Request;

/**
 * @author LiaoZhongKai
 * @date 2019/9/2.
 */
public interface IResultCallback {
    void onSuccess(Object object);
    void onFailure(int errCode,String errMsg);
    void onError();
}
