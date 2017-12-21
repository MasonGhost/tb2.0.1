package com.zhiyicx.thinksnsplus.data.beans.circle;

import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;

import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/11/30/11:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleZipBean {

    public CircleZipBean(CircleInfo CircleInfo, List<CirclePostListBean> circlePostListBeanList) {
        mCircleInfo = CircleInfo;
        mCirclePostListBeanList = circlePostListBeanList;
    }

    private CircleInfo mCircleInfo;
    private List<CirclePostListBean> mCirclePostListBeanList;

    public CircleInfo getCircleInfo() {
        return mCircleInfo;
    }

    public void setCircleInfo(CircleInfo CircleInfo) {
        mCircleInfo = CircleInfo;
    }

    public List<CirclePostListBean> getCirclePostListBeanList() {
        return mCirclePostListBeanList;
    }

    public void setCirclePostListBeanList(List<CirclePostListBean> circlePostListBeanList) {
        mCirclePostListBeanList = circlePostListBeanList;
    }
}
