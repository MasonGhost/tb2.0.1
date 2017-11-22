package com.zhiyicx.zhibolibrary.model.entity;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class SearchJson {
    public String code;
    public String message;
    public AllSearch data;

    public class AllSearch {
        public SearchResult[] user_list;
        public SearchResult[] stream_list;
        public SearchResult[] video_list;
    }

    @Override
    public String toString() {
        return "SearchJson{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
