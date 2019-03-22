package com.mvvm.demo.activity.register;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.base.lib.Logs;
import com.mvvm.demo.entity.LoginBean;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.http.HttpManager;
import com.mvvm.demo.http.HttpService;
import com.mvvm.demo.http.RxSchedulers;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DefaultObserver;

/**
 * Created by hzy on 2019/3/20
 *
 * @author hzy
 */
public class RegisterViewModel extends AndroidViewModel {

    private static final String TAG = "ProjectViewModel";

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean> result = new MutableLiveData<>();

    CompositeDisposable disposable;

    public RegisterViewModel(@NonNull Application application) {
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

    public MutableLiveData<ResponseBean> getResult() {
        return result;
    }

    public void postRegister(String username, String password, String password2) {
        disposable.add(HttpManager.getInstance().getService(HttpService.class)
                .postRegister(username, password, password2)
                .compose(RxSchedulers.ioMain())
                .subscribe(responseBean -> {
                    result.setValue(responseBean);
                }, throwable -> {
                    Logs.e(TAG, throwable.toString() + "-----" + throwable.getMessage());
                }));
    }
}
