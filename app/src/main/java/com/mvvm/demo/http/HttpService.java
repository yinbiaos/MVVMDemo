package com.mvvm.demo.http;

import com.mvvm.demo.entity.ArticleBean;
import com.mvvm.demo.entity.HomeBanner;
import com.mvvm.demo.entity.ResponseBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author yinbiao
 * @date 2019/3/18
 */
public interface HttpService {

    /**
     * 1.1 首页文章列表
     *
     * @return
     */
    @GET("/article/list/{page}/json")
    Observable<ResponseBean<ArticleBean>> getArticle(@Path("page") int page);

    /**
     * 1.2 首页banner
     *
     * @return
     */
    @GET("/banner/json")
    Observable<ResponseBean<List<HomeBanner>>> getBannerList();
}
