package com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.follow.MyFollowContract;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist.QATopicListConstact;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class QuestionTopicAdapter extends CommonAdapter<BaseListBean> {

    private ITSListPresenter mPresenter;

    public QuestionTopicAdapter(Context context, List<BaseListBean> datas, ITSListPresenter presenter) {
        super(context, R.layout.item_qatopic_list, datas);
        this.mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, BaseListBean baseListBean, int position) {
        QATopicBean topicBean = (QATopicBean) baseListBean;
        ImageView headImage = holder.getImageViwe(R.id.iv_topic_cover);
        CheckBox subscrib = holder.getView(R.id.tv_topic_subscrib);
        holder.setText(R.id.tv_topic_feed_count,
                String.format(Locale.getDefault(), mContext.getString(R.string.qa_show_topic_followed),
                        topicBean.getFollows_count(), topicBean.getQuestions_count()));
        ConvertUtils.stringLinkConvert(holder.getTextView(R.id.tv_topic_feed_count), setLinks(topicBean), false);
        holder.setText(R.id.tv_topic_name, topicBean.getName());
        holder.setText(R.id.tv_topic_subscrib, mContext.getString(R.string.qa_topic_follow));
        Glide.with(getContext())
                .load(topicBean.getAvatar())
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
                .into(headImage);

        // 设置订阅状态
        boolean isJoined = topicBean.getHas_follow();
        subscrib.setChecked(isJoined);
        subscrib.setText(isJoined ? getContext().getString(R.string.qa_topic_followed) : getContext().getString(R.string.qa_topic_follow));
        subscrib.setPadding(isJoined ? getContext().getResources().getDimensionPixelSize(R.dimen.spacing_small) : getContext().getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, 0, 0);
        RxView.clicks(subscrib)
                .filter(aVoid -> mPresenter != null)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (!mPresenter.handleTouristControl()) {
                        if (mPresenter instanceof QATopicListConstact.Presenter) {
                            ((QATopicListConstact.Presenter) mPresenter).handleTopicFollowState(position, topicBean.getId() + "", isJoined);
                        } else if (mPresenter instanceof MyFollowContract.Presenter) {
                            ((MyFollowContract.Presenter) mPresenter).handleTopicFollowState(position, topicBean);
                        }
                    } else {
                        subscrib.setChecked(false);
                    }
                });
    }

    private List<Link> setLinks(QATopicBean topicBean) {
        List<Link> links = new ArrayList<>();
        Link followCountLink = new Link(topicBean.getFollows_count() + "").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(followCountLink);

        Link answerCountLink = new Link(topicBean.getQuestions_count() + "").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(answerCountLink);
        return links;
    }

    public void setPresenter(ITSListPresenter presenter) {
        mPresenter = presenter;
    }
}
