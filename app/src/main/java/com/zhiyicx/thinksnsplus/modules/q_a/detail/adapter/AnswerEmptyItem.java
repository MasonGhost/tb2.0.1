package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.text.TextUtils;
import android.widget.TextView;

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
    public int getItemViewLayoutId() {
        return R.layout.view_empty_question_detail;
    }

    @Override
    public boolean isForViewType(AnswerInfoBean item, int position) {
        return TextUtils.isEmpty(item.getBody()) && item.getInvited() != 1;
    }

    @Override
    public void convert(ViewHolder holder, AnswerInfoBean baseListBean, AnswerInfoBean lastT, int position, int itemCounts) {
        TextView emptyView = holder.getView(R.id.tv_notice);
    }
}
