package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/4/20.
 */
@Entity
public class CandyWalletOrderBean extends BaseListBean {
    @Id
    private Long id;
    private String title;
    private int candy_cat_id;
    private String count;
    private String charge;
    private int type;
    private int status;
    private String created_at;
    private String updated_at;

    @Generated(hash = 1677241017)
    public CandyWalletOrderBean(Long id, String title, int candy_cat_id,
            String count, String charge, int type, int status, String created_at,
            String updated_at) {
        this.id = id;
        this.title = title;
        this.candy_cat_id = candy_cat_id;
        this.count = count;
        this.charge = charge;
        this.type = type;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @Generated(hash = 1438099247)
    public CandyWalletOrderBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCandy_cat_id() {
        return candy_cat_id;
    }

    public void setCandy_cat_id(int candy_cat_id) {
        this.candy_cat_id = candy_cat_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
