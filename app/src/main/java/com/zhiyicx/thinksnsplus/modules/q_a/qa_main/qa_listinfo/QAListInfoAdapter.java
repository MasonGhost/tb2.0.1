package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @Author Jliuer
 * @Date 2017/08/15/15:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QAListInfoAdapter extends CommonAdapter<QAListInfoBean> {

    public QAListInfoAdapter(Context context, int layoutId, List<QAListInfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QAListInfoBean infoBean, int position) {
        ImageView imageView = holder.getImageViwe(R.id.item_info_imag);
        holder.setText(R.id.item_info_title, infoBean.getSubject());
        holder.setText(R.id.item_info_time, TimeUtils.getTimeFriendlyForDetail(infoBean.getCreated_at()));
        holder.setText(R.id.item_info_count, String.format(Locale.getDefault(), mContext.getString(R.string.qa_show_topic_followed_reward)
                , infoBean.getWatchers_count(), infoBean.getAnswers_count(), infoBean.getAmount()));
        ConvertUtils.stringLinkConvert(holder.getTextView(R.id.item_info_count), setLinks(infoBean));
        TextView contentTextView = holder.getView(R.id.item_info_hotcomment);
        String content = infoBean.getBody();
        imageView.getLayoutParams();
        int id = RegexUtils.getImageIdFromMarkDown(MarkdownConfig.IMAGE_FORMAT, content);

        if (id > 0) {
            imageView.setVisibility(View.VISIBLE);
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
        }

        ImageUtils.loadQAUserHead(infoBean.getUser(), contentTextView, content, infoBean.getAnonymity() == 1, false);
    }

    private List<Link> setLinks(QAListInfoBean infoBean) {
        List<Link> links = new ArrayList<>();
        Link followCountLink = new Link(infoBean.getWatchers_count() + "").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(followCountLink);

        Link answerCountLink = new Link(infoBean.getAnswers_count() + "").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(answerCountLink);

        Link rewardMoneyLink = new Link("￥" + infoBean.getAmount()).setTextColor(ContextCompat.getColor(getContext(), R.color
                .withdrawals_item_enable))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(rewardMoneyLink);
        return links;
    }
}