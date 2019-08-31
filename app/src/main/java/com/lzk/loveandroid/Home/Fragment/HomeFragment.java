package com.lzk.loveandroid.Home.Fragment;


import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lzk.loveandroid.Base.BaseFragment;
import com.lzk.loveandroid.Home.Adapter.HomeArticleAdapter;
import com.lzk.loveandroid.Home.Bean.Home.HomeArticle;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Utils.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
    HomeArticleAdapter mHomeArticleAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initEventAndData() {
        OkGo.<String>get("https://www.wanandroid.com/article/list/0/json")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtil.d(TAG,result);
                        Gson gson = new Gson();
                        HomeArticle homeArticle = gson.fromJson(result,HomeArticle.class);
                        setRecyclerView(homeArticle);
                    }
                });
    }

    private void setRecyclerView(HomeArticle  homeArticle){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mHomeRv.setLayoutManager(layoutManager);
        mHomeArticleAdapter = new HomeArticleAdapter(R.layout.layout_home_item,homeArticle.getData().getDatas());
        mHomeRv.setAdapter(mHomeArticleAdapter);
    }

    @Override
    public void reload() {

    }

}
