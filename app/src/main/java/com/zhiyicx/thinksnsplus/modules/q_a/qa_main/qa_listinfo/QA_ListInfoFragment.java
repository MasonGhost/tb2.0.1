package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.widget.textview.CenterImageSpan;
import com.zhiyicx.baseproject.widget.textview.CircleImageDrawable;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment.ITEM_SPACING;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_ListInfoFragment extends TSListFragment<QA_ListInfoConstact.Presenter, QAListInfoBean>
        implements QA_ListInfoConstact.View {

    public static final String BUNDLE_QA_TYPE = "qa_type";

    private String mQAInfoType;

    @Inject
    QA_ListInfoFragmentPresenter mQA_listInfoFragmentPresenter;

    public static QA_ListInfoFragment newInstance(String params) {
        QA_ListInfoFragment fragment = new QA_ListInfoFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_QA_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getQAInfoType() {
        return mQAInfoType;
    }

    @Override
    protected void onEmptyViewClick() {
        mRefreshlayout.setRefreshing(true);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initData() {
        DaggerQA_ListInfoComponent
                .builder().appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .qA_listInfoFragmentPresenterModule(new QA_listInfoFragmentPresenterModule(this))
                .build().inject(this);

        mQAInfoType = getArguments().getString(BUNDLE_QA_TYPE);

        super.initData();

    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        requestNetData(null, maxId, mQAInfoType, isLoadMore);
    }

    private void requestNetData(String subject, Long maxId, String type, boolean isLoadMore) {
        mPresenter.requestNetData(subject, maxId, type, isLoadMore);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CommonAdapter<QAListInfoBean>(getActivity(), R.layout.item_qa_content, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, QAListInfoBean infoBean, int position) {
                ImageView imageView = holder.getImageViwe(R.id.item_info_imag);
                holder.setText(R.id.item_info_title, infoBean.getSubject());
                holder.setText(R.id.item_info_time, TimeUtils.getTimeFriendlyForDetail(infoBean.getCreated_at()));
                holder.setText(R.id.item_info_count, String.format(Locale.getDefault(), getString(R.string.qa_show_topic_followed_reward)
                        , infoBean.getWatchers_count(), infoBean.getAnswers_count(), infoBean.getAmount()));
                ConvertUtils.stringLinkConvert(holder.getTextView(R.id.item_info_count), setLinks(infoBean));
                TextView contentTextView = holder.getView(R.id.item_info_hotcomment);
                String content = infoBean.getBody();
                int id = RegexUtils.getImageIdFromMarkDown(MarkdownConfig.IMAGE_FORMAT, content);

                if (id > 0) {
                    int w = DeviceUtils.getScreenWidth(mContext);
                    int h = getResources().getDimensionPixelOffset(R.dimen.qa_info_iamge_height);
                    String url = ImageUtils.imagePathConvertV2(id, w, h, ImageZipConfig.IMAGE_80_ZIP);
                    Glide.with(mContext).load(url)
                            .asBitmap()
                            .override(w, h)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    imageView.setImageBitmap(resource);
                                    Bitmap newBmp = Bitmap.createScaledBitmap(resource,
                                            contentTextView.getLineHeight(), contentTextView.getLineHeight(), true);
                                    CircleImageDrawable headImage = new CircleImageDrawable(newBmp);
                                    headImage.setBounds(8, 0, 8 + contentTextView.getLineHeight(), contentTextView.getLineHeight());
                                    ImageSpan imgSpan = new CenterImageSpan(headImage, infoBean.getAnonymity() == 1);
                                    SpannableString spannableString = SpannableString.valueOf("T" + RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, content));
                                    spannableString.setSpan(imgSpan, 0, 1, Spannable
                                            .SPAN_EXCLUSIVE_EXCLUSIVE);
                                    contentTextView.setText(spannableString);
                                }
                            });
                }else{
                    Bitmap newBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), com.zhiyicx.baseproject.R.mipmap.ico_potoablum),
                            contentTextView.getLineHeight(), contentTextView.getLineHeight(), true);
                    CircleImageDrawable headImage = new CircleImageDrawable(newBmp);
                    headImage.setBounds(8, 0, 8 + contentTextView.getLineHeight(), contentTextView.getLineHeight());
                    ImageSpan imgSpan = new CenterImageSpan(headImage, infoBean.getAnonymity() == 1);
                    SpannableString spannableString = SpannableString.valueOf("T" + RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, content));
                    spannableString.setSpan(imgSpan, 0, 1, Spannable
                            .SPAN_EXCLUSIVE_EXCLUSIVE);
                    contentTextView.setText(spannableString);
                }
            }
        };
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
