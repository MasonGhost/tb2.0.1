package com.zhiyicx.thinksnsplus.data.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/18
 * @Contact master.jungle68@gmail.com
 */

public class SystemConfigBean implements Serializable {

    /**
     * im_serve : 127.0.0.1:9900
     * im_helper : [{"uid":"1","url":"https://plus.io/users/1"}]
     */
    @SerializedName("im:serve")
    private String im_serve;
    @SerializedName("im:helper")
    private List<ImHelperBean> im_helper;

    public String getIm_serve() {
        return im_serve;
    }

    public void setIm_serve(String im_serve) {
        this.im_serve = im_serve;
    }

    public List<ImHelperBean> getIm_helper() {
        return im_helper;
    }

    public void setIm_helper(List<ImHelperBean> im_helper) {
        this.im_helper = im_helper;
    }

    /**
     * uid : 1
     * url : https://plus.io/users/1
     */

    public static class ImHelperBean implements Serializable {
        private String uid;
        private String url;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "ImHelperBean{" +
                    "uid='" + uid + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SystemConfigBean{" +
                "im_serve='" + im_serve + '\'' +
                ", im_helper=" + im_helper +
                '}';
    }
}
