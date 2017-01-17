package com.zhiyicx.thinksnsplus.data.beans;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author LiuChao
 * @describe 储存任务（文件上传）的返回体
 * @date 2017/1/16
 * @contact email:450127106@qq.com
 */

public class StorageTaskBean {
    // storage_id 储存唯一标识字段,表示跳过上传步骤和通知步骤，直接上传成功。
    private int storage_id;
    // storage_task_id 上传任务的id，作为本次操作的唯一标识符
    private int storage_task_id;
    // 下面的部分暂时没有啥用，但需要传递给服务器
    private String uri;
    private String method;
    private JSONObject headers;
    private JSONArray options;

    public int getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(int storage_id) {
        this.storage_id = storage_id;
    }

    public int getStorage_task_id() {
        return storage_task_id;
    }

    public void setStorage_task_id(int storage_task_id) {
        this.storage_task_id = storage_task_id;
    }


}
