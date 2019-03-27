package com.mvvm.demo.activity.navigat;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.base.lib.Logs;
import com.mvvm.demo.entity.NaviBean;
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
public class NaviViewModel extends AndroidViewModel {

    private static final String TAG = "NaviViewModel";
    CompositeDisposable disposable;

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<List<NaviBean>>> result = new MutableLiveData<>();

    public NaviViewModel(@NonNull Application application) {
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

    public MutableLiveData<ResponseBean<List<NaviBean>>> getResult() {
        return result;
    }

    public void getNavi() {
        disposable.add(HttpManager.getInstance().getService(HttpService.class).getNaviData()
                .compose(RxSchedulers.ioMain())
                .subscribe(listResponseBean -> {
                    result.setValue(listResponseBean);
                }, throwable -> {
                    Logs.d(TAG, throwable.toString());
                    result.setValue(null);
                }));
    }

}
