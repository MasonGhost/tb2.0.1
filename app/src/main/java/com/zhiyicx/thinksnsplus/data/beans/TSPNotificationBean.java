package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;

import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_COMMENTS;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_DIGGS;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_PINNED_COMMENT;

/**
 * @Describe detail to @see{https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/notifications.md#%E9%80%9A%E7%9F%A5%E5%88%97%E8%A1%A8}
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
    @Id
    private Long _id;
    private String id;
    private String read_at;
    private String created_at;
    @Convert(converter = DataBeanParamsConverter.class, columnType = String.class)
    private DataBean data;
    private long user_id;// 这条通知的操作者


    @Generated(hash = 917704485)
    public TSPNotificationBean(Long _id, String id, String read_at, String created_at, DataBean data) {
        this._id = _id;
        this.id = id;
        this.read_at = read_at;
        this.created_at = created_at;
        this.data = data;
    }

    @Generated(hash = 1606766504)
    public TSPNotificationBean() {
    }


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

    public long getUser_id() {
        if (user_id != 0) {
            return user_id;
        }
        if (data != null) {
            switch (data.getChannel()) {
                case NOTIFICATION_KEY_FEED_COMMENTS:
                    break;
                case NOTIFICATION_KEY_FEED_DIGGS:
                    break;
                case NOTIFICATION_KEY_FEED_PINNED_COMMENT:

                    break;

            }
        }
        return user_id;
    }

    public void setUser_id(long user_id) {
        if (user_id != 0) {
            this.user_id = user_id;
        } else {
            this.user_id = getUser_id();
        }

    }

    public static class DataBean implements Serializable {
        private static final long serialVersionUID = 6464434974795251975L;
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

    /**
     * list<DataBean> 转 String 形式存入数据库
     */
    public static class DataBeanParamsConverter implements PropertyConverter<DataBean, String> {

        @Override
        public DataBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(DataBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }


}
