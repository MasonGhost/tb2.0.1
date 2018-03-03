package com.zhiyicx.thinksnsplus.data.beans.tbtask;

import java.util.List;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/3
 * @contact master.jungle68@gmail.com
 */
public class TBTaskBean {

    /**
     * id : 5
     * type : frequency
     * frequency : 10
     * trigger : share
     * reward : true
     * amount : 1
     * name : 分享快讯
     * description : 分享快讯
     * notification : false
     * created_at : 2018-02-28 02:26:56
     * updated_at : 2018-02-28 02:26:56
     * deleted_at : null
     * complete_at : 2018-03-02
     * notes : [{"id":60,"task_id":5,"user_id":2,"created_at":"2018-03-02 09:04:52","updated_at":"2018-03-02 09:04:52"},{"id":61,"task_id":5,
     * "user_id":2,"created_at":"2018-03-02 09:05:06","updated_at":"2018-03-02 09:05:06"},{"id":62,"task_id":5,"user_id":2,"created_at":"2018-03-02
     * 09:05:07","updated_at":"2018-03-02 09:05:07"},{"id":63,"task_id":5,"user_id":2,"created_at":"2018-03-02 09:05:08","updated_at":"2018-03-02
     * 09:05:08"},{"id":64,"task_id":5,"user_id":2,"created_at":"2018-03-02 09:05:08","updated_at":"2018-03-02 09:05:08"},{"id":65,"task_id":5,
     * "user_id":2,"created_at":"2018-03-02 09:05:09","updated_at":"2018-03-02 09:05:09"},{"id":66,"task_id":5,"user_id":2,"created_at":"2018-03-02
     * 09:05:10","updated_at":"2018-03-02 09:05:10"},{"id":67,"task_id":5,"user_id":2,"created_at":"2018-03-02 09:05:11","updated_at":"2018-03-02
     * 09:05:11"},{"id":68,"task_id":5,"user_id":2,"created_at":"2018-03-02 09:05:11","updated_at":"2018-03-02 09:05:11"},{"id":69,"task_id":5,
     * "user_id":2,"created_at":"2018-03-02 09:05:12","updated_at":"2018-03-02 09:05:12"},{"id":70,"task_id":5,"user_id":2,"created_at":"2018-03-02
     * 09:05:13","updated_at":"2018-03-02 09:05:13"},{"id":71,"task_id":5,"user_id":2,"created_at":"2018-03-02 09:05:14","updated_at":"2018-03-02
     * 09:05:14"},{"id":72,"task_id":5,"user_id":2,"created_at":"2018-03-02 09:05:14","updated_at":"2018-03-02 09:05:14"},{"id":73,"task_id":5,
     * "user_id":2,"created_at":"2018-03-02 09:11:55","updated_at":"2018-03-02 09:11:55"},{"id":74,"task_id":5,"user_id":2,"created_at":"2018-03-02
     * 09:11:56","updated_at":"2018-03-02 09:11:56"}]
     */

    public enum  TBTASKTRIGGER{
        INVITE_FIRENDS("INVITE_FIRENDS"),
        EDIT_INVITE_CODE("EDIT_INVITE_CODE"),
        SHARE("share"),
        CERTIFICATION("certification");

        public String value;

        TBTASKTRIGGER(String value) {
            this.value = value;
        }
    }

    private int id;
    private String type;
    private int frequency;
    private String trigger;
    private boolean reward;
    private int amount;
    private String name;
    private String description;
    private boolean notification;
    private String created_at;
    private String updated_at;
    private Object deleted_at;
    private String complete_at;
    private List<NotesBean> notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public boolean isReward() {
        return reward;
    }

    public void setReward(boolean reward) {
        this.reward = reward;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
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

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Object deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getComplete_at() {
        return complete_at;
    }

    public void setComplete_at(String complete_at) {
        this.complete_at = complete_at;
    }

    public List<NotesBean> getNotes() {
        return notes;
    }

    public void setNotes(List<NotesBean> notes) {
        this.notes = notes;
    }

    public static class NotesBean {
        /**
         * id : 60
         * task_id : 5
         * user_id : 2
         * created_at : 2018-03-02 09:04:52
         * updated_at : 2018-03-02 09:04:52
         */

        private int id;
        private int task_id;
        private int user_id;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTask_id() {
            return task_id;
        }

        public void setTask_id(int task_id) {
            this.task_id = task_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
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
