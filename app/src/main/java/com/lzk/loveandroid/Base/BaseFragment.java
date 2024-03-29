package com.lzk.loveandroid.Base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hjq.toast.ToastUtils;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Utils.LogUtil;

import java.util.PrimitiveIterator;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author LiaoZhongKai
 * @date 2019/8/30.
 */
public abstract class BaseFragment extends Fragment implements IBaseView, View.OnClickListener {

    /**
     * 页面加载状态
     */
    private static final int NORMAL_STATE = 0;
    private static final int LOADING_STATE = 1;
    private static final int ERROR_STATE = 2;
    private static final int EMPTY_STATE = 3;
    private int currentState = NORMAL_STATE;//当前状态
    /**
     * 状态View
     */
    private View mErrorView;
    private View mLoadingView;
    private View mEmptyView;
    //状态View要覆盖的内容View,它的父控件必须是FrameLayout/RelativeLayout/CoordinatorLayout
    private ViewGroup mPageContentView;

    private TextView mLoadingContentTv;
    private TextView mEmptyContentTv;
    private TextView mErrorContentTv;

    private Unbinder unBinder;

    //懒加载
    /**
     * 是否初始化过布局
     */
    protected boolean isViewInitiated;
    /**
     * 是否加载过数据
     */
    protected boolean isDataInitiated;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(getLayoutId(), container,false);
        unBinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initStateView();
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated=true;
        //加载数据
        lazyLoadData();
    }

    private void initStateView(){
        if (getView() == null) {
            return;
        }
        mPageContentView = (ViewGroup) getView().findViewById(R.id.page_content_view);
        if (mPageContentView != null) {
            if (!(mPageContentView.getParent() instanceof ViewGroup)) {
                throw new IllegalStateException(
                        "mPageContentView's ParentView should be a ViewGroup.");
            }
            ViewGroup mParent = (ViewGroup) mPageContentView.getParent();
            View.inflate(getActivity(), R.layout.layout_loading_view, mParent);
            View.inflate(getActivity(), R.layout.layout_error_view, mParent);
            View.inflate(getActivity(),R.layout.layout_empty_view,mParent);
            mLoadingView = mParent.findViewById(R.id.loading_group_fl);
            mEmptyView = mParent.findViewById(R.id.empty_group_fl);
            mErrorView = mParent.findViewById(R.id.error_group_fl);
            mLoadingContentTv = mLoadingView.findViewById(R.id.loading_tip_tv);
            mEmptyContentTv = mEmptyView.findViewById(R.id.empty_tip_tv);
            mErrorContentTv = mErrorView.findViewById(R.id.error_tip_tv);

            //重新加载按钮的点击事件
            Button mErrorBtn = mErrorView.findViewById(R.id.error_reload_btn);
            mErrorBtn.setOnClickListener(this);
            Button mEmptyBtn = mEmptyView.findViewById(R.id.empty_reload_btn);
            mEmptyBtn.setOnClickListener(this);

            //初始化状态显示
            mLoadingView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mPageContentView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void showLoadingLayout(String content) {
        if (currentState == LOADING_STATE || mLoadingView == null) {
            return;
        }
        hideCurrentView();
        currentState = LOADING_STATE;
        if (mLoadingView.getVisibility() == View.GONE){
            mLoadingView.setVisibility(View.VISIBLE);
        }
        if (content != null && !content.equals("")){
            mLoadingContentTv.setText(content);
        }else {
            mLoadingContentTv.setText(getResources().getString(R.string.loading));
        }
    }

    @Override
    public void showEmptyLayout(String content) {
        if (currentState == EMPTY_STATE){
            return;
        }
        hideCurrentView();
        currentState = EMPTY_STATE;
        if (mEmptyView.getVisibility() == View.GONE){
            mEmptyView.setVisibility(View.VISIBLE);
        }
        if (content != null && !content.equals("")){
            mEmptyContentTv.setText(content);
        }else {
            mEmptyContentTv.setText(getResources().getString(R.string.empty));
        }
    }

    @Override
    public void showErrorLayout(String content) {
        if (currentState == ERROR_STATE){
            return;
        }
        hideCurrentView();
        currentState = ERROR_STATE;
        if (mErrorView.getVisibility() == View.GONE){
            mErrorView.setVisibility(View.VISIBLE);
        }
        if (content != null && !content.equals("")){
            mErrorContentTv.setText(content);
        }else {
            mErrorContentTv.setText(getResources().getString(R.string.error));
        }
    }

    @Override
    public void showPageContent() {
        if (currentState == NORMAL_STATE) {
            return;
        }
        hideCurrentView();
        currentState = NORMAL_STATE;
        if (mPageContentView.getVisibility() ==View.INVISIBLE){
            mPageContentView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏当前显示的View
     */
    private void hideCurrentView() {
        switch (currentState) {
            case NORMAL_STATE:
                if (mPageContentView == null) {
                    return;
                }
                if (mPageContentView.getVisibility() == View.VISIBLE){
                    mPageContentView.setVisibility(View.INVISIBLE);
                }
                break;
            case LOADING_STATE:
                if (mLoadingView.getVisibility() == View.VISIBLE){
                    mLoadingView.setVisibility(View.GONE);
                }
                break;
            case ERROR_STATE:
                if (mErrorView.getVisibility() == View.VISIBLE){
                    mErrorView.setVisibility(View.GONE);
                }
                break;
            case EMPTY_STATE:
                if (mEmptyView.getVisibility() == View.VISIBLE){
                    mEmptyView.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 加载布局文件
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化事件和数据
     */
    protected abstract void initEventAndData();

    /**
     * 重新加载
     */
    protected void reload(){}

    /**
     * 懒加载数据
     */
    private void lazyLoadData(){
        if (getUserVisibleHint() && isViewInitiated && !isDataInitiated) {
            initEventAndData();
            isDataInitiated = true;//不再重复加载
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoadData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.error_reload_btn:
            case R.id.empty_reload_btn:
                reload();
                break;
        }
    }

    protected void showToastInCenter(String content){
        ToastUtils.setGravity(Gravity.CENTER,0,0);
        ToastUtils.show(content);
    }
}
