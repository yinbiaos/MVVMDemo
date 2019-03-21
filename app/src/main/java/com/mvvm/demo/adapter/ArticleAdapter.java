package com.mvvm.demo.adapter;

import android.content.Context;
import android.content.Intent;

import com.base.lib.SharedHelper;
import com.base.lib.ToastUtil;
import com.mvvm.demo.R;
import com.mvvm.demo.activity.X5WebView;
import com.mvvm.demo.activity.login.LoginActivity;
import com.mvvm.demo.config.Constants;
import com.mvvm.demo.entity.ArticleListBean;
import com.mvvm.demo.listener.OnCollectListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by hzy on 2019/1/24
 * ArticleAdapter 首页文章Adapter
 *
 * @author hzy
 */
public class ArticleAdapter extends CommonAdapter<ArticleListBean> {

    private OnCollectListener mOnCollectListener;

    public void setmOnCollectListener(OnCollectListener mOnCollectListener) {
        this.mOnCollectListener = mOnCollectListener;
    }


    public ArticleAdapter(Context context, List<ArticleListBean> datas) {
        super(context, R.layout.item_article, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ArticleListBean bean, int position) {
        boolean isNewest =
                bean.getNiceDate().contains("小时") || bean.getNiceDate().contains("分钟");
        holder.setText(R.id.tv_author, "作者：" + bean.getAuthor());
        holder.setText(R.id.tv_chapterName,
                "分类:" + bean.getChapterName());
        holder.setText(R.id.tv_title, bean.getTitle()
                .replaceAll("&ldquo;", "\"")
                .replaceAll("&rdquo;", "\"")
                .replaceAll("&mdash;", "—"));
        holder.setVisible(R.id.tv_new, isNewest);
        holder.setText(R.id.tv_project, bean.getSuperChapterName());
        holder.setText(R.id.tv_time, "时间：" + bean.getNiceDate());
        holder.setImageResource(R.id.imv_like, bean.isCollect() ?
                R.drawable.icon_like : R.drawable.icon_unlike);
        //收藏和取消收藏
        holder.setOnClickListener(R.id.imv_like, v -> {
            if (!SharedHelper.getInstance().getBoolean(Constants.ISLOGIN, false)) {
                ToastUtil.showToast(mContext, "请先登录");
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                return;
            }
            mOnCollectListener.onCollect(bean.isCollect(), bean.getId(), position);
        });
        holder.setOnClickListener(R.id.tv_project, v -> {
            Intent intent = new Intent(mContext, X5WebView.class);
            intent.putExtra("mUrl",
                    Constants.BASE_URL + bean.getTags().get(0).getUrl());
            intent.putExtra("mTitle", bean.getTags().get(0).getName());
            mContext.startActivity(intent);
        });
    }

}

