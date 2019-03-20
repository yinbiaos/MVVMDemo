package com.mvvm.demo.listener;

/**
 * 收藏事件
 */
public interface OnCollectListener {

    /**
     * 点击收藏按钮
     */
    void onCollect(boolean collect,int id,int position);
}
