package com.hxf.okhttp_go.http;

public interface IModelListener {
    void onLoadFinish(BaseModel baseModel, int errorCode, String prompt, boolean isEmpty, boolean isFirstPage, boolean hasNextPage);
}
