package com.zhiyicx.thinksnsplus.data.beans;


import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description 专辑列表
 */
public class MusicAlbumListBean extends BaseListBean implements Parcelable {

    /**
     * status : true
     * code : 0
     * message : 操作成功
     * data : [{"sheet_id":1,"title":"周杰伦","storage":1,"taste_count":2000},{"sheet_id":2,
     * "title":"陈奕迅","storage":2,"taste_count":1000}]
     */

    private boolean status;
    private int code;
    private String message;
    private List<DataBean> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sheet_id : 1
         * title : 周杰伦
         * storage : 1
         * taste_count : 2000
         * <p>
         * sheet_id int 专辑id
         * title 专辑名称
         * storage int 封面id
         * taste_count int 播放数量
         */

        private int sheet_id;
        private String title;
        private int storage;
        private int taste_count;

        public int getSheet_id() {
            return sheet_id;
        }

        public void setSheet_id(int sheet_id) {
            this.sheet_id = sheet_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStorage() {
            return storage;
        }

        public void setStorage(int storage) {
            this.storage = storage;
        }

        public int getTaste_count() {
            return taste_count;
        }

        public void setTaste_count(int taste_count) {
            this.taste_count = taste_count;
        }
    }
}
