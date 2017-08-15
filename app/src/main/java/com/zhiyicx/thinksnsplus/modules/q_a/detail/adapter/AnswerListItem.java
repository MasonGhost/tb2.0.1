package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class AnswerListItem implements ItemViewDelegate<AnswerInfoBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_question_answer;
    }

    @Override
    public boolean isForViewType(AnswerInfoBean item, int position) {
        return !TextUtils.isEmpty(item.getBody());
    }

    @Override
    public void convert(ViewHolder holder, AnswerInfoBean answerInfoBean, AnswerInfoBean lastT, int position, int itemCounts) {

    }
}
