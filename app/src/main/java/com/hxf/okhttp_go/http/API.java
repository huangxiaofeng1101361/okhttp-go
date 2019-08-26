package com.hxf.okhttp_go.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    //示例
    @GET("OAuth/getAccessTokenSig")
    Call <ResponseBody> getToken(@Query("oauth_consumer_key") String key, @Query("oauth_consumer_sig") String sign, @Query("clientid") String clientid, @Query("clientsecret") String clientsecret);
}
