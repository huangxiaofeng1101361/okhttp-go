package com.hxf.okhttp_go.http;

public interface IProtocolListener {
    void onProtocolRequestFinish(int taskId, int errorCode, BaseResponse responseData);
}
