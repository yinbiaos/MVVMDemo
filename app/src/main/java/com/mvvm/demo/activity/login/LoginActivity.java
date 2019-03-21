package com.mvvm.demo.activity.login;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.base.lib.SharedHelper;
import com.base.lib.ToastUtil;
import com.mvvm.demo.BaseActivity;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.MainActivity;
import com.mvvm.demo.activity.register.RegisterActivity;
import com.mvvm.demo.config.Constants;
import com.mvvm.demo.entity.LoginBean;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.widget.TitleBarLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hzy on 2019/1/18
 * LoginActivity  登录界面
 *
 * @author hzy
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.title_bar)
    TitleBarLayout mTitleBar;

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    private String username;
    private String password;

    Boolean isOtherToLogin = false;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewAndData();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    protected void initViewAndData() {
        /**
         * //从注册界面跳转过来直接记录账号密码
         */
        if (getIntent().getExtras() != null) {
            isOtherToLogin = true;
            username = getIntent().getStringExtra("username");
            password = getIntent().getStringExtra("password");
            mEtUsername.setText(username);
            mEtPassword.setText(password);
        }
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mTitleBar.setTitleBarBackgroundColor(getResources().getColor(R.color.c_6c8cff));
        mTitleBar.setTitleColor(getResources().getColor(R.color.c_ffffff));
        mTitleBar.setTitle("登录");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isOtherToLogin) {
            mEtUsername.setText("362070860@qq.com");
            mEtPassword.setText("123456");
        }
    }

    @OnClick({R.id.bt_login, R.id.bt_reset_password, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();//登录按钮
                break;
            case R.id.bt_reset_password:
                //重置密码
                Toast.makeText(this, "点击了重置密码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_register:
                //注册
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            default:
                break;
        }
    }

    private void login() {
        username = mEtUsername.getText().toString().trim();
        password = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showToast(mContext, "请输入用户名");
        } else if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(mContext, "请输入登录密码");
        } else {
            viewModel.postLogin(username, password);
            viewModel.getResult().observe(this, (ResponseBean<LoginBean> responseBean) -> {
                if (responseBean == null) {
                    return;
                }
                if (responseBean.getErrorCode() == 0) {
                    ToastUtil.showToast(mContext, "登录成功");
                    LoginBean loginBean = (LoginBean) responseBean.getData();
                    SharedHelper.getInstance().put(Constants.USERNAME, loginBean.getUsername());
                    SharedHelper.getInstance().put(Constants.ISLOGIN, true);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    ToastUtil.showToast(mContext, responseBean.getErrorMsg());
                }
            });
        }
    }


}
