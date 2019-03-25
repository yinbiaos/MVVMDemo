package com.mvvm.demo.http;

import android.content.Intent;

import com.base.lib.Logs;
import com.mvvm.demo.App;
import com.mvvm.demo.activity.login.LoginActivity;
import com.mvvm.demo.entity.ResponseBean;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import io.reactivex.functions.Predicate;

/**
 * http消息过滤器
 *
 * @author yinbiao
 * @date 2019/3/25
 */
public class HttpFilter {

    /**
     * 创建登录消息过滤器
     *
     * @return Predicate
     */
    public static Predicate<ResponseBean> createLoginFilter() {
        return new Predicate<ResponseBean>() {
            @Override
            public boolean test(ResponseBean responseBean) throws Exception {
                if (responseBean.getErrorCode() == -1001) {
                    showMessagePositiveDialog();
                    throw new Exception(responseBean.getErrorMsg());
                }
                return true;
            }
        };
    }

    private static void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(App.getInstance().getTopContext())
                .setTitle("提示")
                .setMessage("请先登录！")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Intent intent = new Intent(App.getInstance().getTopContext(), LoginActivity.class);
                        App.getInstance().getTopContext().startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}
