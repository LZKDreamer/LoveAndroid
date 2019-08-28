package com.lzk.loveandroid.main;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this,R.color.colorPrimary));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void reload() {

    }
}
