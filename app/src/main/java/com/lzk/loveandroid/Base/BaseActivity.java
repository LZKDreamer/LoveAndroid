package com.lzk.loveandroid.Base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.lzk.loveandroid.EventBus.Event;
import com.lzk.loveandroid.EventBus.EventUtil;
import com.lzk.loveandroid.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author LiaoZhongKai
 * @date 2019/8/28.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, IBaseView {

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

    //日志


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPageContentView = (ViewGroup) findViewById(R.id.page_content_view);
        if (mPageContentView != null) {
            if (!(mPageContentView.getParent() instanceof ViewGroup)) {
                throw new IllegalStateException(
                        "mPageContentView's ParentView should be a ViewGroup.");
            }
            ViewGroup mParent = (ViewGroup) mPageContentView.getParent();
            View.inflate(this, R.layout.layout_loading_view, mParent);
            View.inflate(this, R.layout.layout_error_view, mParent);
            View.inflate(this,R.layout.layout_empty_view,mParent);
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

        if (isRegisterEventBus()) {
            EventUtil.register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventUtil.unregister(this);
        }
    }

    /**
     * 加载布局文件
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 重新加载
     */
    public abstract void reload();

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBusReceive(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(Event event) {

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

    /**
     * 隐藏当前显示的View
     */
    public void hideCurrentView() {
        switch (currentState) {
            case NORMAL_STATE:
                if (mPageContentView == null) {
                    return;
                }
                mPageContentView.setVisibility(View.INVISIBLE);
                break;
            case LOADING_STATE:
                mLoadingView.setVisibility(View.GONE);
                break;
            case ERROR_STATE:
                mErrorView.setVisibility(View.GONE);
                break;
            case EMPTY_STATE:
                mEmptyView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    @Override
    public void showEmptyLayout(String content) {
        if (currentState == EMPTY_STATE){
            return;
        }
        hideCurrentView();
        currentState = EMPTY_STATE;
        mEmptyView.setVisibility(View.VISIBLE);
        if (content != null && !content.equals("")){
            mEmptyContentTv.setText(content);
        }else {
            mEmptyContentTv.setText(getResources().getString(R.string.empty));
        }
    }

    @Override
    public void showLoadingLayout(String content) {
        if (currentState == LOADING_STATE || mLoadingView == null) {
            return;
        }
        hideCurrentView();
        currentState = LOADING_STATE;
        mLoadingView.setVisibility(View.VISIBLE);
        if (content != null && !content.equals("")){
            mLoadingContentTv.setText(content);
        }else {
            mLoadingContentTv.setText(getResources().getString(R.string.loading));
        }
    }

    @Override
    public void showErrorLayout(String content) {
        if (currentState == ERROR_STATE){
            return;
        }
        hideCurrentView();
        currentState = ERROR_STATE;
        mErrorView.setVisibility(View.VISIBLE);
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
        mPageContentView.setVisibility(View.VISIBLE);
    }

    protected void showToastInCenter(String content){
        ToastUtils.setGravity(Gravity.CENTER,0,0);
        ToastUtils.show(content);
    }
}
