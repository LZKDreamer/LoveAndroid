package com.lzk.loveandroid.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.jaeger.library.StatusBarUtil;
import com.lzk.loveandroid.App.AppConstant;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.CommonWeb.Activity.CommonWebActivity;
import com.lzk.loveandroid.CustomView.CommonDialog;
import com.lzk.loveandroid.EventBus.Event;
import com.lzk.loveandroid.EventBus.EventConstant;
import com.lzk.loveandroid.EventBus.EventUtil;
import com.lzk.loveandroid.Home.Fragment.HomeFragment;
import com.lzk.loveandroid.Knowledge.Fragment.KnowledgeFragment;
import com.lzk.loveandroid.Login.LoginActivity;
import com.lzk.loveandroid.Navigation.Fragment.NavFragment;
import com.lzk.loveandroid.Project.Fragment.ProjectFragment;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Search.SearchActivity;
import com.lzk.loveandroid.Search.SearchHistoryAdapter;
import com.lzk.loveandroid.Utils.SPUtil;
import com.lzk.loveandroid.wx.Fragment.WXFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    //UI
    @BindView(R.id.common_toolbar_title_tv)
    TextView mCommonToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar mCommonToolbar;
    @BindView(R.id.main_fragment_container_fl)
    FrameLayout mMainFragmentContainerFl;
    @BindView(R.id.main_float_btn)
    FloatingActionButton mMainFloatBtn;
    @BindView(R.id.main_bottom_nav_view)
    BottomNavigationView mMainBottomNavView;
    @BindView(R.id.main_nav_view)
    NavigationView mMainNavView;
    @BindView(R.id.main_drawer_layout)
    DrawerLayout mMainDrawerLayout;
    private TextView mUsernameTv;

    //Fragment
    private HomeFragment mHomeFragment;
    private KnowledgeFragment mKnowledgeFragment;
    private WXFragment mWXFragment;
    private NavFragment mNavFragment;
    private ProjectFragment mProjectFragment;
    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.colorPrimary));
        ButterKnife.bind(this);
        setSupportActionBar(mCommonToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        }
        mCommonToolbarTitleTv.setText(getResources().getString(R.string.tab_home));

        //Fragment
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        setDefaultFragment();

        //DrawerNav
        mUsernameTv = mMainNavView.getHeaderView(0).findViewById(R.id.header_username_tv);
        if (SPUtil.getInstance().getBoolean(AppConstant.USER_LOGIN_STATUS,false)){
            mMainNavView.getMenu().findItem(R.id.header_logout).setVisible(true);
            mUsernameTv.setText(SPUtil.getInstance().getString(AppConstant.USERNAME,""));
        }else {
            mMainNavView.getMenu().findItem(R.id.header_logout).setVisible(false);
        }
        mMainBottomNavView.setSelectedItemId(R.id.tab_home);
        mMainNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.header_favorite://收藏
                        //mMainDrawerLayout.closeDrawers();
                        break;
                    case R.id.header_setting://设置
                        //mMainDrawerLayout.closeDrawers();
                        break;
                    case R.id.header_about_us://关于我们
                        //mMainDrawerLayout.closeDrawers();
                        break;
                    case R.id.header_logout:
                        CommonDialog dialog = new CommonDialog(MainActivity.this, getString(R.string.prompt)
                                , getString(R.string.is_logout), getString(R.string.sure_logout), getString(R.string.cancel),
                                new CommonDialog.DialogClickListener() {
                                    @Override
                                    public void onDialogClick() {
                                        logout();
                                    }
                                });
                        dialog.show();
                        break;
                }
                return true;
            }
        });
        mUsernameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SPUtil.getInstance().getBoolean(AppConstant.USER_LOGIN_STATUS,false)){
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            }
        });

        //BottomNav
        //BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mMainBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                hideFragment(transaction);
                switch (menuItem.getItemId()) {
                    case R.id.tab_home://首页
                        mCommonToolbarTitleTv.setText(getResources().getString(R.string.tab_home));
                        if (mHomeFragment == null) {
                            mHomeFragment = new HomeFragment();
                            transaction.add(R.id.main_fragment_container_fl, mHomeFragment);
                        } else {
                            transaction.show(mHomeFragment);
                        }
                        break;
                    case R.id.tab_knowledge://知识体系
                        mCommonToolbarTitleTv.setText(getResources().getString(R.string.tab_knowledge_system));
                        if (mKnowledgeFragment == null) {
                            mKnowledgeFragment = new KnowledgeFragment();
                            transaction.add(R.id.main_fragment_container_fl, mKnowledgeFragment);
                        } else {
                            transaction.show(mKnowledgeFragment);
                        }

                        break;
                    case R.id.tab_wx_article://微信公众号
                        mCommonToolbarTitleTv.setText(getResources().getString(R.string.tab_wx_article));
                        if (mWXFragment == null) {
                            mWXFragment = new WXFragment();
                            transaction.add(R.id.main_fragment_container_fl, mWXFragment);
                        } else {
                            transaction.show(mWXFragment);
                        }
                        break;
                    case R.id.tab_navigation://导航
                        mCommonToolbarTitleTv.setText(getResources().getString(R.string.tab_navigation));
                        if (mNavFragment == null) {
                            mNavFragment = new NavFragment();
                            transaction.add(R.id.main_fragment_container_fl, mNavFragment);
                        } else {
                            transaction.show(mNavFragment);
                        }
                        break;
                    case R.id.tab_project://项目
                        mCommonToolbarTitleTv.setText(getResources().getString(R.string.tab_project));
                        if (mProjectFragment == null) {
                            mProjectFragment = new ProjectFragment();
                            transaction.add(R.id.main_fragment_container_fl, mProjectFragment);
                        } else {
                            transaction.show(mProjectFragment);
                        }
                        break;
                    default:
                        break;
                }
                transaction.commit();
                return true;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://菜单
                mMainDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.toolbar_website://常用网站
                    Intent webIntent = new Intent(this, CommonWebActivity.class);
                    startActivity(webIntent);
                break;
            case R.id.toolbar_search://搜索
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);

                break;
        }
        return true;
    }

    private void setDefaultFragment() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            mTransaction.add(R.id.main_fragment_container_fl, mHomeFragment);
        }
        mTransaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mKnowledgeFragment != null) {
            transaction.hide(mKnowledgeFragment);
        }
        if (mWXFragment != null) {
            transaction.hide(mWXFragment);
        }
        if (mNavFragment != null) {
            transaction.hide(mNavFragment);
        }
        if (mProjectFragment != null) {
            transaction.hide(mProjectFragment);
        }
    }

    @OnClick(R.id.main_float_btn)
    public void onViewClicked() {
        backToTop();
    }

    private void backToTop(){
        if (mHomeFragment != null){
            if (mHomeFragment.isVisible()){
                mHomeFragment.backToTop();
                return;
            }
        }
        if (mKnowledgeFragment != null){
            if (mKnowledgeFragment.isVisible()){
                mKnowledgeFragment.backToTop();
            }
        }


    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getType()){
            case EventConstant.TYPE_REGISTER:
            case EventConstant.TYPE_LOGIN:
                mUsernameTv.setText((String)event.getData());
                mMainNavView.getMenu().findItem(R.id.header_logout).setVisible(true);
                break;
            case EventConstant.TYPE_LOGOUT:
                mUsernameTv.setText(getString(R.string.login));
                mMainNavView.getMenu().findItem(R.id.header_logout).setVisible(false);
                SPUtil.getInstance().remove(AppConstant.USERNAME);
                SPUtil.getInstance().remove(AppConstant.PASSWORD);
                SPUtil.getInstance().putBoolean(AppConstant.USER_LOGIN_STATUS,false);
                break;

        }

    }

    /**
     * 退出登录
     */
    private void logout(){
        RequestCenter.requestLogout(new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                EventUtil.sendEvent(new Event(EventConstant.TYPE_LOGOUT));
            }

            @Override
            public void onFailure(int errCode, String errMsg) {

            }

            @Override
            public void onError() {

            }
        });
    }
}
