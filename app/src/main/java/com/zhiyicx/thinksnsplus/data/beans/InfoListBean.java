package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListBean extends BaseListBean{

    private List<ListBean> list;
    private List<RecommendBean> recommend;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<RecommendBean> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<RecommendBean> recommend) {
        this.recommend = recommend;
    }

    public static class ListBean extends BaseListBean{
        /**
         * id : 1
         * title : 123123
         * updated_at : 2017-03-13 09:59:32
         * storage : {"id":1,"image_width":null,"image_height":null}
         */

        private int id;
        private String title;
        private String updated_at;
        private StorageBean storage;

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

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public StorageBean getStorage() {
            return storage;
        }

        public void setStorage(StorageBean storage) {
            this.storage = storage;
        }

        public static class StorageBean {
            /**
             * id : 1
             * image_width : null
             * image_height : null
             */

            private int id;
            private Object image_width;
            private Object image_height;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getImage_width() {
                return image_width;
            }

            public void setImage_width(Object image_width) {
                this.image_width = image_width;
            }

            public Object getImage_height() {
                return image_height;
            }

            public void setImage_height(Object image_height) {
                this.image_height = image_height;
            }
        }
    }

    public static class RecommendBean extends BaseListBean{
        /**
         * id : 1
         * created_at : 2017-03-16 11:31:52
         * updated_at : 2017-03-16 11:31:52
         * cate_id : 2
         * news_id : 1
         * cover : {"id":1,"image_width":null,"image_height":null}
         * sort : 0
         */

        private int id;
        private String created_at;
        private String updated_at;
        private int cate_id;
        private int news_id;
        private CoverBean cover;
        private int sort;

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

        public int getCate_id() {
            return cate_id;
        }

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public int getNews_id() {
            return news_id;
        }

        public void setNews_id(int news_id) {
            this.news_id = news_id;
        }

        public CoverBean getCover() {
            return cover;
        }

        public void setCover(CoverBean cover) {
            this.cover = cover;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public static class CoverBean {
            /**
             * id : 1
             * image_width : null
             * image_height : null
             */

            private int id;
            private int image_width;
            private int image_height;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getImage_width() {
                return image_width;
            }

            public void setImage_width(int image_width) {
                this.image_width = image_width;
            }

            public int getImage_height() {
                return image_height;
            }

            public void setImage_height(int image_height) {
                this.image_height = image_height;
            }
        }
    }
}
