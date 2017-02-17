package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * @author LiuChao
 * @describe 通过Gson从网络获取粉丝关注列表
 * @date 2017/2/17
 * @contact email:450127106@qq.com
 */

public class GsonFollowFansBean {
    private List<GsonFollowsBean> follows;

    public List<GsonFollowsBean> getFollows() {
        return follows;
    }

    public void setFollows(List<GsonFollowsBean> follows) {
        this.follows = follows;
    }

    class GsonFollowsBean {
        private int id;
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
