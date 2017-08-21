package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.SubscriptSpan;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.flowtag.FlowTagLayout;
import com.zhiyicx.thinksnsplus.widget.flowtag.OnTagSelectListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/26
 * @contact email:648129313@qq.com
 */

public class SearchExpertAdapter extends CommonAdapter<ExpertBean>{

    ExpertSearchContract.Presenter mPresenter;

    public SearchExpertAdapter(Context context, List<ExpertBean> datas,ExpertSearchContract.Presenter presenter) {
        super(context, R.layout.item_search_expert, datas);
        mPresenter=presenter;
    }

    @Override
    protected void convert(ViewHolder holder, ExpertBean expertBean, int position) {
        UserAvatarView ivHeadpic = holder.getView(R.id.iv_headpic);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvDigCount = holder.getView(R.id.tv_dig_count);
        CheckBox subscrib = holder.getView(R.id.tv_expert_subscrib);
        FlowTagLayout ftlTags = holder.getView(R.id.ftl_tags);
        tvName.setText(expertBean.getName());
        tvDigCount.setText(String.format(Locale.getDefault(), mContext.getString(R.string.qa_publish_show_expert),
                expertBean.getExtra().getAnswers_count(), expertBean.getExtra().getLikes_count()));
        ConvertUtils.stringLinkConvert(tvDigCount, setLinks(expertBean));
        ftlTags.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE);
        List<UserTagBean> tagBeenList = expertBean.getTags();


        UserInfoBean userInfoBean=new UserInfoBean();
        userInfoBean.setUser_id((long)expertBean.getExtra().getUser_id());
        userInfoBean.setFollower(expertBean.isFollower());
        userInfoBean.setFollowing(expertBean.isFollowing());
        userInfoBean.setVerified(expertBean.getVerified());
        boolean isJoined = expertBean.isFollowing();
        subscrib.setChecked(isJoined);
        subscrib.setText(isJoined ? getContext().getString(R.string.qa_topic_followed) : getContext().getString(R.string.qa_topic_follow));
        subscrib.setPadding(isJoined ? getContext().getResources().getDimensionPixelSize(R.dimen.spacing_small) : getContext().getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, 0, 0);
        RxView.clicks(subscrib)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (TouristConfig.CHEENAL_CAN_SUBSCRIB || !mPresenter.handleTouristControl()) {
                        mPresenter.handleFollowUser(userInfoBean);
                        subscrib.setText(!isJoined ? getContext().getString(R.string.qa_topic_followed) : getContext().getString(R.string.qa_topic_follow));
                    } else {
                        subscrib.setChecked(false);
                    }
                });

        UserTagAdapter adapter = new UserTagAdapter(mContext);
        ftlTags.setAdapter(adapter);
        adapter.clearAndAddAll(tagBeenList);
        ftlTags.setOnTagSelectListener((parent, selectedList) -> {

        });
        ImageUtils.loadCircleUserHeadPic(userInfoBean, ivHeadpic);
    }

    private List<Link> setLinks(ExpertBean expertBean) {
        List<Link> links = new ArrayList<>();
        Link numberCountLink = new Link(Pattern.compile("[0-9]+")).setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .normal_for_assist_text))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(numberCountLink);
        Link digCountLink = new Link(String.valueOf(expertBean.getExtra().getLikes_count())).setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .normal_for_assist_text))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(digCountLink);
        return links;
    }

}
