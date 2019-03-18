package com.mvvm.demo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.base.lib.Logs;

/**
 * @author yinbiao
 * @date 2019/3/11
 */
public class HomeViewModel extends AndroidViewModel {

    private static final String TAG = "HomeViewModel";

    // 创建LiveData
    private MutableLiveData<String> result = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //页面销毁时调用
        Logs.d(TAG, "onCleared:");
    }

    public void doSomeThing() {

    }

    public MutableLiveData<String> getResult() {
        return result;
    }
}
