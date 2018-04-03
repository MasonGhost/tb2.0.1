package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseWebLoad;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.beans.WordResourceBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoTagsAdapter;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.tb.word.WordActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.utils.MarkDownRule;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;
import com.zhiyicx.thinksnsplus.widget.ReWardView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.tiagohm.markdownview.MarkdownView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.MarkdownConfig.IMAGE_FORMAT;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/9
 * @contact email:648129313@qq.com
 */

public class InfoDetailHeaderView extends BaseWebLoad {
    private MarkdownView mContent;
    private MarkdownView mContentSubject;
    private TextView mTitle;
    private TextView mChannel;
    private TextView mFrom;
    private TextView mItemInfoAuthor;
    private TextView mItemInfoMerchainName;
    private DynamicHorizontalStackIconView mDigListView;
    private ReWardView mReWardView;
    private FrameLayout mCommentHintView;
    private TextView mCommentCountView;
    private FrameLayout mInfoRelateList;
    private TagFlowLayout mFtlRelate;
    private RecyclerView mRvRelateInfo;
    private View mInfoDetailHeader;
    private Context mContext;
    private List<ImageBean> mImgList;
    private ImageView mIvDetail;
    private LinearLayout mLLWord;
    private TextView mTvWriteComment;
    private boolean isReviewIng;

    private DynamicDetailAdvertHeader mDynamicDetailAdvertHeader;
    private ArrayList<AnimationRectBean> animationRectBeanArrayList;

    public View getInfoDetailHeader() {
        return mInfoDetailHeader;
    }

    public InfoDetailHeaderView(Context context, List<RealAdvertListBean> adverts) {
        this.mContext = context;
        mImgList = new ArrayList<>();
        animationRectBeanArrayList = new ArrayList<>();
        mInfoDetailHeader = LayoutInflater.from(context).inflate(R.layout
                .item_info_comment_web, null);
        mInfoDetailHeader.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mTitle = (TextView) mInfoDetailHeader.findViewById(R.id.tv_info_title);
        mChannel = (TextView) mInfoDetailHeader.findViewById(R.id.tv_from_channel);
        mFrom = (TextView) mInfoDetailHeader.findViewById(R.id.item_info_timeform);
        mContent = (MarkdownView) mInfoDetailHeader.findViewById(R.id.info_detail_content);
        mContentSubject = (MarkdownView) mInfoDetailHeader.findViewById(R.id.info_content_subject);
        mDigListView = (DynamicHorizontalStackIconView) mInfoDetailHeader.findViewById(R.id.detail_dig_view);
        mDigListView.setVisibility(View.GONE);
        mReWardView = (ReWardView) mInfoDetailHeader.findViewById(R.id.v_reward);
        mCommentHintView = (FrameLayout) mInfoDetailHeader.findViewById(R.id.info_detail_comment);
        mCommentCountView = (TextView) mInfoDetailHeader.findViewById(R.id.tv_comment_count);
        mInfoRelateList = (FrameLayout) mInfoDetailHeader.findViewById(R.id.info_relate_list);
        mFtlRelate = (TagFlowLayout) mInfoDetailHeader.findViewById(R.id.fl_tags);
        mRvRelateInfo = (RecyclerView) mInfoDetailHeader.findViewById(R.id.rv_relate_info);
        mIvDetail = (ImageView) mInfoDetailHeader.findViewById(R.id.iv_detail);
        mItemInfoAuthor = (TextView) mInfoDetailHeader.findViewById(R.id.item_info_author);
        mItemInfoMerchainName = (TextView) mInfoDetailHeader.findViewById(R.id.item_info_merchain_name);
        mLLWord = (LinearLayout) mInfoDetailHeader.findViewById(R.id.ll_word);
        mTvWriteComment = (TextView) mInfoDetailHeader.findViewById(R.id.tv_write_comment);
        initAdvert(context, adverts);
    }

