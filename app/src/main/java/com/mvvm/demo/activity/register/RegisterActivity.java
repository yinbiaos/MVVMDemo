package com.mvvm.demo.activity.register;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.mvvm.demo.BaseActivity;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.login.LoginActivity;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.widget.TitleBarLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Administrator
 */
public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    @BindView(R.id.title_bar)
    TitleBarLayout mTitleBar;

    @BindView(R.id.et_username)
    EditText mEtUsername;

    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_password2)
    EditText mEtPassword2;
    private String username;
    private String password;

    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewAndData();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }


    protected void initViewAndData() {
        mTitleBar.setTitleBarBackgroundColor(getResources().getColor(R.color.c_6c8cff));
        mTitleBar.setTitleColor(getResources().getColor(R.color.c_ffffff));
        mTitleBar.setTitle("注册");
    }

    @OnClick({R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //登录按钮
            case R.id.bt_register:
                viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
                register();
                break;
            default:
                break;
        }
    }

    private void register() {
        username = mEtUsername.getText().toString().trim();
        password = mEtPassword.getText().toString().trim();
        String password2 = mEtPassword2.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showShort("请输入用户名");
        } else if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码");
        } else if (password.length() < 6) {
            ToastUtils.showShort("密码的字符长度至少为6位");
        } else if (TextUtils.isEmpty(password2)) {
            ToastUtils.showShort("请再次输入密码");
        } else if (!password.equals(password2)) {
            ToastUtils.showShort("两次输入的密码不一样请重新输入");
            mEtPassword.setText("");
            mEtPassword2.setText("");
        } else {
            viewModel.postRegister(username, password, password2);
            viewModel.getResult().observe(this, (ResponseBean responseBean) -> {
                if (responseBean == null) {
                    return;
                }
                if (responseBean.getErrorCode() == 0) {
                    ToastUtils.showShort("注册成功");
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showShort(responseBean.getErrorMsg());
                }
            });
        }
    }
}
