package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/06/29/14:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicCommentBeanV2 {

    private List<DynamicCommentBean> comments;
    private List<DynamicCommentBean> pinneds;

    public List<DynamicCommentBean> getComments() {
        return comments;
    }

    public void setComments(List<DynamicCommentBean> comments) {
        this.comments = comments;
    }

    public List<DynamicCommentBean> getPinneds() {
        return pinneds;
    }

    public void setPinneds(List<DynamicCommentBean> pinneds) {
        this.pinneds = pinneds;
    }
}
