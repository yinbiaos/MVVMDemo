package com.mvvm.demo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.base.lib.Logs;
import com.mvvm.demo.entity.ArticleBean;
import com.mvvm.demo.entity.ResponseBean;
import com.mvvm.demo.http.HttpManager;
import com.mvvm.demo.http.HttpService;
import com.mvvm.demo.http.RxSchedulers;

import io.reactivex.observers.DefaultObserver;

/**
 * @author yinbiao
 * @date 2019/3/11
 */
public class HomeViewModel extends AndroidViewModel {

    private static final String TAG = "HomeViewModel";

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<ArticleBean>> result = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //页面销毁时调用
        Logs.d(TAG, "onCleared:");
    }

    public MutableLiveData<ResponseBean<ArticleBean>> getResult() {
        return result;
    }

    public void getArticle(int page) {
        HttpManager.getInstance().getService(HttpService.class).getArticle(page)
                .compose(RxSchedulers.ioMain())
                .subscribe(new DefaultObserver<ResponseBean<ArticleBean>>() {
                    @Override
                    public void onNext(ResponseBean<ArticleBean> responseBean) {
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
