package com.zhiyicx.thinksnsplus.modules.tb.share;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.io.Serializable;

/**
 * @author Jungle68
 * @describe 链
 * @date 2018/2/28
 * @contact master.jungle68@gmail.com
 */
public class DynamicShareBean implements Serializable {
    private static final long serialVersionUID = -5645852412255261880L;
    private String id;
    private UserInfoBean mUserInfoBean;
    private String time;
    private String content;
    private String type;

    public DynamicShareBean(UserInfoBean userInfoBean, String time, String content, String id,String type) {
        mUserInfoBean = userInfoBean;
        this.time = time;
        this.content = content;
        this.id=id;
        this.type=type;
    }

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
