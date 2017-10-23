package com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
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

public class MyFollowQuestionAdapter extends CommonAdapter<BaseListBean>{

    public MyFollowQuestionAdapter(Context context, List<BaseListBean> datas) {
        super(context, R.layout.item_my_follow_question, datas);
    }

    @Override
    protected void convert(ViewHolder holder, BaseListBean baseListBean, int position) {
        QAListInfoBean qaListInfoBean = (QAListInfoBean) baseListBean;
        holder.setText(R.id.tv_title, qaListInfoBean.getSubject());
        holder.setText(R.id.tv_count, String.format(mContext.getString(R.string.qa_show_topic_followed),
                qaListInfoBean.getWatchers_count(), qaListInfoBean.getAnswers_count()));
        ConvertUtils.stringLinkConvert(holder.getTextView(R.id.tv_count), setLinks(qaListInfoBean), false);
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(qaListInfoBean.getCreated_at()));
    }

    private List<Link> setLinks(QAListInfoBean qaListInfoBean) {
        List<Link> links = new ArrayList<>();
        Link numberCountLink = new Link(Pattern.compile("[0-9]+")).setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(numberCountLink);
        return links;
    }
}
