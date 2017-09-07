package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment.BUNDLE_ANSWER;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsFragment.BUNDLE_SOURCE_ID;

/**
 * @Author Jliuer
 * @Date 2017/08/15/15:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QAListInfoAdapter extends CommonAdapter<QAListInfoBean> {

    private int mContentMaxShowNum;

    public QAListInfoAdapter(Context context, int layoutId, List<QAListInfoBean> datas) {
        super(context, layoutId, datas);
        mContentMaxShowNum = mContext.getResources().getInteger(R.integer
                .dynamic_list_content_max_show_size);
    }

    @Override
    protected void convert(ViewHolder holder, QAListInfoBean infoBean, int position) {
        ImageView imageView = holder.getImageViwe(R.id.item_info_imag);
        TextView titleView = holder.getTextView(R.id.item_info_title);
        titleView.setText(infoBean.getSubject());
        holder.setText(R.id.item_info_time, TimeUtils.getTimeFriendlyForDetail(infoBean.getCreated_at()));
        holder.setText(R.id.item_info_count, String.format(Locale.getDefault(), mContext.getString(R.string.qa_show_topic_followed_content)
                , infoBean.getWatchers_count(), infoBean.getAnswers_count()));
        holder.setText(R.id.item_info_reward, String.format(Locale.getDefault(), mContext.getString(R.string.qa_show_topic_followed_reward)
                , PayConfig.realCurrencyFen2Yuan(infoBean.getAmount())));
        holder.setVisible(R.id.item_info_reward, infoBean.getAmount() > 0 ? View.VISIBLE : View.GONE);
        ConvertUtils.stringLinkConvert(holder.getTextView(R.id.item_info_count), setLinks(infoBean), false);
        ConvertUtils.stringLinkConvert(holder.getTextView(R.id.item_info_reward), setLinks());
        TextView contentTextView = holder.getView(R.id.item_info_hotcomment);
        String content = infoBean.getBody();

        titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, getExcellentTag(), 0);

        int id = 0;
        try {
            id = RegexUtils.getImageIdFromMarkDown(MarkdownConfig.IMAGE_FORMAT, infoBean.getAnswer().getBody());
        } catch (Exception e) {

        }

        if (id > 0) {
            imageView.setVisibility(View.VISIBLE);
            contentTextView.setVisibility(View.VISIBLE);
            ImageUtils.loadQAUserHead(infoBean.getAnswer().getUser(), contentTextView, infoBean.getAnswer().getBody(),
                    infoBean.getAnswer().getAnonymity() == 1
                            && infoBean.getAnswer().getUser_id() != AppApplication.getmCurrentLoginAuth().getUser_id(), false);
            RxView.clicks(contentTextView)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> {
                        Intent intent = new Intent(getContext(), AnswerDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(BUNDLE_ANSWER, infoBean.getAnswer());
                        bundle.putLong(BUNDLE_SOURCE_ID, infoBean.getAnswer().getId());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    });

            RxView.clicks(imageView)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> contentTextView.performClick());

            int w = DeviceUtils.getScreenWidth(mContext);
            int h = mContext.getResources().getDimensionPixelOffset(R.dimen.qa_info_iamge_height);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(w, h));
            String url = ImageUtils.imagePathConvertV2(id, w, h, ImageZipConfig.IMAGE_80_ZIP);
            Glide.with(mContext).load(url)
                    .override(w, h)
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .into(imageView);


        } else {
            imageView.setVisibility(View.GONE);
            contentTextView.setVisibility(View.GONE);
        }

    }

    private List<Link> setLinks(QAListInfoBean infoBean) {
        List<Link> links = new ArrayList<>();
        Link followCountLink = new Link(Pattern.compile("\\b\\d+\\b")).setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(followCountLink);
        return links;
    }

    private List<Link> setLinks() {
        List<Link> links = new ArrayList<>();
        Link rewardMoneyLink = new Link(Pattern.compile("¥\\d+\\.\\d+")).setTextColor(ContextCompat.getColor(getContext(), R.color
                .withdrawals_item_enable))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(rewardMoneyLink);
        return links;
    }

    protected int getExcellentTag() {
        return 0;
    }
}
