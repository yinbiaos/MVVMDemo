package com.mvvm.demo.activity.project;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.base.lib.Logs;
import com.mvvm.demo.entity.ProjectBean;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.http.HttpManager;
import com.mvvm.demo.http.HttpService;
import com.mvvm.demo.http.RxSchedulers;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by hzy on 2019/3/20
 *
 * @author hzy
 */
public class ProjectViewModel extends AndroidViewModel {

    private static final String TAG = "ProjectViewModel";

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<List<ProjectBean>>> result = new MutableLiveData<>();

    CompositeDisposable disposable;

    public ProjectViewModel(@NonNull Application application) {
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

    public MutableLiveData<ResponseBean<List<ProjectBean>>> getResult() {
        return result;
    }

    public void getProjectList() {
        disposable.add(HttpManager.getInstance().getService(HttpService.class)
                .getProjectSubject()
                .compose(RxSchedulers.ioMain())
                .subscribe(listResponseBean -> {
                    result.setValue(listResponseBean);
                }, throwable -> {
                    //TODO
                }));
    }
}
