package com.zhiyicx.thinksnsplus.data.beans.qa;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/18
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class QASearchHistoryBean {
    public static final int TYPE_DEFAULT = 0; // 操作按钮，查看全部，展开等
    public static final int TYPE_QA = 1;
    public static final int TYPE_QA_TOPIC = 2;
    @Id(autoincrement = true)
    private Long id;
    private String content;
    private long create_time;
    private int type = TYPE_DEFAULT;

    public QASearchHistoryBean(String content, int type) {
        this.content = content;
        this.type = type;
        this.create_time=System.currentTimeMillis();
    }

    @Generated(hash = 2062135283)
    public QASearchHistoryBean(Long id, String content, long create_time,
            int type) {
        this.id = id;
        this.content = content;
        this.create_time = create_time;
        this.type = type;
    }

    @Generated(hash = 2135059226)
    public QASearchHistoryBean() {
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "QASearchHistoryBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", create_time=" + create_time +
                ", type=" + type +
                '}';
    }
}
