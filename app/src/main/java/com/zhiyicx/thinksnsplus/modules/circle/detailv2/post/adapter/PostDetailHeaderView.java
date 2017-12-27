package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseWebLoad;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.DigListActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.adapter.BaseDigItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.information.dig.InfoDigListActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.utils.MarkDownRule;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;
import com.zhiyicx.thinksnsplus.widget.ReWardView;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

import static android.view.View.GONE;
import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.MarkdownConfig.IMAGE_FORMAT;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/9
 * @contact email:648129313@qq.com
 */

public class PostDetailHeaderView extends BaseWebLoad {
    private MarkdownView mContent;
    private MarkdownView mContentSubject;
    private TextView mTitle;
    private TextView mChannel;
    private TextView mFrom;
    private DynamicHorizontalStackIconView mDigListView;
    private ReWardView mReWardView;
    private FrameLayout mCommentHintView;
    private TextView mCommentCountView;
    private FrameLayout mInfoRelateList;
    private TagFlowLayout mFtlRelate;
    private RecyclerView mRvRelateInfo;
    private View mInfoDetailHeader;
    private Context mContext;
    private int screenWidth;
    private int picWidth;
    private Bitmap sharBitmap;
    private List<ImageBean> mImgList;
    private ImageView mIvDetail;
    private boolean isReviewIng;

    private DynamicDetailAdvertHeader mDynamicDetailAdvertHeader;
    private ArrayList<AnimationRectBean> animationRectBeanArrayList;
    private View mRewardView;

    public View getInfoDetailHeader() {
        return mInfoDetailHeader;
    }

    public PostDetailHeaderView(Context context, List<RealAdvertListBean> adverts) {
        this.mContext = context;
        mImgList = new ArrayList<>();
        animationRectBeanArrayList = new ArrayList<>();
        mInfoDetailHeader = LayoutInflater.from(context).inflate(R.layout
                .item_post_comment_head, null);
        mInfoDetailHeader.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mTitle = (TextView) mInfoDetailHeader.findViewById(R.id.tv_info_title);
        mChannel = (TextView) mInfoDetailHeader.findViewById(R.id.tv_from_channel);
        mFrom = (TextView) mInfoDetailHeader.findViewById(R.id.item_info_timeform);
        mContent = (MarkdownView) mInfoDetailHeader.findViewById(R.id.info_detail_content);
        mContentSubject = (MarkdownView) mInfoDetailHeader.findViewById(R.id.info_content_subject);
        mDigListView = (DynamicHorizontalStackIconView) mInfoDetailHeader.findViewById(R.id.detail_dig_view);
        mReWardView = (ReWardView) mInfoDetailHeader.findViewById(R.id.v_reward);
        mCommentHintView = (FrameLayout) mInfoDetailHeader.findViewById(R.id.info_detail_comment);
        mCommentCountView = (TextView) mInfoDetailHeader.findViewById(R.id.tv_comment_count);
        mInfoRelateList = (FrameLayout) mInfoDetailHeader.findViewById(R.id.info_relate_list);
        mFtlRelate = (TagFlowLayout) mInfoDetailHeader.findViewById(R.id.fl_tags);
        mRvRelateInfo = (RecyclerView) mInfoDetailHeader.findViewById(R.id.rv_relate_info);
        mIvDetail = (ImageView) mInfoDetailHeader.findViewById(R.id.iv_detail);
        mRewardView = mInfoDetailHeader.findViewById(R.id.v_reward);
        initAdvert(context, adverts);
    }

    public void setDetail(CirclePostListBean circlePostDetailBean) {
        if (circlePostDetailBean != null) {
            mTitle.setText(circlePostDetailBean.getTitle());
            mChannel.setText("来自");
            String from = circlePostDetailBean.getUser().getName();
            if (!TextUtils.isEmpty(from)) {
                mFrom.setText(from);
            }
            mContentSubject.setVisibility(GONE);

            // 资讯contente
            if (!TextUtils.isEmpty(circlePostDetailBean.getBody())) {
                InternalStyleSheet css = new Github();
                css.addRule("body", "line-height: 1.6", "padding: 0px");
                css.addRule(".container", "padding-right:0", ";padding-left:0", "text-align:justify");
                mContent.addStyleSheet(MarkDownRule.generateStandardQuoteStyle());
                mContent.loadMarkdown(dealPic(circlePostDetailBean.getBody()));
                mContent.setWebChromeClient(mWebChromeClient);

                mContent.setOnElementListener(new MarkdownView.OnElementListener() {
                    @Override
                    public void onButtonTap(String s) {

                    }

                    @Override
                    public void onCodeTap(String s, String s1) {

                    }

                    @Override
                    public void onHeadingTap(int i, String s) {

                    }

                    @Override
                    public void onImageTap(String s, int width, int height) {
                        //  杨大佬说先暂时不做，如果后期要做的话 要做画廊 多张
                        LogUtils.d("Cathy", "onImageTap // " + s);
                        int position = 0;
                        for (int i = 0; i < mImgList.size(); i++) {
                            if (mImgList.get(i).getImgUrl().equals(s)) {
                                position = i;
                            }
                        }
                        GalleryActivity.startToGallery(mContext, position, mImgList, animationRectBeanArrayList);
                    }

                    @Override
                    public void onLinkTap(String s, String s1) {
                        CustomWEBActivity.startToWEBActivity(mContext, s1, s);
                    }

                    @Override
                    public void onKeystrokeTap(String s) {

                    }

                    @Override
                    public void onMarkTap(String s) {

                    }
                });
            }
            // 评论信息
            updateCommentView(circlePostDetailBean);
        }
    }