    public void setDetail(InfoListDataBean infoMain) {
        if (infoMain != null) {
            mTitle.setText(infoMain.getTitle());
//            mChannel.setVisibility(infoMain.getCategory() == null ? GONE : VISIBLE);
//            mChannel.setText(infoMain.getCategory() == null ? "" : infoMain.getCategory().getName());
            String from = TimeUtils.getYeayMonthDay(TimeUtils.utc2LocalLong(infoMain.getCreated_at()));
            if (!TextUtils.isEmpty(from)) {
                mFrom.setText(from);
            }
            mItemInfoAuthor.setText(infoMain.getAuthor());
            mItemInfoMerchainName.setText(infoMain.getUser().getName());
            try {
                UserInfoBean userInfoBean=new UserInfoBean();
                userInfoBean.setName(infoMain.getAuthor());
                userInfoBean.setUser_id(infoMain.getUser().getUser_id());
                userInfoBean.setAvatar(infoMain.getUser().getAvatar());
                mReWardView.setUserInfoBean(infoMain.getUser());
            }catch (Exception e){}
            // 引用
//            if (!TextUtils.isEmpty(infoMain.getSubject())) {
//                infoMain.setSubject(InfoPublishBean.DEFALUT_SUBJECT + infoMain.getSubject() + "\n\n");
//                mContentSubject.setVisibility(VISIBLE);
//                mContentSubject.addStyleSheet(MarkDownRule.generateStandardQuoteStyle());
//                mContentSubject.loadMarkdown(infoMain.getSubject());
//            } else {
//                mContentSubject.setVisibility(GONE);
//            }
            // 资讯contente
            if (!TextUtils.isEmpty(infoMain.getContent())) {
                mContent.addStyleSheet(MarkDownRule.generateStandardStyle());
                mContent.loadMarkdown(dealPic(infoMain.getContent()));
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
            // 评论信息
            updateCommentView(infoMain);
            mLLWord.setVisibility(infoMain.getComment_status() == 1 ? VISIBLE : GONE);
            mTvWriteComment.setOnClickListener(mTvWriteComment -> {
                String content = TextUtils.isEmpty(infoMain.getText_content()) ? infoMain.getContent() : infoMain.getText_content();
                WordActivity.startWordActivity(mContext, new WordResourceBean(infoMain.getUser(), infoMain.getId().toString(), infoMain.getTitle(), content));
            });
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

    public void updateDigList(InfoListDataBean infoMain) {
//        if (infoMain == null) {
//            return;
//        }
//
//        mDigListView.setDigCount(infoMain.getDigg_count());
//        mDigListView.setPublishTime(infoMain.getCreated_at());
//        mDigListView.setViewerCount(infoMain.getHits());
//        // 设置点赞头像
//        mDigListView.setDigUserHeadIconInfo(infoMain.getDigList());
//
//        // 点赞信息
//        if (infoMain.getDigList() != null
//                && infoMain.getDigList().size() > 0) {
//            // 设置跳转到点赞列表
//            mDigListView.setDigContainerClickListener(digContainer -> {
//                Intent intent = new Intent(mContext, InfoDigListActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(InfoDigListActivity.BUNDLE_INFO_DIG, infoMain);
//                intent.putExtra(InfoDigListActivity.BUNDLE_INFO_DIG, bundle);
//                mContext.startActivity(intent);
//            });
//        }
    }

    /**
     * 更新评论页面
     */
    public void updateCommentView(InfoListDataBean infoMain) {
        // 评论信息
        if (infoMain.getComment_count() != 0) {
            //mCommentHintView.setVisibility(View.VISIBLE);
            mCommentCountView.setText(mContext.getString(R.string.dynamic_comment_count, infoMain.getComment_count() + ""));
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

    public void setRelateInfo(InfoListDataBean infoMain) {
        List<InfoListDataBean> infoListDataBeen = infoMain.getRelateInfoList();
        if (infoListDataBeen != null && infoListDataBeen.size() > 0) {
            if (!isReviewIng) {
//                mInfoRelateList.setVisibility(VISIBLE);
//                mFtlRelate.setVisibility(VISIBLE);
//                mRvRelateInfo.setVisibility(VISIBLE);
            }

            // 标签
            List<UserTagBean> tagBeanList = infoMain.getTags();
            if (tagBeanList != null && tagBeanList.size() > 0) {
                UserInfoTagsAdapter mUserInfoTagsAdapter = new UserInfoTagsAdapter(tagBeanList, mContext);
                mFtlRelate.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        return false;
                    }
                });
                mFtlRelate.setAdapter(mUserInfoTagsAdapter);
            }
            LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mRvRelateInfo.setLayoutManager(manager);
            mRvRelateInfo.setNestedScrollingEnabled(false);
            CommonAdapter adapter = new CommonAdapter<InfoListDataBean>(mContext, R.layout.item_info, infoListDataBeen) {
                @Override
                protected void convert(ViewHolder holder, InfoListDataBean infoListDataBean, int position) {
                    final TextView title = holder.getView(R.id.item_info_title);
                    final ImageView imageView = holder.getView(R.id.item_info_imag);

                    // 记录点击过后颜色
                    if (AppApplication.sOverRead.contains(infoListDataBean.getId())) {
                        title.setTextColor(SkinUtils.getColor(R.color.normal_for_assist_text));
                    }

                    title.setText(infoListDataBean.getTitle());
                    if (infoListDataBean.getImage() == null) {
                        imageView.setVisibility(View.GONE);
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        Glide.with(BaseApplication.getContext())
                                .load(ImageUtils.imagePathConvertV2(infoListDataBean.getImage().getId(), imageView.getWidth(), imageView.getHeight(),
                                        ImageZipConfig.IMAGE_80_ZIP))
                                .placeholder(R.drawable.shape_default_image)
                                .error(R.drawable.shape_default_image)
                                .override(imageView.getContext().getResources().getDimensionPixelOffset(R.dimen.info_channel_list_image_width)
                                        , imageView.getContext().getResources().getDimensionPixelOffset(R.dimen.info_channel_list_height))
                                .into(imageView);
                    }
                    // 来自单独分开
                    String category = infoListDataBean.getCategory() == null ? "" : infoListDataBean.getCategory().getName();
                    holder.setText(R.id.tv_from_channel, category);
                    // 投稿来源，浏览数，时间
                    String from = infoListDataBean.getFrom().equals(title.getContext().getString(R.string.info_publish_original)) ?
                            infoListDataBean.getAuthor() : infoListDataBean.getFrom();
                    String infoData = String.format(Locale.getDefault(), title.getContext().getString(R.string.info_list_count)
                            , from, String.valueOf(infoListDataBean.getHits()), TimeUtils.getTimeFriendlyNormal(infoListDataBean
                                    .getUpdated_at()));
                    holder.setText(R.id.item_info_timeform, infoData);
                    // 是否置顶
                    holder.setVisible(R.id.tv_top_flag, infoListDataBean.isTop() ? View.VISIBLE : View.GONE);
                    holder.itemView.setOnClickListener(v -> {
                        if (!AppApplication.sOverRead.contains(infoListDataBean.getId())) {
                            AppApplication.sOverRead.add(infoListDataBean.getId().intValue());
                        }
                        FileUtils.saveBitmapToFile(mContext, ConvertUtils.drawable2BitmapWithWhiteBg(getContext()
                                , imageView.getDrawable(), R.mipmap.icon), "info_share");
                        title.setTextColor(mContext.getResources()
                                .getColor(R.color.normal_for_assist_text));
                        // 跳转到新的咨询页
                        Intent intent = new Intent(mContext, InfoDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(BUNDLE_INFO, infoListDataBeen.get(position));
                        intent.putExtra(BUNDLE_INFO, bundle);
                        mContext.startActivity(intent);
                    });
                }
            };
            mRvRelateInfo.setAdapter(adapter);
        } else {
//            mInfoRelateList.setVisibility(GONE);
            mFtlRelate.setVisibility(GONE);
            mRvRelateInfo.setVisibility(GONE);
        }
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
//        mInfoRelateList.setVisibility(visible);
//        mFtlRelate.setVisibility(visible);
//        mDigListView.setVisibility(visible);
        mRvRelateInfo.setVisibility(visible);
    }

    public void destroyedWeb() {
        destryWeb(mContent);
        destryWeb(mContentSubject);

    }

    public MarkdownView getContentWebView() {
        return mContent;
    }

    public MarkdownView getContentSubWebView() {
        return mContentSubject;
    }
}
