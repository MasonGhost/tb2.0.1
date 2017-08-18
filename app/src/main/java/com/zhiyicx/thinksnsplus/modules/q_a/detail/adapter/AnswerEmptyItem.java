package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.text.TextUtils;

import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.widget.EmptyItem;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/16
 * @contact email:648129313@qq.com
 */

public class AnswerEmptyItem extends EmptyItem<AnswerInfoBean> {
    @Override
    public boolean isForViewType(AnswerInfoBean item, int position) {
        return TextUtils.isEmpty(item.getBody());
    }

    @Override
    public void convert(ViewHolder holder, AnswerInfoBean baseListBean, AnswerInfoBean lastT, int position, int itemCounts) {
        super.convert(holder, baseListBean, lastT, position, itemCounts);
        EmptyView emptyView = holder.getView(R.id.comment_emptyview);
        emptyView.setErrorMessage(emptyView.getContext().getString(R.string.qa_question_no_answer));
        emptyView.setErrorImag(0);
    }
}
