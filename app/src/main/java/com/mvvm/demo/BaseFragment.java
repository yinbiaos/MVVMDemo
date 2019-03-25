package com.mvvm.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 *
 * @author yinbiao
 * @date 2019/3/8
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mContext;
    protected Unbinder unbinder;
    protected QMUITipDialog progressDialog;
    protected QMUITipDialog tipDialog;
    private Handler handler = new Handler();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();

        progressDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载中...")
                .create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //View注解框架绑定
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        if (tipDialog != null) {
            tipDialog.cancel();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(this);
        }
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
