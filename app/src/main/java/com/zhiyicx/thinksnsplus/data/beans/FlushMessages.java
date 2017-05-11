package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/13
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class FlushMessages {

    /**
     * key : comments
     * uids :
     * count : 0
     * time : 2017-04-13 3:31:10
     * max_id : 1
     */
    @Id(autoincrement = true)
    Long id;
    @Unique
    private String key;
    @Convert(converter = UidConveter.class, columnType = String.class)
    private List<Long> uids;
    private int count;
    private String time;
    private long max_id;

    @Generated(hash = 1596072861)
    public FlushMessages(Long id, String key, List<Long> uids, int count, String time,
            long max_id) {
        this.id = id;
        this.key = key;
        this.uids = uids;
        this.count = count;
        this.time = time;
        this.max_id = max_id;
    }

    @Generated(hash = 505793851)
    public FlushMessages() {
    }

    @Override
    public String toString() {
        return "FlushMessages{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", uids=" + uids +
                ", count=" + count +
                ", time='" + time + '\'' +
                ", max_id=" + max_id +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Long> getUids() {
        return this.uids;
    }

    public void setUids(List<Long> uids) {
        this.uids = uids;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getMax_id() {
        return this.max_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }

    /**
     * list<Integer></> 转 String 形式存入数据库
     */
    public static class UidConveter implements PropertyConverter<List<Long>, String> {
        @Override
        public List<Long> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<Long> params) {
            if (params == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(params);
        }
    }
}
