package com.zhiyicx.thinksnsplus.data.beans;

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
    private String uri; // 上传附件的地址
    private String method;// 请求附件上传的方式
    private Object headers; // 请求头
    private Object options;// 请求体
    private String input ; // 上传资源的表单名称

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getHeaders() {
        return headers;
    }

    public void setHeaders(Object headers) {
        this.headers = headers;
    }

    public Object getOptions() {
        return options;
    }

    public void setOptions(Object options) {
        this.options = options;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "StorageTaskBean{" +
                "storage_id=" + storage_id +
                ", storage_task_id=" + storage_task_id +
                ", uri='" + uri + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", options=" + options +
                ", input='" + input + '\'' +
                '}';
    }
}
