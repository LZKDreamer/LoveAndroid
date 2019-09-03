package com.lzk.loveandroid.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lzk.loveandroid.App.AppConstant;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.EventBus.Event;
import com.lzk.loveandroid.EventBus.EventConstant;
import com.lzk.loveandroid.EventBus.EventUtil;
import com.lzk.loveandroid.Home.Bean.Home.HomeArticle;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Register.RegisterActivity;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.lzk.loveandroid.Utils.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_back_iv)
    ImageView mLoginBackIv;
    @BindView(R.id.login_username_et)
    EditText mLoginUsernameEt;
    @BindView(R.id.login_psw_et)
    EditText mLoginPswEt;
    @BindView(R.id.login_login_btn)
    Button mLoginLoginBtn;
    @BindView(R.id.login_register_btn)
    Button mLoginRegisterBtn;

    public static final int REQUEST_CODE=1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void reload() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this,R.color.colorWhite));
        StatusBarUtil.setTransparent(this);
    }

    @OnClick({R.id.login_back_iv, R.id.login_login_btn, R.id.login_register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_back_iv:
                finish();
                break;
            case R.id.login_login_btn:
                String username = mLoginUsernameEt.getText().toString().trim();
                String password = mLoginPswEt.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    showToastInCenter(getResources().getString(R.string.username_or_psw_not_empty));
                }else {
                    if (NetworkUtil.isNetworkConnected()){
                        login(username,password);
                    }else {
                        showToastInCenter(getResources().getString(R.string.network_error));
                    }
                }

                break;
            case R.id.login_register_btn:
                Intent register = new Intent(this, RegisterActivity.class);
                startActivityForResult(register,REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            finish();
        }
    }

    private void login(String username,String psw){
        RequestCenter.requestLogin(username, psw, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                SPUtil.getInstance().putString(AppConstant.USERNAME,username);
                SPUtil.getInstance().putString(AppConstant.PASSWORD,psw);
                SPUtil.getInstance().putBoolean(AppConstant.USER_LOGIN_STATUS,true);
                showToastInCenter(getResources().getString(R.string.login_success));
                EventUtil.sendEvent(new Event(EventConstant.TYPE_LOGIN,username));
                finish();
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
}
