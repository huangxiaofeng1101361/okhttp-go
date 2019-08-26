package com.hxf.okhttp_go.http;


import okhttp3.ResponseBody;
import retrofit2.Response;

public interface RetrofitRequestListener {
    void onResponse(int taskId, Response<ResponseBody> response);

    void onErrorResponse(int taskId, Throwable t);

}
