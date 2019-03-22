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

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by hzy on 2019/3/20
 *
 * @author hzy
 */
public class ProjectListViewModel extends AndroidViewModel {

    private static final String TAG = "ProjectListViewModel";

    CompositeDisposable disposable;

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<ProjectListBean>> result = new MutableLiveData<>();
    private MutableLiveData<ResponseBean> collectResult = new MutableLiveData<>();
    private MutableLiveData<ResponseBean> unCollectResult = new MutableLiveData<>();

    public ProjectListViewModel(@NonNull Application application) {
        super(application);
        disposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //页面销毁时调用
        Logs.d(TAG, "onCleared:");
        disposable.clear();
    }

    public MutableLiveData<ResponseBean<ProjectListBean>> getResult() {
        return result;
    }

    public void getProjectList(int page, int cid) {
        disposable.add(HttpManager.getInstance().getService(HttpService.class)
                .getProjectList(page, cid)
                .compose(RxSchedulers.ioMain())
                .subscribe(responseBean -> {
                    result.setValue(responseBean);
                }, throwable -> {
                    //TODO
                }));
    }

    public MutableLiveData<ResponseBean> getCollectResult() {
        return collectResult;
    }

    public void collectArticle(int id) {
        disposable.add(HttpManager.getInstance().getService(HttpService.class)
                .insideCollect(id)
                .compose(RxSchedulers.ioMain())
                .subscribe(responseBean -> {
                    collectResult.setValue(responseBean);
                }, throwable -> {
                    //TODO
                }));
    }

    public MutableLiveData<ResponseBean> getUnCollectResult() {
        return unCollectResult;
    }

    public void unCollectArticle(int id) {
        disposable.add(HttpManager.getInstance().getService(HttpService.class)
                .articleListUncollect(id)
                .compose(RxSchedulers.ioMain())
                .subscribe(responseBean -> {
                    unCollectResult.setValue(responseBean);

                }, throwable -> {
                    //TODO
                }));

    }
}