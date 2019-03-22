package com.mvvm.demo.activity.home;

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

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;

/**
 * @author yinbiao
 * @date 2019/3/11
 */
public class HomeViewModel extends AndroidViewModel {

    private static final String TAG = "HomeViewModel";
    private Disposable disposable;

    /**
     * 创建LiveData
     */
    private MutableLiveData<ResponseBean<ArticleBean>> result = new MutableLiveData<>();
    private MutableLiveData<ResponseBean> collectResult = new MutableLiveData<>();
    private MutableLiveData<ResponseBean> unCollectResult = new MutableLiveData<>();

    private int mPosition;

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //页面销毁时调用
        Logs.d(TAG, "onCleared:");
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public MutableLiveData<ResponseBean<ArticleBean>> getResult() {
        return result;
    }

    public MutableLiveData<ResponseBean> getUnCollectResult() {
        return unCollectResult;
    }

    public MutableLiveData<ResponseBean> getCollectResult() {
        return collectResult;
    }

    public int getPosition() {
        return mPosition;
    }

    public void getArticle(int page) {
        disposable = HttpManager.getInstance().getService(HttpService.class).getArticle(page)
                .compose(RxSchedulers.ioMain())
                .subscribe(responseBean -> {
                    result.setValue(responseBean);
                }, throwable -> {
                    Logs.d(TAG, throwable.toString());
                    result.setValue(null);
                });
    }


    public void collectArticle(int id, int position) {
        HttpManager.getInstance().getService(HttpService.class)
                .insideCollect(id)
                .compose(RxSchedulers.ioMain())
                .subscribe(new DefaultObserver<ResponseBean>() {
                    @Override
                    public void onNext(ResponseBean responseBean) {
                        mPosition = position;
                        collectResult.setValue(responseBean);
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


    public void unCollectArticle(int id, int position) {
        HttpManager.getInstance().getService(HttpService.class)
                .articleListUncollect(id)
                .compose(RxSchedulers.ioMain())
                .subscribe(new DefaultObserver<ResponseBean>() {
                    @Override
                    public void onNext(ResponseBean responseBean) {
                        mPosition = position;
                        unCollectResult.setValue(responseBean);
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
