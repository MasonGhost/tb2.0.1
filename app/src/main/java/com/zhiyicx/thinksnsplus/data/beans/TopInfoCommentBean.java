package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/08/31/9:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopInfoCommentBean extends BaseListBean {

    /**
     * id : 12
     * created_at : 2017-07-27 08:43:33
     * updated_at : 2017-07-27 08:44:20
     * channel : news:comment
     * state : 1
     * raw : 1
     * target : 1
     * user_id : 2
     * target_user : 2
     * amount : 50
     * day : 5
     * cate_id : 1
     * expires_at : 2017-08-01 08:44:20
     * news : {"id":1,"created_at":"2017-07-25 00:00:00","updated_at":"2017-07-27 08:58:02","title":"资讯标题","content":"阿斯顿发生地方爱上地方爱上地方阿斯顿","digg_count":0,"comment_count":1,"hits":1,"from":"1","is_recommend":1,"subject":"潇洒地方","author":"哈哈哈","audit_status":0,"audit_count":0,"user_id":2,"category":{"id":1,"name":"分类1","rank":0},"image":{"id":1,"size":"1920x1080"},"pinned":{"id":13,"created_at":"2017-07-27 09:10:04","updated_at":"2017-07-27 09:10:04","channel":"news","state":1,"raw":0,"target":1,"user_id":2,"target_user":0,"amount":50,"day":5,"cate_id":1,"expires_at":"2017-07-25 00:00:00"}}
     * comment : {"id":1,"user_id":2,"target_user":2,"reply_user":0,"body":"sldkfjalksdjflakjsdflkajsd","commentable_id":1,"commentable_type":"news","created_at":"2017-07-25 00:00:00","updated_at":"2017-07-25 00:00:00"}
     */

    private Long id;
    private String created_at;
    private String updated_at;
    private String channel;
    private int state;
    private int raw;
    private int target;
    private int user_id;
    private int target_user;
    private int amount;
    private int day;
    private int cate_id;
    private String expires_at;
    private NewsBean news;
    private CommentedBean comment;

    @Override
    public Long getMaxId() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTarget_user() {
        return target_user;
    }

    public void setTarget_user(int target_user) {
        this.target_user = target_user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public NewsBean getNews() {
        return news;
    }

    public void setNews(NewsBean news) {
        this.news = news;
    }

    public CommentedBean getComment() {
        return comment;
    }

    public void setComment(CommentedBean comment) {
        this.comment = comment;
    }

    public static class NewsBean {
        /**
         * id : 1
         * created_at : 2017-07-25 00:00:00
         * updated_at : 2017-07-27 08:58:02
         * title : 资讯标题
         * content : 阿斯顿发生地方爱上地方爱上地方阿斯顿
         * digg_count : 0
         * comment_count : 1
         * hits : 1
         * from : 1
         * is_recommend : 1
         * subject : 潇洒地方
         * author : 哈哈哈
         * audit_status : 0
         * audit_count : 0
         * user_id : 2
         * category : {"id":1,"name":"分类1","rank":0}
         * image : {"id":1,"size":"1920x1080"}
         * pinned : {"id":13,"created_at":"2017-07-27 09:10:04","updated_at":"2017-07-27 09:10:04","channel":"news","state":1,"raw":0,"target":1,"user_id":2,"target_user":0,"amount":50,"day":5,"cate_id":1,"expires_at":"2017-07-25 00:00:00"}
         */

        private int id;
        private String created_at;
        private String updated_at;
        private String title;
        private String content;
        private int digg_count;
        private int comment_count;
        private int hits;
        private String from;
        private int is_recommend;
        private String subject;
        private String author;
        private int audit_status;
        private int audit_count;
        private int user_id;
        private CategoryBean category;
        private ImageBean image;
        private PinnedBean pinned;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getDigg_count() {
            return digg_count;
        }

        public void setDigg_count(int digg_count) {
            this.digg_count = digg_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(int audit_status) {
            this.audit_status = audit_status;
        }

        public int getAudit_count() {
            return audit_count;
        }

        public void setAudit_count(int audit_count) {
            this.audit_count = audit_count;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public CategoryBean getCategory() {
            return category;
        }

        public void setCategory(CategoryBean category) {
            this.category = category;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public PinnedBean getPinned() {
            return pinned;
        }

        public void setPinned(PinnedBean pinned) {
            this.pinned = pinned;
        }

        public static class CategoryBean {
            /**
             * id : 1
             * name : 分类1
             * rank : 0
             */

            private int id;
            private String name;
            private int rank;

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

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }
        }

        public static class ImageBean {
            /**
             * id : 1
             * size : 1920x1080
             */

            private int id;
            private String size;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }
        }

        public static class PinnedBean {
            /**
             * id : 13
             * created_at : 2017-07-27 09:10:04
             * updated_at : 2017-07-27 09:10:04
             * channel : news
             * state : 1
             * raw : 0
             * target : 1
             * user_id : 2
             * target_user : 0
             * amount : 50
             * day : 5
             * cate_id : 1
             * expires_at : 2017-07-25 00:00:00
             */

            private int id;
            private String created_at;
            private String updated_at;
            private String channel;
            private int state;
            private int raw;
            private int target;
            private int user_id;
            private int target_user;
            private int amount;
            private int day;
            private int cate_id;
            private String expires_at;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getRaw() {
                return raw;
            }

            public void setRaw(int raw) {
                this.raw = raw;
            }

            public int getTarget() {
                return target;
            }

            public void setTarget(int target) {
                this.target = target;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getTarget_user() {
                return target_user;
            }

            public void setTarget_user(int target_user) {
                this.target_user = target_user;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getCate_id() {
                return cate_id;
            }

            public void setCate_id(int cate_id) {
                this.cate_id = cate_id;
            }

            public String getExpires_at() {
                return expires_at;
            }

            public void setExpires_at(String expires_at) {
                this.expires_at = expires_at;
            }
        }
    }
}
