package com.hxf.okhttp_go.http;

import com.hxf.okhttp_go.utils.Constant;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final String TAG = "RetrofitManager";
    private static RetrofitManager mInstance;
    private Retrofit mRetrofit;
    private OkHttpClient client;
    private String token = "";

    public static RetrofitManager getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder().addHeader(Constant.TOKEN, token).build();
                    return chain.proceed(request);
                })
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("") //此处填接口的服务器地址
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void setToken(String token) {
        this.token = token;
    }

   public Retrofit getRetrofit(){
        return mRetrofit;
   }

}
