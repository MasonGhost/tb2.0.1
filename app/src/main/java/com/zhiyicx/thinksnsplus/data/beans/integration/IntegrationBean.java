package com.zhiyicx.thinksnsplus.data.beans.integration;

import java.io.Serializable;

/**
 * @Describe 积分  doc {https://slimkit.github.io/plus-docs/v2/core/users/#%E6%9B%B4%E6%96%B0%E8%AE%A4%E8%AF%81%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99}
 * @Author Jungle68
 * @Date 2018/1/22
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationBean implements Serializable{

    private static final long serialVersionUID = 6734690561948710212L;
    /**
     * owner_id : 1
     * type : 1
     * sum : 9400
     * created_at : 2018-01-17 06:57:18
     * updated_at : 2018-01-18 06:57:24
     */

    private int owner_id;
    private int type;
    private int sum;
    private String created_at;
    private String updated_at;

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "IntegrationBean{" +
                "owner_id=" + owner_id +
                ", type=" + type +
                ", sum=" + sum +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
