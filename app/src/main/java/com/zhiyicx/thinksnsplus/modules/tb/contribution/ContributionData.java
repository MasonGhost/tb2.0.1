package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ContributionData extends BaseListBean {
    /**
     * {
     * "user_id": 3,// 用户id
     * "invite_id": 2,// 邀请者id
     * "obtain": 0,// 贡献值
     * "rank": 1, // 排名
     * "inviter": {// 邀请人信息
     * "id": 2,
     * "name": "zhangsan",
     * "bio": null,
     * "sex": 0,
     * "location": null,
     * "created_at": "2018-02-26 08:05:00",
     * "updated_at": "2018-02-26 08:05:00",
     * "avatar": null,
     * "bg": null,
     * "verified": null,
     * "extra": {
     * "user_id": 2,
     * "likes_count": 0,
     * "comments_count": 0,
     * "followers_count": 0,
     * "followings_count": 0,
     * "invite_count": 2,
     * "updated_at": "2018-02-28 13:47:38",
     * "feeds_count": 0,
     * "questions_count": 0,
     * "answers_count": 0,
     * "checkin_count": 12,
     * "last_checkin_count": 1
     * }
     * }
     * }
     */

    private long user_id;
    private long invite_id;
    private int obtain;
    private int rank;
    private UserInfoBean inviter;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getInvite_id() {
        return invite_id;
    }

    public void setInvite_id(long invite_id) {
        this.invite_id = invite_id;
    }

    public int getObtain() {
        return obtain;
    }

    public void setObtain(int obtain) {
        this.obtain = obtain;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public UserInfoBean getInviter() {
        return inviter;
    }

    public void setInviter(UserInfoBean inviter) {
        this.inviter = inviter;
    }
}
