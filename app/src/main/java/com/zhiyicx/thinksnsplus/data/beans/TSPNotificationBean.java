package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.annotation.Entity;

/**
 * @Describe  detail to @see{https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/notifications.md#%E9%80%9A%E7%9F%A5%E5%88%97%E8%A1%A8}
 * @Author Jungle68
 * @Date 2017/7/11
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class TSPNotificationBean {


    /**
     * id : 98aaae93-9d9e-446e-b894-691569b686b5
     * read_at : 2017-07-11 04:23:08
     * data : {"channel":"feed:pinned-comment","target":1,"content":"我是测试消息","extra":null}
     * created_at : 2017-07-10 04:23:08
     */
    private Long _id;
    private String id;
    private String read_at;
    private DataBean data;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRead_at() {
        return read_at;
    }

    public void setRead_at(String read_at) {
        this.read_at = read_at;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public static class DataBean {
        /**
         * channel : feed:pinned-comment
         * target : 1
         * content : 我是测试消息
         * extra : null
         */

        private String channel;
        private int target;
        private String content;
        private Object extra;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getExtra() {
            return extra;
        }

        public void setExtra(Object extra) {
            this.extra = extra;
        }
    }
}
