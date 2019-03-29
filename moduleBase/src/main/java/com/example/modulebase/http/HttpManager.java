package com.example.modulebase.http;

import com.example.modulecommon.bean.modulebase.MyDateDeserializer;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpManager {
    //定义基本地址
    public static final String HOST = "http://gank.io/";
    //设置连接超时的值
    private static final int TIMEOUT = 10;
    //声明HttpService对象
    protected HttpService mHttpService;
    //声明HttpManager对象
    private volatile static HttpManager mHttpManager;

    protected HttpManager() {
        //新建一个文件用来缓存网络请求
        //File cacheDirectory = new File(getApplicationContext().getCacheDir().getAbsolutePath(), "HttpCache");
        //实例化一个OkHttpClient.Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                //设置从主机读信息超时
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                //设置写信息超时
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                //设置缓存文件
                //.cache(new Cache(cacheDirectory, 10 * 1024 * 1024))
                /**
                 * 设置OkHttp拦截器
                 * 为每个retrofit2的网络请求都增加相同的head头信息
                 */
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json; charset=UTF-8")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .addHeader("Cache-Control", String.format("public, max-age=%d", 60))
                                .removeHeader("Pragma")
                                .build();
                        return chain.proceed(request);
                    }
                });

        Retrofit.Builder rBuilder = new Retrofit.Builder()
                .client(builder.build())
                //网络访问返回字符串转换器
                .addConverterFactory(ScalarsConverterFactory.create())
                //网络访问返回json转换器
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().registerTypeAdapter(Date.class,
                                new MyDateDeserializer()).setDateFormat(DateFormat.LONG).create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //基本网络地址
                .baseUrl(getHost());
        mHttpService = rBuilder.build().create(HttpService.class);
    }

    protected String getHost() {
        return HOST;
    }

    public HttpService getHttpService() {
        return mHttpService;
    }

    public static HttpManager getInstance() {
        if (mHttpManager == null) {
            synchronized (HttpManager.class) {
                if (mHttpManager == null) {
                    mHttpManager = new HttpManager();
                }
            }
        }
        return mHttpManager;
    }
}
