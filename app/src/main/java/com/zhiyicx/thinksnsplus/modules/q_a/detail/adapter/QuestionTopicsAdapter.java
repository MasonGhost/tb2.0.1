package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;


/**
 * @Describe list adapter for recommenc question topic
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

public class QuestionTopicsAdapter extends TagAdapter<QATopicBean> {
    private final LayoutInflater mInflater;

    public QuestionTopicsAdapter(List<QATopicBean> datas, Context context) {
        super(datas);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(FlowLayout parent, int position, QATopicBean qaTopicBean) {
        TextView tv = (TextView) mInflater.inflate(R.layout.item_publish_question_topics,
                parent, false);
        tv.setText(qaTopicBean.getName());
        return tv;
    }
}
