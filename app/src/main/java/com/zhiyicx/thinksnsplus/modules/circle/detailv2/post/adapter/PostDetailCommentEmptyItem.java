package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.adapter;

import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.widget.EmptyItem;

/**
 * @Author Jliuer
 * @Date 2017/03/30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PostDetailCommentEmptyItem extends EmptyItem<CirclePostCommentBean> {

    @Override
    public boolean isForViewType(CirclePostCommentBean item, int position) {
        return TextUtils.isEmpty(item.getContent());
    }
}
