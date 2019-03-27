package com.mvvm.demo.activity.system;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.base.lib.Logs;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.entity.SystemDataBean;
import com.mvvm.demo.http.HttpManager;
import com.mvvm.demo.http.HttpService;
import com.mvvm.demo.http.RxSchedulers;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author yinbiao
 * @date 2019/3/11
 */
public class SystemViewModel extends AndroidViewModel {

    private static final String TAG = "SystemViewModel";
    CompositeDisposable disposable;

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<List<SystemDataBean>>> result = new MutableLiveData<>();

    public SystemViewModel(@NonNull Application application) {
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

    public MutableLiveData<ResponseBean<List<SystemDataBean>>> getResult() {
        return result;
    }

    public void getData() {
        disposable.add(HttpManager.getInstance().getService(HttpService.class)
                .getSystemData()
                .compose(RxSchedulers.ioMain())
                .subscribe(listResponseBean -> {
                    result.setValue(listResponseBean);
                }, throwable -> {
                    Logs.d(TAG, throwable.toString());
                    result.setValue(null);
                }));
    }

}
