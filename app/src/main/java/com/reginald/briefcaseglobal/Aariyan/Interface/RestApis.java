package com.reginald.briefcaseglobal.Aariyan.Interface;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApis {

    @GET("Products.php?")
    Observable<ResponseBody> getProducts(@Query("CustomerCode") String code);

    @POST("PostDealsXmls.php")
    Observable<ResponseBody> postToServer(@Body String xmlData);
}
