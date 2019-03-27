package com.mvvm.demo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.base.lib.Logs;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

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

    protected QMUITipDialog tipDialog;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mContext = this;
        //View注解框架绑定
        unbinder = ButterKnife.bind(this);
        App.getInstance().addContext(this);
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
        Logs.d(TAG, "onDestroy:" + mContext.toString());
        if (unbinder != null) {
            //ButterKnife解绑
            unbinder.unbind();
        }

        if (tipDialog != null) {
            tipDialog.cancel();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(this);
        }
        App.getInstance().removeContext(this);
    }

    /**
     * show tips
     *
     * @param tips 提示语
     * @see com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.IconType
     */
    protected void showTipsDialog(@QMUITipDialog.Builder.IconType int tipType, String tips) {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(tips)
                .create();
        tipDialog.show();
        handler.postDelayed(tipDialog::cancel, 1500);
    }

}
