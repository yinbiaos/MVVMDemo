package com.base.lib;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by biao.yin on 2018/4/26.
 * Toast辅助类
 *
 * @author yinbiao
 */

public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }
}
