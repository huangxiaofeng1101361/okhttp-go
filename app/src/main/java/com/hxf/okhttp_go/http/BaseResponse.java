package com.hxf.okhttp_go.http;

public class BaseResponse {
    private int errorCode;
    private String msg;

    public BaseResponse(int errorCode,String prompt){
        this.errorCode = errorCode;
        this.msg = prompt;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
