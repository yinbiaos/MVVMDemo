package com.mvvm.demo.activity.project.projectlist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.base.lib.Logs;
import com.mvvm.demo.entity.ProjectListBean;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.http.HttpManager;
import com.mvvm.demo.http.HttpService;
import com.mvvm.demo.http.RxSchedulers;

import io.reactivex.observers.DefaultObserver;

/**
 * Created by hzy on 2019/3/20
 *
 * @author hzy
 */
public class ProjectListViewModel extends AndroidViewModel {

    private static final String TAG = "ProjectListViewModel";

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<ProjectListBean>> result = new MutableLiveData<>();
    private MutableLiveData<ResponseBean> collectResult = new MutableLiveData<>();
    private MutableLiveData<ResponseBean> UnCollectResult = new MutableLiveData<>();

    public ProjectListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //页面销毁时调用
        Logs.d(TAG, "onCleared:");
    }

    public MutableLiveData<ResponseBean<ProjectListBean>> getResult() {
        return result;
    }

    public void getProjectList(int page, int cid) {
        HttpManager.getInstance().getService(HttpService.class)
                .getProjectList(page, cid)
                .compose(RxSchedulers.ioMain())
                .subscribe(new DefaultObserver<ResponseBean<ProjectListBean>>() {
                    @Override
                    public void onNext(ResponseBean<ProjectListBean> responseBean) {
                        result.setValue(responseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logs.e(TAG, e.toString() + "-----" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<ResponseBean> getCollectResult() {
        return collectResult;
    }

    public void collectArticle(int id) {
        HttpManager.getInstance().getService(HttpService.class)
                .insideCollect(id)
                .compose(RxSchedulers.ioMain())
                .subscribe(new DefaultObserver<ResponseBean>() {
                    @Override
                    public void onNext(ResponseBean responseBean) {
                        collectResult.setValue(responseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logs.e(TAG, e.toString() + "-----" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<ResponseBean> getUnCollectResult() {
        return UnCollectResult;
    }

    public void unCollectArticle(int id) {
        HttpManager.getInstance().getService(HttpService.class)
                .articleListUncollect(id)
                .compose(RxSchedulers.ioMain())
                .subscribe(new DefaultObserver<ResponseBean>() {
                    @Override
                    public void onNext(ResponseBean responseBean) {
                        UnCollectResult.setValue(responseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logs.e(TAG, e.toString() + "-----" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}