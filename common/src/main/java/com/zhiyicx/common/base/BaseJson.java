package com.zhiyicx.common.base;

/**
 * @Describe Json 解析基类
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public class BaseJson<T> {
    /**
     * {"message":"","data":{"key":"success"},"status":true,"code":0}
     */

    private boolean status;
    private String message;
    private int code;
    private T data;

    public BaseJson() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseJson{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
