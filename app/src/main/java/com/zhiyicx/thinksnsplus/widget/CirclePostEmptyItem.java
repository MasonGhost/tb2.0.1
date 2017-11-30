package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;

/**
 * @author Jliuer
 * @Date 2017/11/30/15:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostEmptyItem extends EmptyItem<CirclePostListBean> {

    public CirclePostEmptyItem() {
    }

    @Override
    public boolean isForViewType(CirclePostListBean item, int position) {
        return item != null && item.getId() == null;
    }
}
