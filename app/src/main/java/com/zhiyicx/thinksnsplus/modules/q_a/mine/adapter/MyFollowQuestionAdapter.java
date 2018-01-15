package com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter;

import android.content.Context;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo.QAListBaseInfoAdapter;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyFollowQuestionAdapter extends QAListBaseInfoAdapter<BaseListBean> {

    public MyFollowQuestionAdapter(Context context, List<BaseListBean> datas) {
        super(context, R.layout.item_qa_content, datas);
    }

    @Override
    protected boolean isNeedShowAnswer() {
        return false;
    }
}
