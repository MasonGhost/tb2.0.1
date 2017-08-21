package com.zhiyicx.thinksnsplus.data.beans.qa;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/18
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class QASearchHistoryBean {
    public static final int TYPE_QA = 0;
    public static final int TYPE_QA_TOPIC = 1;
    @Id
    private Long id;
    private String content;
    private int type;

    @Generated(hash = 296145513)
    public QASearchHistoryBean(Long id, String content, int type) {
        this.id = id;
        this.content = content;
        this.type = type;
    }

    @Generated(hash = 2135059226)
    public QASearchHistoryBean() {
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
}
