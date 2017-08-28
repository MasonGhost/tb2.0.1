package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.widget.EmptyItem;

/**
 * @Author Jliuer
 * @Date 2017/03/30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AnswerDetailCommentEmptyItem extends EmptyItem<AnswerCommentListBean> {
    @Override
    public boolean isForViewType(AnswerCommentListBean item, int position) {
        return TextUtils.isEmpty(item.getBody());
    }
}
