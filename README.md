# okhttp-go
简述：基于retrofit的二次封装，结合app整体架构，采用MVP模式（我所理解的MVP，如有不对，请指正，感谢），支持单页与分页加载.

在Application处进行初始化

RetrofitManager.getInstance().init();

原则上一个请求对应一个model,以获取token为例，示例代码如下：

新建一个TokenModel继承SinglePageModel,此处对应MVP架构的Model层

public class TokenModel extends SinglePageModel {
    private TokenResponse tokenResponse;

    public TokenResponse getTokenResponse(){
        return tokenResponse;
    }
    @Override
    public Call getCall() {
        String md5Str = UtilsToB.md5(UtilsToB.USER_APP_KEY + "AndroidClient" + "imei:" + UtilsToB.USER_IMEI + UtilsToB.USER_SECRET);
        return RequestManager.getInstance().getRequest().getToken(UtilsToB.USER_APP_KEY, md5Str, "AndroidClient", "imei:" + UtilsToB.USER_IMEI);
    }

    @Override
    public Class<? extends BaseResponse> getResponseDataClass() {
        return TokenResponse.class;
    }

    @Override
    public void onDataLoadFinish(int errorCode, BaseResponse response) {
             if(response instanceof TokenResponse){
                 tokenResponse = (TokenResponse) response;
                 if(tokenResponse.getResponse() != null && tokenResponse.getResponse().getDocs() != null){
                     sendMessageToUI(errorCode,response.getResponseHeader() == null?"":response.getResponseHeader().getErrorinfo(),false,true,false);
                 }else{
                     sendMessageToUI(errorCode,response.getResponseHeader() == null?"":response.getResponseHeader().getErrorinfo(),true,true,false);
                 }
         }
    }
}


获取到数据后，具体逻辑业务处理放在P层处理，类似的会有TokenPresenter,该类实现IModelListener接口，在其回调方法onLoadFinish里面处理具体逻辑，该层持有Model层的引用以及view层（即activity或fragment）的引用，在其构造方法先注册这个接口,调用其基类BaseModel的register方法

发起请求，直接调用SinglePageModel或者MultiPageModel的sendNetworkRequest方法
