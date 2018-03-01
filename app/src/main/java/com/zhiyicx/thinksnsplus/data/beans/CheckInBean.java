package com.zhiyicx.thinksnsplus.data.beans;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.cache.CacheBean;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */
public class CheckInBean extends CacheBean {


    /**
     * "rank_users": [
     * {
     * "id": 1,
     * "name": "Seven",
     * "bio": "Seven 的个人传记",
     * "sex": 2,
     * "location": "成都 中国",
     * "created_at": "2017-06-02 08:43:54",
     * "updated_at": "2017-07-25 03:59:39",
     * "avatar": "http://plus.io/api/v2/users/1/avatar",
     * "bg": "http://plus.io/storage/user-bg/000/000/000/01.png",
     * "verified": null,
     * "extra": {
     * "user_id": 1,
     * "likes_count": 0,
     * "comments_count": 8,
     * "followers_count": 0,
     * "followings_count": 1,
     * "updated_at": "2017-08-11 01:32:36",
     * "feeds_count": 0,
     * "questions_count": 5,
     * "answers_count": 3,
     * "checkin_count": 2,
     * "last_checkin_count": 2
     * }
     * }
     * ],
     * checked_in : true
     * checkin_count : 2
     * last_checkin_count : 2
     * attach_balance : 0
     * {
     * "check_in_status": true,// 今日签到状态
     * "check_in_reward": 1,// 签到奖励
     * "check_in_count": 3 // 连续签到天数
     * }
     */
    private List<UserInfoBean> rank_users;
    @SerializedName(value = "checked_in", alternate = "check_in_status")
    private boolean checked_in;
    private int check_in_reward;
    private int checkin_count;
    @SerializedName(value = "last_checkin_count", alternate = "check_in_count")
    private int last_checkin_count;
    private int attach_balance;

    public List<UserInfoBean> getRank_users() {
        return rank_users;
    }

    public void setRank_users(List<UserInfoBean> rank_users) {
        this.rank_users = rank_users;
    }

    public boolean isChecked_in() {
        return checked_in;
    }

    public void setChecked_in(boolean checked_in) {
        this.checked_in = checked_in;
    }

    public int getCheckin_count() {
        return checkin_count;
    }

    public void setCheckin_count(int checkin_count) {
        this.checkin_count = checkin_count;
    }

    public int getLast_checkin_count() {
        return last_checkin_count;
    }

    public void setLast_checkin_count(int last_checkin_count) {
        this.last_checkin_count = last_checkin_count;
    }

    public int getAttach_balance() {
        return attach_balance;
    }

    public void setAttach_balance(int attach_balance) {
        this.attach_balance = attach_balance;
    }

    public int getCheck_in_reward() {
        return check_in_reward;
    }

    public void setCheck_in_reward(int check_in_reward) {
        this.check_in_reward = check_in_reward;
    }
}
