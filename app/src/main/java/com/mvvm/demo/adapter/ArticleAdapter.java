package com.mvvm.demo.adapter;

import android.content.Context;

import com.mvvm.demo.R;
import com.mvvm.demo.entity.ArticleListBean;
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


    public ArticleAdapter(Context context, List<ArticleListBean> datas) {
        super(context, R.layout.item_article, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ArticleListBean articleListBean, int position) {
        boolean isNewest =
                articleListBean.getNiceDate().contains("小时") || articleListBean.getNiceDate().contains("分钟");
        holder.setText(R.id.tv_author, "作者：" + articleListBean.getAuthor());
        holder.setText(R.id.tv_chapterName,
                "分类:" + articleListBean.getChapterName());
        holder.setText(R.id.tv_title, articleListBean.getTitle()
                .replaceAll("&ldquo;", "\"")
                .replaceAll("&rdquo;", "\"")
                .replaceAll("&mdash;", "—"));
        holder.setVisible(R.id.tv_new, isNewest);
        holder.setText(R.id.tv_project, articleListBean.getSuperChapterName());
        holder.setText(R.id.tv_time, "时间：" + articleListBean.getNiceDate());
        holder.setImageResource(R.id.imv_like, articleListBean.isCollect() ?
                R.drawable.icon_like :
                R.drawable.icon_unlike);
        //收藏和取消收藏
        holder.setOnClickListener(R.id.imv_like, v -> {
//                    if (!(boolean) SharedPreferencesUtil.getData(Constants.ISLOGIN, false)) {
//                        ToastUtils.showShort("请先登录");
//                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
//                        return;
//                    }
//                    if (articleListBean.isCollect()) {
//                        mPresenter.unCollectArticle(articleListBean.getId(), position);
//                    } else {
//                        mPresenter.collectArticle(articleListBean.getId(), position);
//                    }
        });
        holder.setOnClickListener(R.id.tv_project, v -> {
//            Intent intent = new Intent(mContext, X5WebView.class);
//            intent.putExtra("mUrl",
//                    Constants.BASE_URL + articleListBean.getTags().get(0).getUrl());
//            intent.putExtra("mTitle", articleListBean.getTags().get(0).getName());
//            mContext.startActivity(intent);
        });
    }

}
