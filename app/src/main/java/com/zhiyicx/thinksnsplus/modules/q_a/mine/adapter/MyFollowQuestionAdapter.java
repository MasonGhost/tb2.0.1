package com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo.QAListBaseInfoAdapter;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
