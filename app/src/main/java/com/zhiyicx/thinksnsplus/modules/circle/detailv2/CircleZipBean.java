package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDetail;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;

import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/11/30/11:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleZipBean {

    public CircleZipBean(CircleInfoDetail circleInfoDetail, List<CirclePostListBean> circlePostListBeanList) {
        mCircleInfoDetail = circleInfoDetail;
        mCirclePostListBeanList = circlePostListBeanList;
    }

    private CircleInfoDetail mCircleInfoDetail;
    private List<CirclePostListBean> mCirclePostListBeanList;

    public CircleInfoDetail getCircleInfoDetail() {
        return mCircleInfoDetail;
    }

    public void setCircleInfoDetail(CircleInfoDetail circleInfoDetail) {
        mCircleInfoDetail = circleInfoDetail;
    }

    public List<CirclePostListBean> getCirclePostListBeanList() {
        return mCirclePostListBeanList;
    }

    public void setCirclePostListBeanList(List<CirclePostListBean> circlePostListBeanList) {
        mCirclePostListBeanList = circlePostListBeanList;
    }
}
