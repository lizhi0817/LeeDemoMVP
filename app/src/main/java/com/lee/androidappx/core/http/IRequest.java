package com.lee.androidappx.core.http;

import com.lee.androidappx.model.bean.LoginBean;
import com.lee.androidappx.model.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 23:44
 * Description  :   请求接口
 */
public interface IRequest {

    @FormUrlEncoded
    @POST(ApiService.LOGIN_URL)
    Observable<Result<LoginBean>> login(@Field("phone") String phone,
                                        @Field("pwd") String pwd);


}
