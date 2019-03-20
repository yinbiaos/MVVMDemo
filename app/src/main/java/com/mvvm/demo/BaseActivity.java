package com.mvvm.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    protected Context mContext;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mContext = this;
//        setStatusBar();
        //View注解框架绑定
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 设置ContetView的布局
     *
     * @return int
     */
    protected abstract int getContentView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            //ButterKnife解绑
            unbinder.unbind();
        }
    }

//    /**
//     * 设置沉浸式状态栏
//     */
//    protected void setStatusBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //5.0及以上
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            //对状态栏单独设置颜色
//            getWindow().setStatusBarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //4.4到5.0
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
//    }

}
