package com.lzk.loveandroid.Request;

import com.google.gson.Gson;
import com.lzk.loveandroid.Home.Bean.Home.HomeArticle;
import com.lzk.loveandroid.Home.Bean.Home.HomeBanner;
import com.lzk.loveandroid.Home.Bean.Home.HomeTopArticle;
import com.lzk.loveandroid.Utils.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * @author LiaoZhongKai
 * @date 2019/9/2.
 * @function 所有网络请求都放在这里
 */
public class RequestCenter {

    private static Gson gson = new Gson();
    private static final String TAG = "RequestCenter";

    /**
     * 首页Banner
     * @param callback
     */
    public static void requestHomeBanner(IResultCallback callback){
        OkGo.<String>get("https://www.wanandroid.com/banner/json")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        HomeBanner homeBanner = gson.fromJson(response.body(),HomeBanner.class);
                        if (homeBanner.getErrorCode() == Constant.SUCCESS_CODE){
                            callback.onSuccess(homeBanner);
                        }else {
                            callback.onFailure(homeBanner.getErrorCode(),homeBanner.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null){
                            LogUtil.e(TAG,throwable.getMessage());
                        }
                    }
                });
    }

    /**
     * 首页文章
     * @param page 页数
     * @param callback
     */
    public static void requestHomeArticle(int page,IResultCallback callback){
        OkGo.<String>get("https://www.wanandroid.com/article/list/"+page+"/json")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        HomeArticle homeArticle = gson.fromJson(result,HomeArticle.class);
                        if (homeArticle.getErrorCode() == Constant.SUCCESS_CODE){
                            callback.onSuccess(homeArticle);
                        }else {
                            callback.onFailure(homeArticle.getErrorCode(),homeArticle.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null){
                            LogUtil.e(TAG,throwable.getMessage());
                        }
                    }
                });
    }

    /**
     * 首页置顶文章
     * @param callback
     */
    public static void requestHomeTopArticel(IResultCallback callback){
        OkGo.<String>get("https://www.wanandroid.com/article/top/json")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        HomeTopArticle homeTopArticle = gson.fromJson(response.body(),HomeTopArticle.class);
                        if (homeTopArticle.getErrorCode() == Constant.SUCCESS_CODE){
                            callback.onSuccess(homeTopArticle);
                        }else {
                            callback.onFailure(homeTopArticle.getErrorCode(),homeTopArticle.getErrorMsg());
                        }
                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null){
                            LogUtil.e(TAG,throwable.getMessage());
                        }
                    }
                });
    }

    /**
     * 收藏站内文章
     * @param articleId 文章id
     */
    public static void requestCollectArticle(String articleId,IResultCallback callback){
        OkGo.<String>post("https://www.wanandroid.com/lg/collect/"+articleId+"/json")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        HomeArticle homeArticle = gson.fromJson(response.body(),HomeArticle.class);
                        if (homeArticle.getErrorCode() == Constant.SUCCESS_CODE){
                            callback.onSuccess(null);
                        }else {
                            callback.onFailure(homeArticle.getErrorCode(),homeArticle.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null){
                            LogUtil.e(TAG,throwable.getMessage());
                        }
                    }
                });
    }

    /**
     * 取消收藏文章（文章列表中取消）
     * @param articleId
     * @param callback
     */
    public static void requestUnCollectArticle(String articleId,IResultCallback callback){
        OkGo.<String>post("https://www.wanandroid.com/lg/uncollect_originId/"+articleId+"/json")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        HomeArticle homeArticle = gson.fromJson(response.body(),HomeArticle.class);
                        if (homeArticle.getErrorCode() == Constant.SUCCESS_CODE){
                            callback.onSuccess(null);
                        }else {
                            callback.onFailure(homeArticle.getErrorCode(),homeArticle.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null){
                            LogUtil.e(TAG,throwable.getMessage());
                        }
                    }
                });
    }

    /**
     * 登录
     * @param username
     * @param password
     */
    public static void requestLogin(String username,String password,IResultCallback callback){
        OkGo.<String>post("https://www.wanandroid.com/user/login")
                .params("username",username)
                .params("password",password)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtil.d("RequestCenter",result);
                        HomeArticle homeArticle = gson.fromJson(result,HomeArticle.class);
                        if (homeArticle.getErrorCode() == Constant.SUCCESS_CODE){
                            callback.onSuccess(homeArticle);
                        }else {
                            callback.onFailure(homeArticle.getErrorCode(),homeArticle.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null){
                            LogUtil.e(TAG,throwable.getMessage());
                        }
                    }
                });
    }

    /**
     * 退出登录
     * @param callback
     */
    public static void requestLogout(IResultCallback callback){
        OkGo.<String>get("https://www.wanandroid.com/user/logout/json")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        HomeArticle homeArticle = gson.fromJson(response.body(),HomeArticle.class);
                        if (homeArticle.getErrorCode() == Constant.SUCCESS_CODE){
                            callback.onSuccess(homeArticle);
                        }else {
                            callback.onFailure(homeArticle.getErrorCode(),homeArticle.getErrorMsg());
                        }
                    }
                });
    }

    /**
     * 注册
     * @param username
     * @param psw
     * @param repeatPsw
     * @param callback
     */
    public static void requestRegister(String username,String psw,String repeatPsw,IResultCallback callback){
        OkGo.<String>post("https://www.wanandroid.com/user/register")
                .params("username",username)
                .params("password",psw)
                .params("repassword",repeatPsw)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        HomeArticle homeArticle = gson.fromJson(response.body(),HomeArticle.class);
                        if (homeArticle.getErrorCode() == Constant.SUCCESS_CODE){
                            callback.onSuccess(homeArticle);
                        }else {
                            callback.onFailure(homeArticle.getErrorCode(),homeArticle.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null){
                            LogUtil.e(TAG,throwable.getMessage());
                        }
                    }
                });
    }
}
