package com.hxf.okhttp_go.http;

import com.hxf.okhttp_go.utils.NetError;
import retrofit2.Call;

public abstract class SinglePageModel extends BaseModel implements IProtocolListener{

    public abstract Call getCall();
    public abstract Class<? extends BaseResponse> getResponseDataClass();
    public abstract void onDataLoadFinish(int errorCode,BaseResponse response);

    public void sendNetworkRequest(){
        ProtocolManager.getInstance().sendRequest(getCall(),getResponseDataClass(),this);
    }

    @Override
    public void onProtocolRequestFinish(int taskId, int errorCode, BaseResponse responseData) {
        if(responseData != null && errorCode == NetError.SUCCESS){
            onDataLoadFinish(errorCode,responseData);
        }
    }
}
