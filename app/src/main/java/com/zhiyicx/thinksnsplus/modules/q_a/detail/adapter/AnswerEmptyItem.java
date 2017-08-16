package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.widget.EmptyItem;

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
}
