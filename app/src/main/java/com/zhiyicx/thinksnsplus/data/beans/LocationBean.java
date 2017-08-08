package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/8
 * @Contact master.jungle68@gmail.com
 */
public class LocationBean extends BaseListBean{

    /**
     * items : [{"id":2508,"name":"成都市","pid":2507,"extends":"","created_at":"2017-06-02 08:44:10","updated_at":"2017-06-02 08:44:10"}]
     * tree : {"id":2507,"name":"四川省","pid":1,"extends":"","created_at":"2017-06-02 08:44:10","updated_at":"2017-06-02 08:44:10","parent":{"id":1,"name":"中国","pid":0,"extends":"3","created_at":"2017-06-02 08:43:54","updated_at":"2017-06-02 08:43:54","parent":null}}
     */

    private TreeBean tree;
    private List<ItemsBean> items;

    public TreeBean getTree() {
        return tree;
    }

    public void setTree(TreeBean tree) {
        this.tree = tree;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class TreeBean implements android.os.Parcelable {
        /**
         * id : 2507
         * name : 四川省
         * pid : 1
         * extends :
         * created_at : 2017-06-02 08:44:10
         * updated_at : 2017-06-02 08:44:10
         * parent : {"id":1,"name":"中国","pid":0,"extends":"3","created_at":"2017-06-02 08:43:54","updated_at":"2017-06-02 08:43:54","parent":null}
         */

        private int id;
        private String name;
        private int pid;
        @SerializedName("extends")
        private String extendsX;
        private String created_at;
        private String updated_at;
        private ParentBean parent;

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

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getExtendsX() {
            return extendsX;
        }

        public void setExtendsX(String extendsX) {
            this.extendsX = extendsX;
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

        public ParentBean getParent() {
            return parent;
        }

        public void setParent(ParentBean parent) {
            this.parent = parent;
        }

        public static class ParentBean implements android.os.Parcelable {
            /**
             * id : 1
             * name : 中国
             * pid : 0
             * extends : 3
             * created_at : 2017-06-02 08:43:54
             * updated_at : 2017-06-02 08:43:54
             * parent : null
             */

            private int id;
            private String name;
            private int pid;
            @SerializedName("extends")
            private String extendsX;
            private String created_at;
            private String updated_at;
            private ParentBean parent;

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

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public String getExtendsX() {
                return extendsX;
            }

            public void setExtendsX(String extendsX) {
                this.extendsX = extendsX;
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

            public ParentBean getParent() {
                return parent;
            }

            public void setParent(ParentBean parent) {
                this.parent = parent;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.name);
                dest.writeInt(this.pid);
                dest.writeString(this.extendsX);
                dest.writeString(this.created_at);
                dest.writeString(this.updated_at);
                dest.writeParcelable(this.parent, flags);
            }

            public ParentBean() {
            }

            protected ParentBean(Parcel in) {
                this.id = in.readInt();
                this.name = in.readString();
                this.pid = in.readInt();
                this.extendsX = in.readString();
                this.created_at = in.readString();
                this.updated_at = in.readString();
                this.parent = in.readParcelable(ParentBean.class.getClassLoader());
            }

            public static final Creator<ParentBean> CREATOR = new Creator<ParentBean>() {
                @Override
                public ParentBean createFromParcel(Parcel source) {
                    return new ParentBean(source);
                }

                @Override
                public ParentBean[] newArray(int size) {
                    return new ParentBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeInt(this.pid);
            dest.writeString(this.extendsX);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeParcelable(this.parent, flags);
        }

        public TreeBean() {
        }

        protected TreeBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.pid = in.readInt();
            this.extendsX = in.readString();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.parent = in.readParcelable(ParentBean.class.getClassLoader());
        }

        public static final Creator<TreeBean> CREATOR = new Creator<TreeBean>() {
            @Override
            public TreeBean createFromParcel(Parcel source) {
                return new TreeBean(source);
            }

            @Override
            public TreeBean[] newArray(int size) {
                return new TreeBean[size];
            }
        };
    }

    public static class ItemsBean implements android.os.Parcelable {
        /**
         * id : 2508
         * name : 成都市
         * pid : 2507
         * extends :
         * created_at : 2017-06-02 08:44:10
         * updated_at : 2017-06-02 08:44:10
         */

        private int id;
        private String name;
        private int pid;
        @SerializedName("extends")
        private String extendsX;
        private String created_at;
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

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getExtendsX() {
            return extendsX;
        }

        public void setExtendsX(String extendsX) {
            this.extendsX = extendsX;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeInt(this.pid);
            dest.writeString(this.extendsX);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
        }

        public ItemsBean() {
        }

        protected ItemsBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.pid = in.readInt();
            this.extendsX = in.readString();
            this.created_at = in.readString();
            this.updated_at = in.readString();
        }

        public static final Creator<ItemsBean> CREATOR = new Creator<ItemsBean>() {
            @Override
            public ItemsBean createFromParcel(Parcel source) {
                return new ItemsBean(source);
            }

            @Override
            public ItemsBean[] newArray(int size) {
                return new ItemsBean[size];
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
        dest.writeParcelable(this.tree, flags);
        dest.writeTypedList(this.items);
    }

    public LocationBean() {
    }

    protected LocationBean(Parcel in) {
        super(in);
        this.tree = in.readParcelable(TreeBean.class.getClassLoader());
        this.items = in.createTypedArrayList(ItemsBean.CREATOR);
    }

    public static final Creator<LocationBean> CREATOR = new Creator<LocationBean>() {
        @Override
        public LocationBean createFromParcel(Parcel source) {
            return new LocationBean(source);
        }

        @Override
        public LocationBean[] newArray(int size) {
            return new LocationBean[size];
        }
    };
}
