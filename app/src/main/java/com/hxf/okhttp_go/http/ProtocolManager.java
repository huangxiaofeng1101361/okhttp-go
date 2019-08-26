package com.hxf.okhttp_go.http;

import android.util.Log;
import com.google.gson.Gson;
import com.hxf.okhttp_go.utils.NetError;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ProtocolManager implements RetrofitRequestListener {
    private static final String TAG = "ProtocolManager";
    private static ProtocolManager mInstance;
    private ConcurrentHashMap<Integer, HttpTask> mRefQueue;
    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    public static ProtocolManager getInstance() {
        if (mInstance == null) {
            synchronized (ProtocolManager.class) {
                if (mInstance == null) {
                    mInstance = new ProtocolManager();
                }
            }
        }
        return mInstance;
    }

    public ProtocolManager() {
        mRefQueue = new ConcurrentHashMap<>();
    }

    private int getSequenceNumber() {
        return mSequenceGenerator.incrementAndGet();
    }

    public void sendRequest(Call call, Class<? extends BaseResponse> responseDataClass, IProtocolListener listener) {
        int taskId = getSequenceNumber();
        HttpTask httpTask = new HttpTask(responseDataClass, listener);
        mRefQueue.put(taskId, httpTask);
        RetrofitRequest retrofitRequest = new RetrofitRequest(taskId, this);
        retrofitRequest.request(call);
    }

    @Override
    public void onResponse(int taskId, Response<ResponseBody> response) {
        HttpTask task = mRefQueue.remove(taskId);
        if (task == null || task.listener == null) {
            return;
        }
        if (response == null || response.body() == null) {
            task.listener.onProtocolRequestFinish(taskId, NetError.SUCCESS, null);
        } else {
            BaseResponse object = null;
            try {
                String result = response.body().string();
                Log.d(TAG, "response.body: " + result + "\n" + ",responseDataClass : " + task.responseDataClass);
                if (task.responseDataClass != null) {
                    object = new Gson().fromJson(result, task.responseDataClass);
                } else {
                    object = new BaseResponse(NetError.SUCCESS,"");
                }
            } catch (Exception e) {
                e.printStackTrace();
                object = new BaseResponse(NetError.PARSE_ERROR,"");
            }
            task.listener.onProtocolRequestFinish(taskId, object.getErrorCode(), object);
        }
    }

    @Override
    public void onErrorResponse(int taskId,Throwable t) {
        Log.d(TAG, "error: " + t);
        HttpTask task = mRefQueue.remove(taskId);
        if(task == null){
            return;
        }
        IProtocolListener listener = task.listener;
        BaseResponse data;
        if(listener != null){
            data = new BaseResponse(NetError.UNKNOW_ERROR,"");
            listener.onProtocolRequestFinish(taskId, NetError.UNKNOW_ERROR,data);
        }
    }

}
