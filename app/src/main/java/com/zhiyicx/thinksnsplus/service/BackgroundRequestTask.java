package com.zhiyicx.thinksnsplus.service;


import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;

import java.util.Map;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundRequestTask {
    public static final int DEFAULT_MAX_RETRY_COUNT = 5;
    private int max_retry_count = DEFAULT_MAX_RETRY_COUNT; // 最大重新请求次数
    private BackgroundTaskRequestMethodConfig methodType = BackgroundTaskRequestMethodConfig.POST;
    private String path;
    private Map<String, Object> params;

    public BackgroundRequestTask() {
    }

    public BackgroundRequestTask(String path, Map<String, Object> params) {
        this.path = path;
        this.params = params;
    }

    public BackgroundRequestTask(BackgroundTaskRequestMethodConfig methodType, String path, Map<String, Object> params) {
        this.methodType = methodType;
        this.path = path;
        this.params = params;
    }

    public BackgroundTaskRequestMethodConfig getMethodType() {
        return methodType;
    }

    public void setMethodType(BackgroundTaskRequestMethodConfig methodType) {
        this.methodType = methodType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public int getMax_retry_count() {
        return max_retry_count;
    }

    public void setMax_retry_count(int max_retry_count) {
        this.max_retry_count = max_retry_count;
    }
}
