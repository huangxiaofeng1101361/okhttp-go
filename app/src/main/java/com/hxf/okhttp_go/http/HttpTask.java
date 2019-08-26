package com.hxf.okhttp_go.http;


public class HttpTask {
    public Class<? extends BaseResponse> responseDataClass;
    public IProtocolListener listener;


    public HttpTask(Class<? extends BaseResponse> responseDataClass, IProtocolListener listener){
        this.responseDataClass = responseDataClass;
        this.listener = listener;
    }
}
