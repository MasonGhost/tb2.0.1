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
    private boolean invite_code;


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

    public boolean isInvite_code() {
        return invite_code;
    }

    public void setInvite_code(boolean invite_code) {
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

}
