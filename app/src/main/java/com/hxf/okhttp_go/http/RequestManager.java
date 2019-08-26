package com.hxf.okhttp_go.http;

public class RequestManager {

    private static RequestManager mInstance;
    private API mRequest;

    public static RequestManager getInstance(){
        if(mInstance == null){
            synchronized (RequestManager.class){
                if(mInstance == null){
                    return  new RequestManager();
                }
            }
        }
        return mInstance;
    }

    public API getRequest() {
        if (mRequest == null) {
            synchronized (RequestManager.class) {
                mRequest = RetrofitManager.getInstance().getRetrofit().create(API.class);
            }
        }
        return mRequest;
    }
}
