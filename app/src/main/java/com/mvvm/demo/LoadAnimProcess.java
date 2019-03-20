package com.mvvm.demo;

/**
 * 页面数据加载过程
 *
 * @author yinbiao
 * @date 2019/3/20
 */
public interface LoadAnimProcess {

    /**
     * 显示加载动画
     *
     * @param layoutId 替换布局的id
     */
    void startLoading(int layoutId);

    /**
     * 显示加载错误页面
     */
    void error();

    /**
     * 完成
     */
    void done();

    /**
     * 完成，但是没有数据
     */
    void doneEmpty();
}
