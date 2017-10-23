package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * @Describe 为读书统计总模型  {@see  https://slimkit.github.io/plus-docs/v2/core/users/unread}
 * @Author Jungle68
 * @Date 2017/10/23
 * @Contact master.jungle68@gmail.com
 */
public class UnReadNotificaitonBean {
    private Long user_id;
    private int unread_comments_count;
    private int unread_likes_count;
    private String created_at;
    private String updated_at;
    private List<UnreadCountBean> comments;
    private List<UnreadCountBean> likes;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public int getUnread_comments_count() {
        return unread_comments_count;
    }

    public void setUnread_comments_count(int unread_comments_count) {
        this.unread_comments_count = unread_comments_count;
    }

    public int getUnread_likes_count() {
        return unread_likes_count;
    }

    public void setUnread_likes_count(int unread_likes_count) {
        this.unread_likes_count = unread_likes_count;
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

    public List<UnreadCountBean> getComments() {
        return comments;
    }

    public void setComments(List<UnreadCountBean> comments) {
        this.comments = comments;
    }

    public List<UnreadCountBean> getLikes() {
        return likes;
    }

    public void setLikes(List<UnreadCountBean> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "UnReadNotificaitonBean{" +
                "user_id=" + user_id +
                ", unread_comments_count=" + unread_comments_count +
                ", unread_likes_count=" + unread_likes_count +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", comments=" + comments +
                ", likes=" + likes +
                '}';
    }
}
