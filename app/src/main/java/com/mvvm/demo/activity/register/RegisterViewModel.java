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

import io.reactivex.observers.DefaultObserver;

/**
 * Created by hzy on 2019/3/20
 *
 * @author hzy
 * */
public class RegisterViewModel extends AndroidViewModel {

    private static final String TAG = "ProjectViewModel";

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean> result = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //页面销毁时调用
        Logs.d(TAG, "onCleared:");
    }

    public MutableLiveData<ResponseBean> getResult() {
        return result;
    }

    public void postRegister(String username, String password, String password2) {
        HttpManager.getInstance().getService(HttpService.class)
                .postRegister(username, password, password2)
                .compose(RxSchedulers.ioMain())
                .subscribe(new DefaultObserver<ResponseBean>() {
                    @Override
                    public void onNext(ResponseBean responseBean) {
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
}
