package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/19
 * @contact email:648129313@qq.com
 */

public class GroupDynamicCommentEmptyItem extends EmptyItem<GroupDynamicCommentListBean> {
    @Override
    public boolean isForViewType(GroupDynamicCommentListBean item, int position) {
        return item.getContent() == null;
    }
}
