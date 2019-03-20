package com.mvvm.demo.activity.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.base.lib.Logs;
import com.mvvm.demo.App;
import com.mvvm.demo.entity.LoginBean;
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
public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "ProjectViewModel";

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<LoginBean>> result = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //页面销毁时调用
        Logs.d(TAG, "onCleared:");
    }

    public MutableLiveData<ResponseBean<LoginBean>> getResult() {
        return result;
    }

    public void postLogin(String username, String password) {
        HttpManager.getInstance().getService(HttpService.class)
                .postLogin(username, password)
                .compose(RxSchedulers.ioMain())
                .subscribe(new DefaultObserver<ResponseBean<LoginBean>>() {
                    @Override
                    public void onNext(ResponseBean<LoginBean> responseBean) {
                        result.setValue(responseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logs.e(TAG, e.toString() + "-----" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Logs.e(TAG, "onComplete");
                    }
                });
    }
}
