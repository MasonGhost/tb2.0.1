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

    private static final long serialVersionUID = -2767044631905981596L;
    /**
     * wallet:ratio: 200 // 转换显示余额的比例，百分比。（200 就表示 200%）
     * im_serve : 127.0.0.1:9900
     * im_helper : [{"uid":"1","url":"https://plus.io/users/1"}]
     * "ad": [],
     * "checkin": false
     */
    @SerializedName("wallet:ratio")
    private int wallet_ratio;
    @SerializedName("im:serve")
    private String im_serve;
    @SerializedName("im:helper")
    private List<ImHelperBean> im_helper;
    @SerializedName("ad")
    private List<Advert> mAdverts;
    @SerializedName("wallet:recharge-type")
    private String[] mWalletTtype;

    private boolean checkin;


    public String[] getWalletTtype() {
        return mWalletTtype;
    }

    public void setWalletTtype(String[] walletTtype) {
        mWalletTtype = walletTtype;
    }

    public int getWallet_ratio() {
        return wallet_ratio;
    }

    public void setWallet_ratio(int wallet_ratio) {
        this.wallet_ratio = wallet_ratio;
    }

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

    public List<Advert> getAdverts() {
        return mAdverts;
    }

    public void setAdverts(List<Advert> adverts) {
        mAdverts = adverts;
    }

    public boolean isCheckin() {
        return checkin;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }

    /**
     * uid : 1
     * url : https://plus.io/users/1
     */

    public static class ImHelperBean implements Serializable {
        private String uid;
        private String url;
        private boolean isDelete;

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

        public boolean isDelete() {
            return isDelete;
        }

        public void setDelete(boolean delete) {
            isDelete = delete;
        }

        @Override
        public String toString() {
            return "ImHelperBean{" +
                    "uid='" + uid + '\'' +
                    ", url='" + url + '\'' +
                    ", isDelete=" + isDelete +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SystemConfigBean{" +
                "wallet_ratio=" + wallet_ratio +
                ", im_serve='" + im_serve + '\'' +
                ", im_helper=" + im_helper +
                '}';
    }

    /**
     * {
     * "id":1,
     * "title":"广告1",
     * "type":"image",
     * "data":{
     * "image":"https://avatars0.githubusercontent.com/u/5564821?v=3&s=460",
     * "link":"https://github.com/zhiyicx/thinksns-plus"
     * }
     */
    public static class Advert implements Serializable {
        private int id;
        private String title;
        private String type;
        private Object data;
        private ImageAdvert mImageAdvert;

        public ImageAdvert getImageAdvert() {
            return mImageAdvert;
        }

        public void setImageAdvert(ImageAdvert imageAdvert) {
            mImageAdvert = imageAdvert;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }


}
