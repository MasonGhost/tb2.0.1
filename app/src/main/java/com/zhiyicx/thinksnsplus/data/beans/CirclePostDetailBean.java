package com.zhiyicx.thinksnsplus.data.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/12/01/16:20
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostDetailBean implements Serializable{

    private static final long serialVersionUID = 7637237373642887996L;
    /**
     * id : 81
     * group_id : 1
     * user_id : 1
     * title : 这是帖11
     * body : 这是帖子
     * summary :
     * likes_count : 0
     * comments_count : 0
     * views_count : 0
     * created_at : 2017-11-28 06:46:02
     * updated_at : 2017-11-28 07:16:46
     * liked : false
     * collected : false
     * group : {"id":1,"name":"哈哈哈","user_id":1,"category_id":1,"location":null,"longitude":null,"latitude":null,"geo_hash":null,"allow_feed":0,"mode":"public","money":0,"summary":"简介\n","notice":"这是公告","users_count":1,"posts_count":47,"audit":1,"created_at":null,"updated_at":"2017-11-28 07:12:20"}
     * user : {"id":1,"name":"admin","bio":null,"sex":2,"location":"四川省 巴中市 南江县","created_at":"2017-10-23 01:17:34","updated_at":"2017-11-15 07:36:17","avatar":"http://thinksns-plus.dev/api/v2/users/1/avatar","bg":null,"verified":{"type":"user","icon":"http://thinksns-plus.dev/storage/certifications/000/000/0us/er.png","description":"1111"},"extra":{"user_id":1,"likes_count":5,"comments_count":3,"followers_count":0,"followings_count":6,"updated_at":"2017-11-27 07:25:04","feeds_count":8,"questions_count":2,"answers_count":0,"checkin_count":7,"last_checkin_count":1}}
     */

    private Long id;
    private long group_id;
    private int user_id;
    private String title;
    private String body;
    private String summary;
    private int likes_count;
    private int comments_count;
    private int views_count;
    private String created_at;
    private String updated_at;
    private boolean liked;
    private boolean collected;
    private GroupBean group;
    private UserBean user;
    private boolean pinned ;
    private List<CirclePostCommentBean> comments;
    private List<PostDigListBean> digs;

    public List<PostDigListBean> getDigs() {
        return digs;
    }

    public void setDigs(List<PostDigListBean> digs) {
        this.digs = digs;
    }

    public List<CirclePostCommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CirclePostCommentBean> comments) {
        this.comments = comments;
    }

    public boolean hasPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
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

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean getCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public GroupBean getGroup() {
        return group;
    }

    public void setGroup(GroupBean group) {
        this.group = group;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class GroupBean {
        /**
         * id : 1
         * name : 哈哈哈
         * user_id : 1
         * category_id : 1
         * location : null
         * longitude : null
         * latitude : null
         * geo_hash : null
         * allow_feed : 0
         * mode : public
         * money : 0
         * summary : 简介

         * notice : 这是公告
         * users_count : 1
         * posts_count : 47
         * audit : 1
         * created_at : null
         * updated_at : 2017-11-28 07:12:20
         */

        private int id;
        private String name;
        private int user_id;
        private int category_id;
        private Object location;
        private Object longitude;
        private Object latitude;
        private Object geo_hash;
        private int allow_feed;
        private String mode;
        private int money;
        private String summary;
        private String notice;
        private int users_count;
        private int posts_count;
        private int audit;
        private Object created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
            this.location = location;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public Object getGeo_hash() {
            return geo_hash;
        }

        public void setGeo_hash(Object geo_hash) {
            this.geo_hash = geo_hash;
        }

        public int getAllow_feed() {
            return allow_feed;
        }

        public void setAllow_feed(int allow_feed) {
            this.allow_feed = allow_feed;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public int getUsers_count() {
            return users_count;
        }

        public void setUsers_count(int users_count) {
            this.users_count = users_count;
        }

        public int getPosts_count() {
            return posts_count;
        }

        public void setPosts_count(int posts_count) {
            this.posts_count = posts_count;
        }

        public int getAudit() {
            return audit;
        }

        public void setAudit(int audit) {
            this.audit = audit;
        }

        public Object getCreated_at() {
            return created_at;
        }

        public void setCreated_at(Object created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }

    public static class UserBean {
        /**
         * id : 1
         * name : admin
         * bio : null
         * sex : 2
         * location : 四川省 巴中市 南江县
         * created_at : 2017-10-23 01:17:34
         * updated_at : 2017-11-15 07:36:17
         * avatar : http://thinksns-plus.dev/api/v2/users/1/avatar
         * bg : null
         * verified : {"type":"user","icon":"http://thinksns-plus.dev/storage/certifications/000/000/0us/er.png","description":"1111"}
         * extra : {"user_id":1,"likes_count":5,"comments_count":3,"followers_count":0,"followings_count":6,"updated_at":"2017-11-27 07:25:04","feeds_count":8,"questions_count":2,"answers_count":0,"checkin_count":7,"last_checkin_count":1}
         */

        private int id;
        private String name;
        private Object bio;
        private int sex;
        private String location;
        private String created_at;
        private String updated_at;
        private String avatar;
        private Object bg;
        private VerifiedBean verified;
        private ExtraBean extra;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getBio() {
            return bio;
        }

        public void setBio(Object bio) {
            this.bio = bio;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Object getBg() {
            return bg;
        }

        public void setBg(Object bg) {
            this.bg = bg;
        }

        public VerifiedBean getVerified() {
            return verified;
        }

        public void setVerified(VerifiedBean verified) {
            this.verified = verified;
        }

        public ExtraBean getExtra() {
            return extra;
        }

        public void setExtra(ExtraBean extra) {
            this.extra = extra;
        }

        public static class VerifiedBean {
            /**
             * type : user
             * icon : http://thinksns-plus.dev/storage/certifications/000/000/0us/er.png
             * description : 1111
             */

            private String type;
            private String icon;
            private String description;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class ExtraBean {
            /**
             * user_id : 1
             * likes_count : 5
             * comments_count : 3
             * followers_count : 0
             * followings_count : 6
             * updated_at : 2017-11-27 07:25:04
             * feeds_count : 8
             * questions_count : 2
             * answers_count : 0
             * checkin_count : 7
             * last_checkin_count : 1
             */

            private int user_id;
            private int likes_count;
            private int comments_count;
            private int followers_count;
            private int followings_count;
            private String updated_at;
            private int feeds_count;
            private int questions_count;
            private int answers_count;
            private int checkin_count;
            private int last_checkin_count;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getLikes_count() {
                return likes_count;
            }

            public void setLikes_count(int likes_count) {
                this.likes_count = likes_count;
            }

            public int getComments_count() {
                return comments_count;
            }

            public void setComments_count(int comments_count) {
                this.comments_count = comments_count;
            }

            public int getFollowers_count() {
                return followers_count;
            }

            public void setFollowers_count(int followers_count) {
                this.followers_count = followers_count;
            }

            public int getFollowings_count() {
                return followings_count;
            }

            public void setFollowings_count(int followings_count) {
                this.followings_count = followings_count;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public int getFeeds_count() {
                return feeds_count;
            }

            public void setFeeds_count(int feeds_count) {
                this.feeds_count = feeds_count;
            }

            public int getQuestions_count() {
                return questions_count;
            }

            public void setQuestions_count(int questions_count) {
                this.questions_count = questions_count;
            }

            public int getAnswers_count() {
                return answers_count;
            }

            public void setAnswers_count(int answers_count) {
                this.answers_count = answers_count;
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
        }
    }
}
