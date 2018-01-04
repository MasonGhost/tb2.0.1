package com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;

/**
 * @author Jliuer
 * @Date 2017/11/30/14:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostListItemForZeroImage extends CirclePostListBaseItem {

    public CirclePostListItemForZeroImage(Context context) {
        super(context);
    }

    @Override
    public boolean isForViewType(CirclePostListBean item, int position) {
        return item.getId() != null && (item.getImages() == null || item.getImages().isEmpty());
    }
}
