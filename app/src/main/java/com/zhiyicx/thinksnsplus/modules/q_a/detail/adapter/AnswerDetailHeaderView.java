package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
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
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;
import com.zhiyicx.thinksnsplus.widget.ReWardView;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/9
 * @contact email:648129313@qq.com
 */

public class AnswerDetailHeaderView {

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
        mContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mAnswerHeaderEventListener != null) {
                    mAnswerHeaderEventListener.loadFinish();
                }
            }
        });

        mUserFollow.setOnCheckedChangeListener((buttonView, isChecked) -> mAnswerHeaderEventListener.userFollowClick(isChecked));
        if (adverts != null) {
            initAdvert(context, adverts);
        }
    }

    public void setDetail(AnswerInfoBean answerInfoBean) {
        if (answerInfoBean != null) {
            // 资讯content
            if (!TextUtils.isEmpty(answerInfoBean.getBody())) {
                InternalStyleSheet css = new Github();
                css.addRule("body", "line-height: 1.6", "padding: 10px");
                css.addRule(".container", "padding-right:0", ";padding-left:0");
                mContent.addStyleSheet(css);
                mContent.loadMarkdown(dealPic(answerInfoBean.getBody()));
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
            mName.setText(answerInfoBean.getUser().getName());
            mDescription.setText(answerInfoBean.getUser().getIntro());
            boolean isSelf = answerInfoBean.getUser().getExtra().getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id();
            mUserFollow.setVisibility(isSelf ? GONE : VISIBLE);
            mUserFollow.setChecked(answerInfoBean.getUser().isFollower());
            // 评论信息
            updateCommentView(answerInfoBean);

            ImageUtils.loadUserHead(answerInfoBean.getUser(), mUserAvatarView, false, answerInfoBean.getAnonymity() == 1);

        }
    }

    private void showMarkDownView(TextView textView, String content) {
        textView.post(() -> {
            RichText
                    .fromMarkdown(content) // 数据源
                    .autoFix(true) // 是否自动修复，默认true
                    .autoPlay(true) // gif图片是否自动播放
                    .showBorder(true) // 是否显示图片边框
                    .borderColor(Color.BLUE) // 图片边框颜色
                    .borderSize(10) // 边框尺寸
                    .borderRadius(50) // 图片边框圆角弧度
                    .scaleType(ImageHolder.ScaleType.FIT_CENTER) // 图片缩放方式
                    .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT) // 图片占位区域的宽高
                    .noImage(true) // 不显示并且不加载图片
                    .resetSize(false) // 默认false，是否忽略img标签中的宽高尺寸（只在img标签中存在宽高时才有效），true：忽略标签中的尺寸并触发SIZE_READY回调，false：使用img标签中的宽高尺寸，不触发SIZE_READY回调
                    .clickable(true) // 是否可点击，默认只有设置了点击监听才可点击
                    .into(textView); // 设置目标TextView
        });
    }

    private void initAdvert(Context context, List<RealAdvertListBean> adverts) {
        mDynamicDetailAdvertHeader = new DynamicDetailAdvertHeader(context, mAnswerDetailHeader
                .findViewById(R.id.ll_advert));
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || adverts.isEmpty()) {
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
        String tag = "@![image](";
        while (markDownContent.contains(tag)) {
            int start = markDownContent.indexOf(tag) + tag.length();
            int end = markDownContent.indexOf(")", start);
            String id = "0";
            try {
                id = markDownContent.substring(start, end);
            } catch (Exception e) {
                LogUtils.d("Cathy", e.toString());
            }
            String imgPath = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/" + id + "?q=80";
            markDownContent = markDownContent.replace(tag + id + ")", "![image](" + imgPath + ")");
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
        toll.setToll_money(0f);// 付费金额
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
        // 点赞信息
        if (answerInfoBean.getLikes() != null
                && answerInfoBean.getLikes().size() > 0) {
            mDigListView.setVisibility(VISIBLE);
            mDigListView.setDigCount(answerInfoBean.getLikes_count());
            mDigListView.setPublishTime(answerInfoBean.getUpdated_at());
            mDigListView.setViewerCount(answerInfoBean.getViews_count());
            // 设置点赞头像
            mDigListView.setDigUserHeadIconAnswer(answerInfoBean.getLikes());

            // 设置跳转到点赞列表
            mDigListView.setDigContainerClickListener(digContainer -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(AnswerDigListFragment.DIG_LIST_DATA, answerInfoBean);
                Intent intent = new Intent(mContext, AnswerDigListActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            });
        } else {
            mDigListView.setVisibility(GONE);
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
    public void updateReward(long sourceId, List<RewardsListBean> data, RewardsCountBean rewardsCountBean, RewardType rewardType) {
        mReWardView.initData(sourceId, data, rewardsCountBean, rewardType);
        mReWardView.setOnRewardsClickListener(() -> {

        });
    }

    public void setAnswerHeaderEventListener(AnswerHeaderEventListener answerHeaderEventListener) {
        mAnswerHeaderEventListener = answerHeaderEventListener;
    }

    public interface AnswerHeaderEventListener {
        void loadFinish();

        void userFollowClick(boolean isChecked);

        void clickUserInfo(UserInfoBean user);
    }
}
