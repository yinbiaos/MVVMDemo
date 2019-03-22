package com.mvvm.demo.adapter;

import android.content.Context;
import android.content.Intent;

import com.base.lib.SharedHelper;
import com.base.lib.ToastUtil;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.login.LoginActivity;
import com.mvvm.demo.config.Constants;
import com.mvvm.demo.entity.KnowledgeSystemChildBean;
import com.mvvm.demo.listener.OnCollectListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by hzy on 2019/1/24
 *
 * @author Administrator
 */
public class SubSysListAdapter extends CommonAdapter<KnowledgeSystemChildBean> {

    private OnCollectListener mOnCollectListener;

    public void setmOnCollectListener(OnCollectListener mOnCollectListener) {
        this.mOnCollectListener = mOnCollectListener;
    }

    public SubSysListAdapter(Context context, List<KnowledgeSystemChildBean> datas) {
        super(context, R.layout.item_pub_addr, datas);
    }

    @Override
    protected void convert(ViewHolder holder, KnowledgeSystemChildBean bean, int position) {
        holder.setText(R.id.tv_project, bean.getTitle()
                .replaceAll("&ldquo;", "\"").replaceAll(
                        "&rdquo;", "\""))
                .setText(R.id.tv_time, "时间:" + bean.getNiceDate())
                .setImageResource(R.id.imv_like, bean.isCollect() ?
                        R.drawable.icon_like :
                        R.drawable.icon_unlike)
                .setOnClickListener(R.id.imv_like, v -> {
                    if (!SharedHelper.getInstance().getBoolean(Constants.ISLOGIN, false)) {
                        ToastUtil.showToast(mContext, "请先登录");
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        return;
                    }
                    mOnCollectListener.onCollect(bean.isCollect(), bean.getId(), position);
                });
    }
}
