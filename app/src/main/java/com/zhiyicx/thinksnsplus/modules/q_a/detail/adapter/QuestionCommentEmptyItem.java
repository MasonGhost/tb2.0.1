package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.widget.EmptyItem;

/**
 * @Author Jliuer
 * @Date 2017/03/30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QuestionCommentEmptyItem extends EmptyItem<QuestionCommentBean> {
    @Override
    public boolean isForViewType(QuestionCommentBean item, int position) {
        return TextUtils.isEmpty(item.getBody());
    }
}
