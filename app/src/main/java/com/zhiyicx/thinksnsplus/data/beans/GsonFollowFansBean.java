package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * @author LiuChao
 * @describe 通过Gson从网络获取粉丝关注列表
 * @date 2017/2/17
 * @contact email:450127106@qq.com
 */

public class GsonFollowFansBean {
    private List<GsonFollowsBean> follows;// 关注列表
    private List<GsonFollowsBean> followeds;// 粉丝列表

    public List<GsonFollowsBean> getFollows() {
        return follows;
    }

    public void setFollows(List<GsonFollowsBean> follows) {
        this.follows = follows;
    }

    public List<GsonFollowsBean> getFolloweds() {
        return followeds;
    }

    public void setFolloweds(List<GsonFollowsBean> followeds) {
        this.followeds = followeds;
    }

    public class GsonFollowsBean {
        private int id;// maxId
        private int user_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
