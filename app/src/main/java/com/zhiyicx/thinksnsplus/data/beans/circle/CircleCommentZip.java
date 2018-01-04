package com.zhiyicx.thinksnsplus.data.beans.circle;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;

import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/12/05/10:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleCommentZip {
    private List<CirclePostCommentBean> pinneds;
    private List<CirclePostCommentBean> comments;

    public CircleCommentZip(List<CirclePostCommentBean> pinneds, List<CirclePostCommentBean> comments) {
        this.pinneds = pinneds;
        this.comments = comments;
    }

    public List<CirclePostCommentBean> getPinneds() {
        return pinneds;
    }

    public void setPinneds(List<CirclePostCommentBean> pinneds) {
        this.pinneds = pinneds;
    }

    public List<CirclePostCommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CirclePostCommentBean> comments) {
        this.comments = comments;
    }
}
