package com.lzk.loveandroid.Base;

/**
 * @author LiaoZhongKai
 * @date 2019/8/28.
 */
public interface IBaseView {
    void showLoadingLayout(String content);
    void showEmptyLayout(String content);
    void showErrorLayout(String content);
    void showPageContent();
}
