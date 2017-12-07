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
    private int type = TYPE_DEFAULT;


    public CircleSearchHistoryBean(String content, int type) {
        this.content = content;
        this.type = type;
        this.create_time = System.currentTimeMillis();
    }

    @Generated(hash = 1596019093)
    public CircleSearchHistoryBean(Long id, String content, long create_time,
            int type) {
        this.id = id;
        this.content = content;
        this.create_time = create_time;
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
                ", type=" + type +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
