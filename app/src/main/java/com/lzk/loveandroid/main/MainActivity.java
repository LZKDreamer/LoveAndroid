package com.lzk.loveandroid.main;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.jaeger.library.StatusBarUtil;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, R.color.colorPrimary));
        ButterKnife.bind(this);
        setSupportActionBar(mCommonToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        }
        mCommonToolbarTitleTv.setText(getResources().getString(R.string.tab_home));

        mMainBottomNavView.setSelectedItemId(R.id.tab_home);
        mMainNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.header_favorite://收藏

                        break;
                    case R.id.header_setting://设置

                        break;
                    case R.id.header_about_us://关于我们

                        break;
                    case R.id.header_username_tv://用户名

                        break;
                }
                return true;
            }
        });

        //BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mMainBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tab_home://首页

                        break;
                    case R.id.tab_knowledge://知识体系

                        break;
                    case R.id.tab_wx_article://微信公众号

                        break;
                    case R.id.tab_navigation://导航

                        break;
                    case R.id.tab_project://项目

                        break;
                }
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
        getMenuInflater().inflate(R.menu.common_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://菜单
                mMainDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.toolbar_website://常用网站

                break;
            case R.id.toolbar_search://搜索

                break;
        }
        return true;
    }
}
