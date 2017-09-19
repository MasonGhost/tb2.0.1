package com.zhiyicx.zhibosdk.model.entity;


import com.zhiyicx.zhibosdk.model.api.ZBApi;

import java.io.Serializable;

/**
 * Created by jess on 16/4/18.
 */
public class ZBBaseJson<T> implements Serializable {
    public String code;
    public String message;
    public T data;


    /**
     * 请求是否成功
     * @return
     */
    public boolean isSuccess() {
        if (this.code.equals(ZBApi.REQUEST_SUCESS)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ZBBaseJson{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
