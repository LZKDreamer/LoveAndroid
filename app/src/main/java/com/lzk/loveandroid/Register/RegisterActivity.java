package com.lzk.loveandroid.Register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lzk.loveandroid.App.AppConstant;
import com.lzk.loveandroid.Base.BaseActivity;
import com.lzk.loveandroid.EventBus.Event;
import com.lzk.loveandroid.EventBus.EventConstant;
import com.lzk.loveandroid.EventBus.EventUtil;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Request.IResultCallback;
import com.lzk.loveandroid.Request.RequestCenter;
import com.lzk.loveandroid.Utils.NetworkUtil;
import com.lzk.loveandroid.Utils.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.register_back_iv)
    ImageView mRegisterBackIv;
    @BindView(R.id.register_email_et)
    EditText mRegisterEmailEt;
    @BindView(R.id.register_psw_et)
    EditText mRegisterPswEt;
    @BindView(R.id.register_repeat_psw_et)
    EditText mRegisterRepeatPswEt;
    @BindView(R.id.register_btn)
    Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.common_bg));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void reload() {

    }

    @OnClick({R.id.register_back_iv, R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_back_iv:
                finish();
                break;
            case R.id.register_btn:
                String email = mRegisterEmailEt.getText().toString().trim();
                String psw = mRegisterPswEt.getText().toString().trim();
                String repsw = mRegisterRepeatPswEt.getText().toString().trim();
                if (NetworkUtil.isNetworkConnected()){
                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(psw) || TextUtils.isEmpty(repsw)){
                        showToastInCenter(getString(R.string.email_psw_not_empty));
                    }else if (!psw.equals(repsw)){
                        showToastInCenter(getString(R.string.password_repeat_incorrect));
                    }else {
                        register(email,psw,repsw);
                    }
                }else {
                    showToastInCenter(getString(R.string.network_error));
                }
                break;
        }
    }

    /**
     * 注册
     * @param username
     * @param password
     * @param repasssowrd
     */
    private void register(String username,String password,String repasssowrd){
        RequestCenter.requestRegister(username, password, repasssowrd, new IResultCallback() {
            @Override
            public void onSuccess(Object object) {
                showToastInCenter(getString(R.string.register_success));
                setResult(RESULT_OK,null);
                SPUtil.getInstance().putBoolean(AppConstant.USER_LOGIN_STATUS,true);
                SPUtil.getInstance().putString(AppConstant.USERNAME,username);
                SPUtil.getInstance().putString(AppConstant.PASSWORD,password);
                EventUtil.sendEvent(new Event(EventConstant.TYPE_REGISTER,username));
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
