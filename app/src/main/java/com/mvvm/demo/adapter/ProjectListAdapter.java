package com.mvvm.demo.adapter;

import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.login.LoginActivity;
import com.mvvm.demo.config.Constants;
import com.mvvm.demo.entity.ProjectListBean;
import com.mvvm.demo.listener.OnCollectListener;
import com.mvvm.demo.utils.SharedPreferencesUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by hzy on 2019/1/24
 * ProjectListAdapter  项目列表
 *
 * @author Administrator
 */
public class ProjectListAdapter extends CommonAdapter<ProjectListBean.DatasBean> {

    private Context mContext;
    private OnCollectListener mOnCollectListener;

    public void setmOnCollectListener(OnCollectListener mOnCollectListener) {
        this.mOnCollectListener = mOnCollectListener;
    }

    public ProjectListAdapter(Context context,
                              List<ProjectListBean.DatasBean> datas) {
        super(context, R.layout.item_project_list, datas);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, ProjectListBean.DatasBean bean, int position) {
//        ImageLoaderUtil.LoadImage(mContext, bean.getEnvelopePic(),
//                holder.getView(R.id.imv_project));
        holder.setText(R.id.tv_project_name, bean.getTitle())
                .setText(R.id.tv_project_content, bean.getDesc())
                .setText(R.id.tv_time, bean.getNiceDate())
                .setText(R.id.tv_author, bean.getAuthor())
                .setImageResource(R.id.imv_like, bean.isCollect() ? R.drawable.icon_like :
                        R.drawable.icon_unlike)
                .setOnClickListener(R.id.imv_like, v -> {
                    if (!(boolean) SharedPreferencesUtil.getData(Constants.ISLOGIN, false)) {
                        ToastUtils.showShort("请先登录");
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        return;
                    }
                    if (mOnCollectListener != null) {
                        mOnCollectListener.onCollect(bean.isCollect(), bean.getId(), position);
                    }
                });
    }
}
