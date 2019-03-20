package com.mvvm.demo.activity.home;

import com.mvvm.demo.entity.ArticleListBean;

/**
 * 收藏事件
 */
public interface OnCollectListener {
    /**
     * 点击收藏按钮
     *
     * @param article 收藏对象
     */
    void onCollect(ArticleListBean article);
}
