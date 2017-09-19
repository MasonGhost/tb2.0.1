package com.zhiyicx.zhibolibrary.model.entity;

import java.io.Serializable;

/**
 * Created by zhiyicx on 2016/3/29.
 */
public class ApiList implements Serializable {
    public String message;
    public String code;
    public SearchResult[] data;

    public ApiList(SearchResult[] data) {
        this.data = data;
    }

    public ApiList() {
    }
}
