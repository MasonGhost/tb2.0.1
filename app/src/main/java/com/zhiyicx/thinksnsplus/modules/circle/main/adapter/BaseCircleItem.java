package com.zhiyicx.thinksnsplus.modules.circle.main.adapter;

import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;

/**
 * @Author Jliuer
 * @Date 2017/11/21/14:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class BaseCircleItem implements ItemViewDelegate<CircleInfo> {

    public static final long MYJOINEDCIRCLE = -1L;
    public static final long RECOMMENDCIRCLE = -2L;

    public BaseCircleItem() {
    }

    public BaseCircleItem(CircleItemItemEvent circleItemItemEvent) {
        mCircleItemItemEvent = circleItemItemEvent;
    }

    protected CircleItemItemEvent mCircleItemItemEvent;

    public interface CircleItemItemEvent {
        void toAllJoinedCircle(CircleInfo circleInfo);

        void toCircleDetail(CircleInfo circleInfo);

        void changeRecommend();
    }

    public CircleItemItemEvent getCircleItemItemEvent() {
        return mCircleItemItemEvent;
    }
}
