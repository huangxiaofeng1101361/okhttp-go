package com.hxf.okhttp_go.http;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitRequest {

    private int taskId;
    private RetrofitRequestListener mListener;


    public RetrofitRequest(int taskId, RetrofitRequestListener listener) {
        this.taskId = taskId;
        this.mListener = listener;
    }

    public void request(Call<ResponseBody> call){
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(mListener != null){
                    mListener.onResponse(taskId,response);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                   if(mListener != null){
                       mListener.onErrorResponse(taskId,t);
                   }
            }
        });
    }

}
