package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * @author LiuChao
 * @describe 通过Gson从网络获取粉丝关注列表
 * @date 2017/2/17
 * @contact email:450127106@qq.com
 */

public class GsonFollowFansBean {
    private List<FollowFansBean> follows;// 关注列表
    private List<FollowFansBean> followeds;// 粉丝列表

    public List<FollowFansBean> getFollows() {
        return follows;
    }

    public void setFollows(List<FollowFansBean> follows) {
        this.follows = follows;
    }

    public List<FollowFansBean> getFolloweds() {
        return followeds;
    }

    public void setFolloweds(List<FollowFansBean> followeds) {
        this.followeds = followeds;
    }

}
