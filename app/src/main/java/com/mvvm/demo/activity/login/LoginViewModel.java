package com.mvvm.demo.activity.login;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.base.lib.Logs;
import com.mvvm.demo.entity.LoginBean;
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
public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<LoginBean>> result = new MutableLiveData<>();

    CompositeDisposable disposable;

    public LoginViewModel(@NonNull Application application) {
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


    public MutableLiveData<ResponseBean<LoginBean>> getResult() {
        return result;
    }

    public void postLogin(String username, String password) {
        disposable.add(HttpManager.getInstance().getService(HttpService.class)
                .postLogin(username, password)
                .compose(RxSchedulers.ioMain())
                .subscribe(responseBean -> {
                    result.setValue(responseBean);
                }, throwable -> {
                    Logs.e(TAG, throwable.toString() + "-----" + throwable.getMessage());
                }));
    }
}
