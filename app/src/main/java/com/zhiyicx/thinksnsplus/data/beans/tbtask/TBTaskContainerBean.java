package com.zhiyicx.thinksnsplus.data.beans.tbtask;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/3
 * @contact master.jungle68@gmail.com
 */
public class TBTaskContainerBean {

    /**
     * invite_friend : {"title":"邀请好友","desc":"邀请一名好友得100TBMark","reward":100}
     * invite_code : {"title":"填写邀请码","desc":"填写邀请码获取8个TBMark","reward":100}
     * <p>
     * "certified":1,
     * " ":false
     */
    private ExtralBean extras;

    private List<TBTaskBean> tasks;
    private int certified;
    private InviteCodeBean invite_code;


    public List<TBTaskBean> getTasks() {
        return tasks;
    }

    public void setTasks(List<TBTaskBean> tasks) {
        this.tasks = tasks;
    }

    public int getCertified() {
        return certified;
    }

    public void setCertified(int certified) {
        this.certified = certified;
    }

    public InviteCodeBean isInvite_code() {
        return invite_code;
    }

    public void setInvite_code(InviteCodeBean invite_code) {
        this.invite_code = invite_code;
    }

    public ExtralBean getExtras() {
        return extras;
    }

    public void setExtras(ExtralBean extras) {
        this.extras = extras;
    }

    public static class ExtralBean {
        private InviteBean invite_friend;
        @SerializedName("invite_code")
        private InviteBean invite_codeX;

        public InviteBean getInvite_friend() {
            return invite_friend;
        }

        public void setInvite_friend(InviteBean invite_friend) {
            this.invite_friend = invite_friend;
        }

        public InviteBean getInvite_codeX() {
            return invite_codeX;
        }

        public void setInvite_codeX(InviteBean invite_codeX) {
            this.invite_codeX = invite_codeX;
        }

    }

    public static class InviteBean {
        /**
         * title : 邀请好友
         * desc : 邀请一名好友得100TBMark
         * reward : 100
         */

        private String title;
        private String desc;
        private int reward;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }
    }

    public static class InviteCodeBean {
        private long user_id;
        private long parent_user_id;
        private String created_at;
        private String updated_at;

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public long getParent_user_id() {
            return parent_user_id;
        }

        public void setParent_user_id(long parent_user_id) {
            this.parent_user_id = parent_user_id;
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

}
