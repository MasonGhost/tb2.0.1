package com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic;

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

public class TopicsAdapter extends TagAdapter<QATopicBean> {
    private final LayoutInflater mInflater;

    public TopicsAdapter(List<QATopicBean> datas, Context context) {
        super(datas);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(FlowLayout parent, int position, QATopicBean qaTopicBean) {
        TextView tv = (TextView) mInflater.inflate(R.layout.item_publish_question_topics,
                parent, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tv.setCompoundDrawablesRelative(null, null, UIUtils.getCompoundDrawables(parent.getContext(), R.mipmap.topic_icon_delete), null);
        } else {
            tv.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(parent.getContext(), R.mipmap.topic_icon_delete), null);
        }
        tv.setText(qaTopicBean.getName());
        return tv;
    }
}
