package com.zhiyicx.thinksnsplus.modules.circle.detail.adapter;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class GroupDynamicListItemForZeroImage extends GroupDynamicListBaseItem {
    public GroupDynamicListItemForZeroImage(Context context) {
        super(context);
    }


    @Override
    public boolean isForViewType(GroupDynamicListBean item, int position) {
        return item.getId() != null && (item.getImages() == null || item.getImages().isEmpty());
    }

}
