package com.zhiyicx.thinksnsplus.data.beans.circle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/7
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class CircleSearchHistoryBean {
    public static final int TYPE_DEFAULT = 0; // 操作按钮，查看全部，展开等
    public static final int TYPE_CIRCLE = 1;
    public static final int TYPE_CIRCLE_POST = 2;
    @Id(autoincrement = true)
    private Long id;
    private String content;
    private long create_time;
    private boolean isOutSideCircle;
    private int type = TYPE_DEFAULT;


    public CircleSearchHistoryBean(String content, int type) {
        this.content = content;
        this.type = type;
        this.create_time = System.currentTimeMillis();
    }

    public CircleSearchHistoryBean(String content, int type, boolean isOutSideCircle) {
        this.content = content;
        this.type = type;
        this.isOutSideCircle = isOutSideCircle;
        this.create_time = System.currentTimeMillis();
    }

    @Generated(hash = 1783680448)
    public CircleSearchHistoryBean(Long id, String content, long create_time,
            boolean isOutSideCircle, int type) {
        this.id = id;
        this.content = content;
        this.create_time = create_time;
        this.isOutSideCircle = isOutSideCircle;
        this.type = type;
    }

    @Generated(hash = 1365564893)
    public CircleSearchHistoryBean() {
    }

    @Override
    public String toString() {
        return "CircleSearchHistoryBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", create_time=" + create_time +
                ", isOutSideCircle=" + isOutSideCircle +
                ", type=" + type +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getOutSideCircle() {
        return isOutSideCircle;
    }

    public void setOutSideCircle(boolean outSideCircle) {
        isOutSideCircle = outSideCircle;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreate_time() {
        return this.create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getIsOutSideCircle() {
        return this.isOutSideCircle;
    }

    public void setIsOutSideCircle(boolean isOutSideCircle) {
        this.isOutSideCircle = isOutSideCircle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CircleSearchHistoryBean that = (CircleSearchHistoryBean) o;

        if (create_time != that.create_time) {
            return false;
        }
        if (type != that.type) {
            return false;
        }
        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (int) (create_time ^ (create_time >>> 32));
        result = 31 * result + type;
        return result;
    }
}
