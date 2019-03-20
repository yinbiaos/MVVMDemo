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

import io.reactivex.observers.DefaultObserver;

/**
 * Created by hzy on 2019/3/20
 *
 * @author hzy
 * */
public class ProjectViewModel extends AndroidViewModel {

    private static final String TAG = "ProjectViewModel";

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<List<ProjectBean>>> result = new MutableLiveData<>();

    public ProjectViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //页面销毁时调用
        Logs.d(TAG, "onCleared:");
    }

    public MutableLiveData<ResponseBean<List<ProjectBean>>> getResult() {
        return result;
    }

    public void getProjectList() {
        HttpManager.getInstance().getService(HttpService.class)
                .getProjectSubject()
                .compose(RxSchedulers.ioMain())
                .subscribe(new DefaultObserver<ResponseBean<List<ProjectBean>>>() {
                    @Override
                    public void onNext(ResponseBean<List<ProjectBean>> listResponseBean) {
                        result.setValue(listResponseBean);
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
