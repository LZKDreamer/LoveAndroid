package com.lzk.loveandroid.Home.Fragment;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzk.loveandroid.App.MyApplication;
import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.Home.Adapter.HomeArticleAdapter;
import com.lzk.loveandroid.Home.Bean.Home.HomeArticle;
import com.lzk.loveandroid.Home.Bean.Home.HomeBanner;
import com.lzk.loveandroid.Home.Bean.Home.HomeTopArticle;
import com.lzk.loveandroid.Loader.GlideImageLoader;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.LogUtil;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.lzk.loveandroid.main.ArticleDetailActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {
    public static final String TAG = "HomeFragment";

    @BindView(R.id.home_rv)
    RecyclerView mHomeRv;
    @BindView(R.id.home_refresh_layout)
    SmartRefreshLayout mHomeRefreshLayout;
    @BindView(R.id.page_content_view)
    FrameLayout mPageContentView;

    //RecyclerView
    private HomeArticleAdapter mHomeArticleAdapter;

    //Banner
    private Banner mBanner;
    private List<String> mBannerTitles = new ArrayList<>();//标题
    private List<String> mBannerImages = new ArrayList<>();//图片
    private List<String> mBannerUrls = new ArrayList<>();//链接

    private int curPage=0;//当前页数
    private boolean isRefresh = true;//是否是刷新列表

    private HomeTopArticle homeTopArticle;
    private HomeArticle homeArticle;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initEventAndData() {

        initRecyclerView();

        if (NetworkUtil.isNetworkConnected()){
            showLoadingLayout(null);
            requestHomeBanner();

        }else {
            showErrorLayout(getResources().getString(R.string.network_error));
        }
    }

    //获取Banner
    private void requestHomeBanner(){
        RequestCenter.requestHomeBanner(new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                setBanner((HomeBanner) object);
                requestHomeTopArticle();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showErrorLayout(errMsg);
            }

            @Override
            public void onError() {
                showErrorLayout(null);
            }
        });
    }

    //获取首页置顶文章
    private void requestHomeTopArticle(){
        RequestCenter.requestHomeTopArticel(new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                homeTopArticle = (HomeTopArticle) object;
                requestHomeArticle(homeTopArticle,curPage);
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showErrorLayout(errMsg);
            }

            @Override
            public void onError() {
                showErrorLayout(null);
            }
        });
    }

    //获取首页文章
    private void requestHomeArticle(HomeTopArticle homeTopArticle,int page){
        RequestCenter.requestHomeArticle(page, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                if (isRefresh){
                    homeArticle = (HomeArticle) object;
                }else {
                    homeArticle.getData().getDatas().addAll(((HomeArticle) object).getData().getDatas());
                }
                updateRecyclerView(homeTopArticle , homeArticle);
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showErrorLayout(errMsg);
            }

            @Override
            public void onError() {
                showErrorLayout(null);
            }
        });
    }

    /**
     * 收藏文章
     * @param id
     */
    private void requestCollectArticle(String id,int position){
        RequestCenter.requestCollectArticle(id, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                showToastInCenter(getString(R.string.collect_success));
                homeArticle.getData().getDatas().get(position).setCollect(true);
                mHomeArticleAdapter.notifyItemChanged(position+1);
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showToastInCenter(errMsg);
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 取消收藏
     * @param id
     */
    private void requestUnCollectArticle(String id,int position){
        RequestCenter.requestUnCollectArticle(id, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                showToastInCenter(getString(R.string.uncollect_success));
                homeArticle.getData().getDatas().get(position).setCollect(false);
                mHomeArticleAdapter.notifyItemChanged(position+1);
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                showToastInCenter(errMsg);
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 初始化列表
     */
    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mHomeRv.setLayoutManager(layoutManager);
        mHomeArticleAdapter = new HomeArticleAdapter(R.layout.layout_home_item,null);
        mHomeArticleAdapter.addHeaderView(initBanner());
        mHomeArticleAdapter.openLoadAnimation();
        mHomeRv.setAdapter(mHomeArticleAdapter);

        //上拉加载/下拉刷新监听
        mHomeRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                curPage++;
                requestHomeTopArticle();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                curPage = 0;
                requestHomeBanner();
            }
        });

        //点击事件
        mHomeArticleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.article_item_collect_iv:
                        HomeArticle.Datas item = homeArticle.getData().getDatas().get(position);
                        if (item.isCollect()){
                            requestUnCollectArticle(item.getId()+"",position);
                        }else {
                            requestCollectArticle(item.getId()+"",position);
                        }
                        break;
                }
            }
        });

        mHomeArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = ArticleDetailActivity.newIntent(getActivity(),homeArticle.getData().getDatas().get(position).getTitle(),
                        homeArticle.getData().getDatas().get(position).getLink());
                startActivity(intent);
            }
        });
    }

    private void updateRecyclerView(HomeTopArticle homeTopArticle , HomeArticle homeArticle){
        if (isRefresh){
            List<HomeArticle.Datas> datasList = homeTopArticle.getData();
            if (datasList != null){
                homeArticle.getData().getDatas().addAll(0,datasList);
                mHomeArticleAdapter.replaceData(homeArticle.getData().getDatas());
            }
            mHomeRefreshLayout.finishRefresh();
        }else {
            mHomeArticleAdapter.addData(homeArticle.getData().getDatas());
            mHomeRefreshLayout.finishLoadMore();
        }
        showPageContent();
    }

    /**
     * 初始化Banner
     * @return
     */
    private Banner initBanner(){
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.layout_home_banner,null);
        mBanner = linearLayout.findViewById(R.id.home_banner);
        linearLayout.removeView(mBanner);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mBannerUrls != null && mBannerUrls.size()>position){
                    Intent intent = ArticleDetailActivity.newIntent(getActivity(),mBannerTitles.get(position),
                            mBannerUrls.get(position));
                    startActivity(intent);
                }
            }
        });
        return mBanner;
    }

    private void setBanner(HomeBanner homeBanner){
        if (isRefresh){
            if (homeBanner.getData() == null){
                return;
            }
            mBannerImages.clear();
            mBannerTitles.clear();
            mBannerUrls.clear();

            for (HomeBanner.Banner banner : homeBanner.getData()){
                mBannerImages.add(banner.getImagePath());
                mBannerUrls.add(banner.getUrl());
                mBannerTitles.add(banner.getTitle());
            }

            if (mBanner != null){
                mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                mBanner.setImageLoader(new GlideImageLoader());
                mBanner.setImages(mBannerImages);
                mBanner.setBannerAnimation(Transformer.DepthPage);
                mBanner.setBannerTitles(mBannerTitles);
                mBanner.setDelayTime(3*1000);
                mBanner.isAutoPlay(true);
                mBanner.start();
            }
        }
    }

    @Override
    public void reload() {
        showLoadingLayout(null);
        requestHomeBanner();
    }

    /**
     * 回到顶部
     */
    public void backToTop(){
        if (mHomeRv != null){
            mHomeRv.smoothScrollToPosition(0);
        }
    }

}
