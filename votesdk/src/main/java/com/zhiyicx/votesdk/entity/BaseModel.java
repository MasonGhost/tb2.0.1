package com.zhiyicx.votesdk.entity;

import java.io.Serializable;

/**
 * Created by lei on 2016/8/15.
 */
public class BaseModel implements Serializable {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
