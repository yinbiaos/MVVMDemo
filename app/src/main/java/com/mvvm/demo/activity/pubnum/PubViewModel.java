package com.mvvm.demo.activity.pubnum;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.base.lib.Logs;
import com.mvvm.demo.entity.NaviBean;
import com.mvvm.demo.entity.PublicAddrBean;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.http.HttpManager;
import com.mvvm.demo.http.HttpService;
import com.mvvm.demo.http.RxSchedulers;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author yinbiao
 * @date 2019/3/11
 */
public class PubViewModel extends AndroidViewModel {

    private static final String TAG = "NaviViewModel";
    CompositeDisposable disposable;

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<List<PublicAddrBean>>> result = new MutableLiveData<>();

    public PubViewModel(@NonNull Application application) {
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

    public MutableLiveData<ResponseBean<List<PublicAddrBean>>> getResult() {
        return result;
    }

    public void getData() {
        disposable.add(HttpManager.getInstance().getService(HttpService.class).getPublicAddrList()
                .compose(RxSchedulers.ioMain())
                .subscribe(listResponseBean -> {
                    result.setValue(listResponseBean);
                }, throwable -> {
                    Logs.d(TAG, throwable.toString());
                    result.setValue(null);
                }));
    }

}
