package com.hxf.okhttp_go.http;


import retrofit2.Call;

public abstract class MultiPageModel extends BaseModel implements IProtocolListener {
    protected int offset = 0;
    protected int length = 30;

    protected abstract Call getCall();

    protected abstract Class<? extends BaseResponse> getResponseDataClass();

    protected abstract void onDataLoadFinish(int errorCode, BaseResponse response);


    public void sendNetworkRequest() {
        ProtocolManager.getInstance().sendRequest(getCall(), getResponseDataClass(), this);
    }

    @Override
    public void onProtocolRequestFinish(int taskId, int errorCode, BaseResponse responseData) {
        onDataLoadFinish(errorCode, responseData);
    }

    public void loadNextPage() {
        offset += length;
        sendNetworkRequest();
    }

}
