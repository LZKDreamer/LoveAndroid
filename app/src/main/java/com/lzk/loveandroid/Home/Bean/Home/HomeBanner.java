package com.lzk.loveandroid.Home.Bean.Home;

import com.lzk.loveandroid.Home.Bean.Home.Banner;

import java.util.List;

/**
 * @author LiaoZhongKai
 * @date 2019/8/30.
 */
public class HomeBanner {
    private List<Banner> banners;
    private int errorCode;
    private String errorMsg;

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
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
