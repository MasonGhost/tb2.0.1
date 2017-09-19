package com.zhiyicx.zhibolibrary.model.entity;


import com.zhiyicx.zhibolibrary.model.api.ZBLApi;

import java.io.Serializable;

/**
 * Created by jess on 16/4/18.
 */
public class BaseJson<T> implements Serializable {
    public String code;
    public String message;
    public T data;


    /**
     * 请求是否成功
     * @return
     */
    public boolean isSuccess() {
        if (this.code.equals(ZBLApi.REQUEST_SUCESS)) {
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
