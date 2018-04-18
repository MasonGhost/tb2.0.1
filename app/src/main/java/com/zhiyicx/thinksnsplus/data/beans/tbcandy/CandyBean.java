package com.zhiyicx.thinksnsplus.data.beans.tbcandy;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */
public class CandyBean extends BaseListBean {
    private int id;
    private String candy_id;
    private int candy_cat_id;
    private String name;
    private int receipt_pic;
    private int candy_num;
    private int time_line;
    private String start_at;
    private String end_at;
    private CandyDataBean data;
    private int status;
    private String created_at;
    private String updated_at;
    private int tbmark;
    private int total_tbmark;
    private int end_sec;
    private CandyCateBean candy_cate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCandy_id() {
        return candy_id;
    }

    public void setCandy_id(String candy_id) {
        this.candy_id = candy_id;
    }

    public int getCandy_cat_id() {
        return candy_cat_id;
    }

    public void setCandy_cat_id(int candy_cat_id) {
        this.candy_cat_id = candy_cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReceipt_pic() {
        return receipt_pic;
    }

    public void setReceipt_pic(int receipt_pic) {
        this.receipt_pic = receipt_pic;
    }

    public int getCandy_num() {
        return candy_num;
    }

    public void setCandy_num(int candy_num) {
        this.candy_num = candy_num;
    }

    public int getTime_line() {
        return time_line;
    }

    public void setTime_line(int time_line) {
        this.time_line = time_line;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public CandyDataBean getData() {
        return data;
    }

    public void setData(CandyDataBean data) {
        this.data = data;
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

    public int getTbmark() {
        return tbmark;
    }

    public void setTbmark(int tbmark) {
        this.tbmark = tbmark;
    }

    public int getTotal_tbmark() {
        return total_tbmark;
    }

    public void setTotal_tbmark(int total_tbmark) {
        this.total_tbmark = total_tbmark;
    }

    public int getEnd_sec() {
        return end_sec;
    }

    public void setEnd_sec(int end_sec) {
        this.end_sec = end_sec;
    }

    public CandyCateBean getCandy_cate() {
        return candy_cate;
    }

    public void setCandy_cate(CandyCateBean candy_cate) {
        this.candy_cate = candy_cate;
    }

    @Override
    public String toString() {
        return "CandyBean{" +
                "id=" + id +
                ", candy_id=" + candy_id + '\'' +
                ", candy_cat_id='" + candy_cat_id +
                ", name='" + name + '\'' +
                ", receipt_pic=" + receipt_pic +
                ", candy_num=" + candy_num +
                ", time_line=" + time_line +
                ", start_at=" + start_at + '\'' +
                ", end_at=" + end_at + '\'' +
                ", data=" + data +
                ", status=" + status +
                ", created_at=" + created_at + '\'' +
                ", updated_at=" + updated_at + '\'' +
                ", tbmark=" + tbmark +
                ", total_tbmark=" + total_tbmark +
                ", end_sec=" + end_sec +
                ", candy_cate=" + candy_cate +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.candy_id);
        dest.writeString(this.name);
        dest.writeInt(this.receipt_pic);
        dest.writeInt(this.candy_num);
        dest.writeInt(this.time_line);
        dest.writeString(this.start_at);
        dest.writeString(this.end_at);
        dest.writeParcelable(this.data, flags);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeInt(this.tbmark);
        dest.writeInt(this.total_tbmark);
        dest.writeInt(this.end_sec);
        dest.writeParcelable(this.candy_cate, flags);
    }

}
