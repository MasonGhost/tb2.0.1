package com.zhiyicx.thinksnsplus.data.beans.tbmerchianmessage;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

/**
 * @author Jungle68
 * @describe https://github.com/zhiyicx/plus-tbm/blob/tbm-dev/docs/api-pushNews.md
 * @date 2018/3/23
 * @contact master.jungle68@gmail.com
 */
public class MerchianMassageBean extends BaseListBean {


    /**
     * data : [{"id":1,"title":"fhfjng","content":"gkljl","text_content":null,"created_at":"2018-03-22 18:22:55","type":"news","image":{"id":1,
     * "size":"500x332"},"feed_content":"二氧化碳加油卡"}]
     * feedMin : 2
     * newsMin : 1
     */

    private int feedMin;
    private int newsMin;
    private List<DataBean> data;

    public int getFeedMin() {
        return feedMin;
    }

    public void setFeedMin(int feedMin) {
        this.feedMin = feedMin;
    }

    public int getNewsMin() {
        return newsMin;
    }

    public void setNewsMin(int newsMin) {
        this.newsMin = newsMin;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean extends BaseListBean {
        /**
         * id : 1
         * title : fhfjng
         * content : gkljl
         * text_content : null
         * created_at : 2018-03-22 18:22:55
         * type : news
         * image : {"id":1,"size":"500x332"}
         * feed_content : 二氧化碳加油卡
         */

        private int id;
        private String title;
        private String content;
        private String text_content;
        private String subject;
        private String created_at;
        private String type;
        private ImageBean image;
        private String feed_content;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getText_content() {
            return text_content;
        }

        public void setText_content(String text_content) {
            this.text_content = text_content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public String getFeed_content() {
            return feed_content;
        }

        public void setFeed_content(String feed_content) {
            this.feed_content = feed_content;
        }

        public static class ImageBean implements android.os.Parcelable {
            /**
             * id : 1
             * size : 500x332
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.size);
            }

            public ImageBean() {
            }

            protected ImageBean(Parcel in) {
                this.id = in.readInt();
                this.size = in.readString();
            }

            public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
                @Override
                public ImageBean createFromParcel(Parcel source) {
                    return new ImageBean(source);
                }

                @Override
                public ImageBean[] newArray(int size) {
                    return new ImageBean[size];
                }
            };
        }

        public DataBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.id);
            dest.writeString(this.title);
            dest.writeString(this.content);
            dest.writeString(this.text_content);
            dest.writeString(this.subject);
            dest.writeString(this.created_at);
            dest.writeString(this.type);
            dest.writeParcelable(this.image, flags);
            dest.writeString(this.feed_content);
        }

        protected DataBean(Parcel in) {
            super(in);
            this.id = in.readInt();
            this.title = in.readString();
            this.content = in.readString();
            this.text_content = in.readString();
            this.subject = in.readString();
            this.created_at = in.readString();
            this.type = in.readString();
            this.image = in.readParcelable(ImageBean.class.getClassLoader());
            this.feed_content = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.feedMin);
        dest.writeInt(this.newsMin);
        dest.writeTypedList(this.data);
    }

    public MerchianMassageBean() {
    }

    protected MerchianMassageBean(Parcel in) {
        super(in);
        this.feedMin = in.readInt();
        this.newsMin = in.readInt();
        this.data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<MerchianMassageBean> CREATOR = new Creator<MerchianMassageBean>() {
        @Override
        public MerchianMassageBean createFromParcel(Parcel source) {
            return new MerchianMassageBean(source);
        }

        @Override
        public MerchianMassageBean[] newArray(int size) {
            return new MerchianMassageBean[size];
        }
    };
}
