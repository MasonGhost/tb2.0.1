package com.zhiyicx.thinksnsplus.data.beans;

import android.support.annotation.NonNull;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.utils.hintsidebar.HintSideBarUtils;


public class HintSideBarUserBean extends BaseListBean implements Comparable {
    private String id;
    private String userName;
    private String avatar;
    private char headLetter;

    public HintSideBarUserBean(String id, String avatar, String userName) {
        this.id = id;
        this.avatar = avatar;
        this.userName = userName;
        headLetter = HintSideBarUtils.getHeadChar(userName);
    }

    public String getUserName() {
        return userName;
    }

    public char getHeadLetter() {
        return headLetter;
    }

    public String getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (this == object) {
            return true;
        }
        HintSideBarUserBean that = (HintSideBarUserBean) object;
        return getUserName().equals(that.getUserName());
    }

    @Override
    public int compareTo(@NonNull Object object) {
        if (!(object instanceof HintSideBarUserBean)) {
            throw new ClassCastException();
        }
        HintSideBarUserBean that = (HintSideBarUserBean) object;
        if (getHeadLetter() == '#') {
            if (that.getHeadLetter() == '#') {
                return 0;
            }
            return 1;
        }
        if (that.getHeadLetter() == '#') {
            return -1;
        } else if (that.getHeadLetter() > getHeadLetter()) {
            return -1;
        } else if (that.getHeadLetter() == getHeadLetter()) {
            return 0;
        }
        return 1;
    }

}
