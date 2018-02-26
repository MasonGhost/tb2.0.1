package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Describe detail to @see{
 * https://github.com/slimkit/thinksns-plus/blob/master/docs/zh-CN/api2/notifications.md
 * https://github.com/zhiyicx/thinksns-plus-document/blob/master/Summary/notifications.md#%E8%AF%84%E8%AE%BA%E5%8A%A8%E6%80%81
 * }
 * @Author Jungle68
 * @Date 2017/7/11
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class TSPNotificationBean extends BaseListBean {


    /**
     * id : 98aaae93-9d9e-446e-b894-691569b686b5
     * read_at : 2017-07-11 04:23:08
     * data : {"channel":"feed:pinned-comment","target":1,"content":"我是测试消息","extra":null}
     * created_at : 2017-07-10 04:23:08
     */
    @Id
    private Long _id;
    @Unique
    private String id;
    private String read_at;
    private String created_at;
    @Convert(converter = DataBeanParamsConverter.class, columnType = String.class)
    private DataBean data;
    @Transient
    private UserInfoBean userInfo;
    @Transient
    private boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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

    public UserInfoBean getUserInfo() {
        if (userInfo == null && data != null) {
            try {
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(gson.toJson(data.getExtra()));
                if (jsonObject.has("user")) {
                    JSONObject userStr = jsonObject.getJSONObject("user");
                    userInfo = gson.fromJson(userStr.toString(), UserInfoBean.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class DataBean implements Serializable {
        private static final long serialVersionUID = 6464434974795251975L;
        /**
         * channel : feed:pinned-comment
         * target : 1
         * content : 我是测试消息
         * extra : null
         */
        /**
         * 辛辛苦苦复制半天 总不能不要了吧 先写在这儿
         * 参考 ：https://github.com/zhiyicx/thinksns-plus-document/blob/master/Summary/notifications
         * .md#%E7%94%B3%E8%AF%B7%E8%B5%84%E8%AE%AF%E7%BD%AE%E9%A1%B6
         * 分类：
         * 1、user:reward 打赏
         * 2、paid:xxxxx 付费截点
         * 3、feed:comment 评论
         * 4、feed:comment-reply 被回复
         * 5、feed:pinned-comment 他人在自己发布的内容中申请评论置顶
         * 6、feed:digg 点赞通知
         * 7、music:comment-reply 有回复者时，被回复者通知
         * 8、music:special-comment-reply 专辑 有回复者时，被回复者通知
         * 9、news:comment 资讯评论
         * 10、news:comment-reply 资讯被回复
         * 11、news:pinned-comment 他人在资讯评论申请置顶，通过，驳回
         * 12、news:pinned-news 申请资讯置顶
         * 13、news:reward 咨询的打赏 被打赏
         * 14、question:answer 被邀请者回答时，问题发起者消息 其他回答时，问题发起者消息
         * 15、question:comment 问题被评论
         * 16、question:comment-reply 问题的评论被回复
         * 17、answer:comment 答案被评论
         * 18、answer:comment-reply 答案被回复
         * 19、question:answer-adoption 答案被采纳
         * 20、question 邀请回答
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

        public String getFridendlyContent() {
            String friendlyContent = content;

            friendlyContent = friendlyContent.replaceAll(MarkdownConfig.NETSITE_FORMAT, MarkdownConfig.LINK_EMOJI + Link
                    .DEFAULT_NET_SITE);
            return friendlyContent;
        }

        public Object getExtra() {
            return extra;
        }

        public void setExtra(Object extra) {
            this.extra = extra;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "channel='" + channel + '\'' +
                    ", target=" + target +
                    ", content='" + content + '\'' +
                    ", extra=" + extra +
                    '}';
        }
    }

    /**
     * list<DataBean> 转 String 形式存入数据库
     */
    public static class DataBeanParamsConverter extends BaseConvert<DataBean> {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TSPNotificationBean that = (TSPNotificationBean) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this._id);
        dest.writeString(this.id);
        dest.writeString(this.read_at);
        dest.writeString(this.created_at);
        dest.writeSerializable(this.data);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeByte(this.isOpen ? (byte) 1 : (byte) 0);
    }

    public TSPNotificationBean() {
    }

    protected TSPNotificationBean(Parcel in) {
        super(in);
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readString();
        this.read_at = in.readString();
        this.created_at = in.readString();
        this.data = (DataBean) in.readSerializable();
        this.userInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.isOpen = in.readByte() != 0;
    }

    @Generated(hash = 917704485)
    public TSPNotificationBean(Long _id, String id, String read_at, String created_at, DataBean data) {
        this._id = _id;
        this.id = id;
        this.read_at = read_at;
        this.created_at = created_at;
        this.data = data;
    }

    public static final Creator<TSPNotificationBean> CREATOR = new Creator<TSPNotificationBean>() {
        @Override
        public TSPNotificationBean createFromParcel(Parcel source) {
            return new TSPNotificationBean(source);
        }

        @Override
        public TSPNotificationBean[] newArray(int size) {
            return new TSPNotificationBean[size];
        }
    };
}
