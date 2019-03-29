package com.example.modulebase.http;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface HttpService {
    @GET
    Observable<Response<String>> testHttp(@Url String url);

    @FormUrlEncoded
    @POST("/")
    Observable<Response<String>> testHttp(@FieldMap Map<String, String> fileMap);
}
