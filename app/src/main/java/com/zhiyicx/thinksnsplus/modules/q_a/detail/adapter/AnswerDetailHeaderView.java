package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseWebLoad;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.dig_list.AnswerDigListActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.dig_list.AnswerDigListFragment;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.utils.MarkDownRule;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;
import com.zhiyicx.thinksnsplus.widget.ReWardView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.MarkdownConfig.IMAGE_FORMAT;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/9
 * @contact email:648129313@qq.com
 */

public class AnswerDetailHeaderView extends BaseWebLoad{


    private UserAvatarView mUserAvatarView;
    private TextView mName;
    private TextView mDescription;
    private CheckBox mUserFollow;
    private MarkdownView mContent;

    private DynamicHorizontalStackIconView mDigListView;
    private ReWardView mReWardView;
    private FrameLayout mCommentHintView;
    private TextView mCommentCountView;

    private View mAnswerDetailHeader;
    private RelativeLayout mUserInfoContainer;
    private Context mContext;
    private int screenWidth;
    private int picWidth;
    private Bitmap sharBitmap;
    private List<ImageBean> mImgList;
    private ImageView mIvDetail;

    private DynamicDetailAdvertHeader mDynamicDetailAdvertHeader;
    private ArrayList<AnimationRectBean> animationRectBeanArrayList;

    private AnswerHeaderEventListener mAnswerHeaderEventListener;

    public View getAnswerDetailHeader() {
        return mAnswerDetailHeader;
    }

    public AnswerDetailHeaderView(Context context, List<RealAdvertListBean> adverts) {
        this.mContext = context;
        mImgList = new ArrayList<>();
        animationRectBeanArrayList = new ArrayList<>();

        mAnswerDetailHeader = LayoutInflater.from(context).inflate(R.layout
                .item_answer_comment_header, null);

        mAnswerDetailHeader.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        mUserAvatarView = (UserAvatarView) mAnswerDetailHeader.findViewById(R.id.iv_head_icon);
        mUserInfoContainer = (RelativeLayout) mAnswerDetailHeader.findViewById(R.id.rl_userinfo_container);
        mName = (TextView) mAnswerDetailHeader.findViewById(R.id.tv_user_name);
        mDescription = (TextView) mAnswerDetailHeader.findViewById(R.id.tv_user_desc);
        mUserFollow = (CheckBox) mAnswerDetailHeader.findViewById(R.id.tv_user_follow);
        mContent = (MarkdownView) mAnswerDetailHeader.findViewById(R.id.answer_detail_content);
        mDigListView = (DynamicHorizontalStackIconView) mAnswerDetailHeader.findViewById(R.id.detail_dig_view);
        mReWardView = (ReWardView) mAnswerDetailHeader.findViewById(R.id.v_reward);
        mCommentHintView = (FrameLayout) mAnswerDetailHeader.findViewById(R.id.answer_detail_comment);
        mCommentCountView = (TextView) mAnswerDetailHeader.findViewById(R.id.tv_comment_count);
        mIvDetail = (ImageView) mAnswerDetailHeader.findViewById(R.id.iv_detail);


        initAdvert(context, adverts);
    }