    private void initAdvert(Context context, List<RealAdvertListBean> adverts) {
        mDynamicDetailAdvertHeader = new DynamicDetailAdvertHeader(context, mInfoDetailHeader
                .findViewById(R.id.ll_advert));
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || adverts == null || adverts != null && adverts.isEmpty()) {
            mDynamicDetailAdvertHeader.hideAdvert();
            return;
        }
        mDynamicDetailAdvertHeader.setTitle("广告");
        mDynamicDetailAdvertHeader.setAdverts(adverts);
        mDynamicDetailAdvertHeader.setOnItemClickListener((v, position1, url) ->
                toAdvert(adverts.get(position1).getAdvertFormat().getImage().getLink(), adverts.get(position1).getTitle())
        );
    }

    private void toAdvert(String link, String title) {
        CustomWEBActivity.startToWEBActivity(mContext, link, title);
    }

    private String dealPic(String markDownContent) {
        // 替换图片id 为地址
        Pattern pattern = Pattern.compile(IMAGE_FORMAT);
        Matcher matcher = pattern.matcher(markDownContent);
        while (matcher.find()) {
            String imageMarkDown = matcher.group(0);
            String id = matcher.group(1);

            String imgPath = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/" + id + "?q=80";
            String iamgeTag = imageMarkDown.replaceAll("\\d+", imgPath).replace("@", "");
            markDownContent = markDownContent.replace(imageMarkDown, iamgeTag);
            dealImageList(imgPath, id);
        }
        return markDownContent;
    }

    private void dealImageList(String imgPath, String id) {
        for (ImageBean item : mImgList) {
            if (item.getImgUrl().equals(imgPath)) {
                return;
            }
        }
        ImageBean imageBean = new ImageBean();
        imageBean.setImgUrl(imgPath);// 本地地址，也许有
        Toll toll = new Toll(); // 收费信息
        toll.setPaid(true);// 是否已經付費
        toll.setToll_money(0);// 付费金额
        toll.setToll_type_string("");// 付费类型
        toll.setPaid_node(0);// 付费节点
        imageBean.setToll(toll);
        imageBean.setStorage_id(Integer.parseInt(id));// 图片附件id
        mImgList.add(imageBean);
        try {
            AnimationRectBean rect = AnimationRectBean.buildFromImageView(mIvDetail);// 动画矩形
            animationRectBeanArrayList.add(rect);
        } catch (Exception e) {
            LogUtils.d("Cathy", e.toString());
        }

    }

    public void updateDigList(CirclePostListBean circlePostDetailBean) {
        if (circlePostDetailBean == null) {
            return;
        }

        mDigListView.setDigCount(circlePostDetailBean.getLikes_count());
        mDigListView.setPublishTime(circlePostDetailBean.getCreated_at());
        mDigListView.setViewerCount(circlePostDetailBean.getViews_count());
        // 设置点赞头像
        mDigListView.setDigUserHeadIconPost(circlePostDetailBean.getDigList());

        // 点赞信息
        if (circlePostDetailBean.getDigList() != null
                && circlePostDetailBean.getDigList().size() > 0) {
            // 设置跳转到点赞列表
            mDigListView.setDigContainerClickListener(digContainer -> {
                DigListActivity.startDigActivity(mContext,circlePostDetailBean.getId(), BaseDigItem.DigTypeEnum.POST);
            });
        }
    }

    /**
     * 更新评论页面
     */
    public void updateCommentView(CirclePostListBean circlePostDetailBean) {
        // 评论信息
        if (circlePostDetailBean.getComments_count() != 0) {
            mCommentHintView.setVisibility(View.VISIBLE);
            mCommentCountView.setText(mContext.getString(R.string.dynamic_comment_count, circlePostDetailBean.getComments_count() + ""));
        } else {
            mCommentHintView.setVisibility(View.GONE);
        }
    }

    /**
     * 更新打赏内容
     *
     * @param sourceId         source id  for this reward
     * @param data             reward's users
     * @param rewardsCountBean all reward data
     * @param rewardType       reward type
     */
    public void updateReward(long sourceId, List<RewardsListBean> data, RewardsCountBean rewardsCountBean, RewardType rewardType, String moneyName) {
        mReWardView.initData(sourceId, data, rewardsCountBean, rewardType, moneyName);
        mReWardView.setOnRewardsClickListener(() -> {
        });
    }

    public void setReWardViewVisible(int visible) {
        mReWardView.setVisibility(visible);
    }

    public void setAdvertViewVisible(int visible) {
        if (visible == View.GONE || !com.zhiyicx.common.BuildConfig.USE_ADVERT) {
            mDynamicDetailAdvertHeader.hideAdvert();
        } else if (visible == View.VISIBLE && com.zhiyicx.common.BuildConfig.USE_ADVERT
                && mDynamicDetailAdvertHeader.getAdvertListBeans() != null && !mDynamicDetailAdvertHeader.getAdvertListBeans().isEmpty()) {
            mDynamicDetailAdvertHeader.showAdvert();
        }
    }

    /**
     * @param visible 0 正常，
     */
    public void setInfoReviewIng(int visible) {
        isReviewIng = true;
        setReWardViewVisible(visible);
        setAdvertViewVisible(visible);
        mInfoRelateList.setVisibility(visible);
        mFtlRelate.setVisibility(visible);
        mDigListView.setVisibility(visible);
        mRvRelateInfo.setVisibility(visible);
    }

    public void destroyedWeb() {
        destryWeb(mContent);
        destryWeb(mContentSubject);

    }

    public int scrollCommentToTop() {
        return mRewardView.getBottom();
    }

    public MarkdownView getContentWebView() {
        return mContent;
    }

    public MarkdownView getContentSubWebView() {
        return mContentSubject;
    }
}
