package com.lzk.loveandroid.Home.Bean.Home;

import java.util.List;

/**
 * @author LiaoZhongKai
 * @date 2019/9/2.
 */
public class HomeTopArticle {
    private List<HomeArticle.Datas> data;
    private int errorCode;
    private String errorMsg;

    public List<HomeArticle.Datas> getData() {
        return data;
    }

    public void setData(List<HomeArticle.Datas> data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