    public void setDetail(AnswerInfoBean answerInfoBean) {
        if (answerInfoBean != null) {
            // 资讯content
            if (!TextUtils.isEmpty(answerInfoBean.getBody())) {
                mContent.addStyleSheet(MarkDownRule.generateStandardStyle());
                mContent.loadMarkdown(dealPic(answerInfoBean.getBody()));
                mContent.setWebChromeClient(mWebChromeClient);
                mContent.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        CustomWEBActivity.startToOutWEBActivity(mContext, url);
                        return true;
                    }
                });
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
//                        CustomWEBActivity.startToOutWEBActivity(mContext, s1);
                    }

                    @Override
                    public void onKeystrokeTap(String s) {

                    }

                    @Override
                    public void onMarkTap(String s) {

                    }
                });
            }
            RxView.clicks(mUserInfoContainer)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> {
                        if (mAnswerHeaderEventListener != null && answerInfoBean.getAnonymity() != 1) {
                            mAnswerHeaderEventListener.clickUserInfo(answerInfoBean.getUser());
                        }
                    });

            boolean isAnonmity = answerInfoBean.getAnonymity() == 1;
            boolean isSelf = answerInfoBean.getUser_id() == AppApplication.getMyUserIdWithdefault();
            mDescription.setText(isSelf || !isAnonmity ? answerInfoBean.getUser().getIntro() : "");
            mUserFollow.setVisibility((isAnonmity || isSelf) ? GONE : VISIBLE);
            // 自己的匿名回答，增加匿名提示
            if (isAnonmity) {
                mName.setText(!isSelf ? mContext.getResources().getString(R.string.qa_question_answer_anonymity_user)
                        : answerInfoBean.getUser().getName() + mContext.getString(R.string.qa_question_answer_anonymity_current_user));
                ConvertUtils.stringLinkConvert(mName, setLinks());
            } else {
                mName.setText(answerInfoBean.getUser().getName());
            }
            mUserFollow.setChecked(!isAnonmity && answerInfoBean.getUser().isFollower());
            // 评论信息
            updateCommentView(answerInfoBean);
            mUserFollow.setOnCheckedChangeListener((buttonView, isChecked) -> mAnswerHeaderEventListener.userFollowClick(isChecked));
            ImageUtils.loadUserHead(answerInfoBean.getUser(), mUserAvatarView, false, !isSelf && isAnonmity);

        }
    }

    private List<Link> setLinks() {
        List<Link> links = new ArrayList<>();
        Link followCountLink = new Link(mContext.getString(R.string.qa_question_answer_anonymity_current_user)).setTextColor(ContextCompat.getColor(mContext, R.color
                .normal_for_assist_text))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(mContext, R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(followCountLink);
        return links;
    }

    private void initAdvert(Context context, List<RealAdvertListBean> adverts) {
        mDynamicDetailAdvertHeader = new DynamicDetailAdvertHeader(context, mAnswerDetailHeader
                .findViewById(R.id.ll_advert));
        boolean noAdvert=!com.zhiyicx.common.BuildConfig.USE_ADVERT || adverts == null || (adverts != null && adverts.isEmpty());
        if (noAdvert) {
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

    public void updateUserFollow(boolean isFollowed) {
        mUserFollow.setChecked(isFollowed);
        mUserFollow.setText(isFollowed ? mUserFollow.getContext().getString(R.string.qa_topic_followed) : mUserFollow.getContext().getString(R.string.qa_topic_follow));
        mUserFollow.setPadding(isFollowed ? mContext.getResources().getDimensionPixelSize(R.dimen.spacing_small) : mContext.getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, 0, 0);
    }

    public void updateDigList(AnswerInfoBean answerInfoBean) {
        if (answerInfoBean == null) {
            return;
        }

        mDigListView.setDigCount(answerInfoBean.getLikes_count());
        mDigListView.setPublishTime(answerInfoBean.getCreated_at());
        mDigListView.setViewerCount(answerInfoBean.getViews_count());
        mDigListView.setDigUserHeadIconAnswer(answerInfoBean.getLikes());

        if (answerInfoBean.getLikes() != null
                && answerInfoBean.getLikes().size() > 0) {
            // 设置跳转到点赞列表
            mDigListView.setDigContainerClickListener(digContainer -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(AnswerDigListFragment.DIG_LIST_DATA, answerInfoBean);
                Intent intent = new Intent(mContext, AnswerDigListActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            });
        }
    }

    /**
     * 更新评论页面
     */
    public void updateCommentView(AnswerInfoBean answerInfoBean) {
        // 评论信息
        if (answerInfoBean.getComments_count() != 0) {
            mCommentHintView.setVisibility(View.VISIBLE);
            mCommentCountView.setText(mContext.getString(R.string.dynamic_comment_count, answerInfoBean.getComments_count() + ""));
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

    public void setAnswerHeaderEventListener(AnswerHeaderEventListener answerHeaderEventListener) {
        mAnswerHeaderEventListener = answerHeaderEventListener;
    }

    public MarkdownView getContentWebView() {
        return mContent;
    }

    public interface AnswerHeaderEventListener {
        void userFollowClick(boolean isChecked);

        void clickUserInfo(UserInfoBean user);
    }
    public void destroyedWeb(){
        destryWeb(mContent);

    }
}
