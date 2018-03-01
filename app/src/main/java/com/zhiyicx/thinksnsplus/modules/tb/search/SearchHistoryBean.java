package com.zhiyicx.thinksnsplus.modules.tb.search;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Jungle68
 * @describe 搜索历史数据
 * @date 2018/3/1
 * @contact master.jungle68@gmail.com
 */
@Entity
public class SearchHistoryBean {
    public static final String TYPE_DEFAULT = "default";
    public static final String TYPE_MECHAINSIM_USER = "type_mechainsim_user";

    @Id(autoincrement = true)
    private Long id;
    private String type;
    private String content;
    private long create_time;

    public SearchHistoryBean(String string, String typeDefault) {
        this.content = string;
        this.type = typeDefault;
    }

    @Generated(hash = 614018045)
    public SearchHistoryBean(Long id, String type, String content,
            long create_time) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.create_time = create_time;
    }

    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
