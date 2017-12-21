package com.zhiyicx.thinksnsplus.data.beans.circle;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;

import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/12/01/11:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostBean {

    private List<CirclePostListBean> pinneds;
    private List<CirclePostListBean> posts;

    public List<CirclePostListBean> getPinneds() {
        return pinneds;
    }

    public void setPinneds(List<CirclePostListBean> pinneds) {
        this.pinneds = pinneds;
    }

    public List<CirclePostListBean> getPosts() {
        return posts;
    }

    public void setPosts(List<CirclePostListBean> posts) {
        this.posts = posts;
    }
}
