package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class DynamicCommentEmptyItem extends EmptyItem<DynamicCommentBean> {

    public DynamicCommentEmptyItem(int emptyView){
        super.setEmptView(emptyView);
    }

    @Override
    public boolean isForViewType(DynamicCommentBean item, int position) {
        return item.getComment_content() == null;
    }
}
