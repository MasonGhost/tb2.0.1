package com.zhiyicx.zhibosdk.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhiyicx on 2016/3/29.
 */
public class ZBApiPlay implements Serializable {
    public String code;
    public String message;
    public PlayInfo data;


    public class PlayInfo implements Serializable{
        public String code;
        public String message;
        public int view_count;
        public List<ListBean> list;
        public ZBPlayUrls playurls;
        public ZBApiImInfo im;
        public ListBean.UserBean user;
    }

    public static class ListBean implements Serializable{
        /**
         * id : z1.zhibo_test.zhibocloud_uid_4
         * title : 我在直播
         * location :
         * icon : {"0":"http://my.zhibocloud.com/public/uploads/2016-08-01/images/1676408d0fd7659e08c7539444a389ce.jpg","1":"http://my.zhibocloud.com/public/uploads/2016-08-01/images/thumb/1676408d0fd7659e08c7539444a389ce_t.jpg"}
         * online_count : 0
         */

        private StreamBean stream;
        /**
         * cid : 10087
         * im_uid : 20004
         */

        private ImBean im;
        /**
         * usid : zbuser_7
         */

        private UserBean user;

        public StreamBean getStream() {
            return stream;
        }

        public void setStream(StreamBean stream) {
            this.stream = stream;
        }

        public ImBean getIm() {
            return im;
        }

        public void setIm(ImBean im) {
            this.im = im;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class StreamBean implements Serializable{
            private String id;
            private String title;
            private String location;
            /**
             * 0 : http://my.zhibocloud.com/public/uploads/2016-08-01/images/1676408d0fd7659e08c7539444a389ce.jpg
             * 1 : http://my.zhibocloud.com/public/uploads/2016-08-01/images/thumb/1676408d0fd7659e08c7539444a389ce_t.jpg
             */

            private IconBean icon;
            private int online_count;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public IconBean getIcon() {
                return icon;
            }

            public void setIcon(IconBean icon) {
                this.icon = icon;
            }

            public int getOnline_count() {
                return online_count;
            }

            public void setOnline_count(int online_count) {
                this.online_count = online_count;
            }

            public static class IconBean implements Serializable{
                @SerializedName("0")
                private String value0;
                @SerializedName("1")
                private String value1;

                public String getValue0() {
                    return value0;
                }

                public void setValue0(String value0) {
                    this.value0 = value0;
                }

                public String getValue1() {
                    return value1;
                }

                public void setValue1(String value1) {
                    this.value1 = value1;
                }
            }
        }

        public static class ImBean implements Serializable{
            private int cid;
            private int im_uid;

            public int getCid() {
                return cid;
            }

            public void setCid(int cid) {
                this.cid = cid;
            }

            public int getIm_uid() {
                return im_uid;
            }

            public void setIm_uid(int im_uid) {
                this.im_uid = im_uid;
            }
        }

        public static class UserBean implements Serializable{
            private String usid;

            public String getUsid() {
                return usid;
            }

            public void setUsid(String usid) {
                this.usid = usid;
            }
        }
    }

}
