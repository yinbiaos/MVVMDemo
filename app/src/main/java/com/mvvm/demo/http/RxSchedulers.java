package com.mvvm.demo.http;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hzy on 2019/1/10
 * 线程调度
 **/
public class RxSchedulers {

    public static <T> ObservableTransformer<T, T> ioMain() {
//        return new ObservableTransformer<T, T>() {
//            @Override
//            public ObservableSource<T> apply(Observable<T> upstream) {
//                return upstream.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
