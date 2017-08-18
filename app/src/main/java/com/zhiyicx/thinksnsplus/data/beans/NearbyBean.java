package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * @Describe {@see https://github.com/slimkit/around-amap}
 * @Author Jungle68
 * @Date 2017/8/17
 * @Contact master.jungle68@gmail.com
 */
public class NearbyBean extends BaseListBean {


    /**
     * _id : 18
     * _location : 104.062056,30.566673
     * _name : 成都智艺创想科技有限公司
     * _address : 成都市武侯区环球中心S2区7-1-731成都智艺创想科技有限公司
     * user_id : 4
     * _createtime : 2017-08-16 23:26:53
     * _updatetime : 2017-08-16 23:27:13
     * _province : 四川省
     * _city : 成都市
     * _district : 武侯区
     * _distance : 0
     */
    private Long id; // 主键，没有具体意义
    private String _id;
    private String _location;
    private String _name;
    private String _address;
    private String user_id;
    private UserInfoBean user;
    private String _createtime;
    private String _updatetime;
    private String _province;
    private String _city;
    private String _district;
    private String _distance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_location() {
        return _location;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String get_createtime() {
        return _createtime;
    }

    public void set_createtime(String _createtime) {
        this._createtime = _createtime;
    }

    public String get_updatetime() {
        return _updatetime;
    }

    public void set_updatetime(String _updatetime) {
        this._updatetime = _updatetime;
    }

    public String get_province() {
        return _province;
    }

    public void set_province(String _province) {
        this._province = _province;
    }

    public String get_city() {
        return _city;
    }

    public void set_city(String _city) {
        this._city = _city;
    }

    public String get_district() {
        return _district;
    }

    public void set_district(String _district) {
        this._district = _district;
    }

    public String get_distance() {
        return _distance;
    }

    public void set_distance(String _distance) {
        this._distance = _distance;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }
}
