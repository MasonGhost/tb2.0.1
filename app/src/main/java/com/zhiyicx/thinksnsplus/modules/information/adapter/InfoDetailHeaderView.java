package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoTagsAdapter;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;
import com.zhiyicx.thinksnsplus.widget.ReWardView;
import com.zhiyicx.thinksnsplus.widget.flowtag.FlowTagLayout;
import com.zhiyicx.thinksnsplus.widget.flowtag.OnInitSelectedPosition;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/9
 * @contact email:648129313@qq.com
 */

public class InfoDetailHeaderView {

    private MarkdownView mContent;
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

    public View getInfoDetailHeader(){
        return mInfoDetailHeader;
    }

    public InfoDetailHeaderView(Context context, List<RealAdvertListBean> adverts) {
        this.mContext = context;
        mInfoDetailHeader = LayoutInflater.from(context).inflate(R.layout
                .item_info_comment_web, null);
        mInfoDetailHeader.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mTitle = (TextView) mInfoDetailHeader.findViewById(R.id.tv_info_title);
        mChannel = (TextView) mInfoDetailHeader.findViewById(R.id.tv_from_channel);
        mFrom = (TextView) mInfoDetailHeader.findViewById(R.id.item_info_timeform);
        mContent = (MarkdownView) mInfoDetailHeader.findViewById(R.id.info_detail_content);
        mDigListView = (DynamicHorizontalStackIconView) mInfoDetailHeader.findViewById(R.id.detail_dig_view);
        mReWardView = (ReWardView) mInfoDetailHeader.findViewById(R.id.v_reward);
        mCommentHintView = (FrameLayout) mInfoDetailHeader.findViewById(R.id.info_detail_comment);
        mCommentCountView = (TextView) mInfoDetailHeader.findViewById(R.id.tv_comment_count);
        mInfoRelateList = (FrameLayout) mInfoDetailHeader.findViewById(R.id.info_relate_list);
        mFtlRelate = (TagFlowLayout) mInfoDetailHeader.findViewById(R.id.fl_tags);
        mRvRelateInfo = (RecyclerView) mInfoDetailHeader.findViewById(R.id.rv_relate_info);
    }

    public void setDetail(InfoListDataBean infoMain){
        if (infoMain != null){
            mTitle.setText(infoMain.getTitle());
            mChannel.setVisibility(infoMain.getCategory() == null ? GONE : VISIBLE);
            mChannel.setText(infoMain.getCategory() == null ? "" : infoMain.getCategory().getName());
            String from = infoMain.getFrom().equals(mContext.getString(R.string.info_publish_original)) ?
                    infoMain.getAuthor() : infoMain.getFrom();
            String infoData = String.format(mContext.getString(R.string.info_list_count)
                    , from, infoMain.getHits(), TimeUtils.getTimeFriendlyNormal(infoMain
                            .getUpdated_at()));
            mFrom.setText(infoData);
            // 资讯content
            if (!TextUtils.isEmpty(infoMain.getContent())) {
                InternalStyleSheet css = new Github();
                mContent.addStyleSheet(css);
                String subject = infoMain.getSubject();
                mContent.loadMarkdown(subject + dealPic(infoMain.getContent()));
            }
            // 评论信息
            if (infoMain.getComment_count() != 0) {
                mCommentHintView.setVisibility(View.VISIBLE);
                mCommentCountView.setText(mContext.getString(R.string.dynamic_comment_count, infoMain.getComment_count() + ""));
            } else {
                mCommentHintView.setVisibility(View.GONE);
            }
        }
    }

    private String dealPic(String markDownContent) {
        // 替换图片id 为地址
        String tag = "@![image](";
        while (markDownContent.contains(tag)) {
            int start = markDownContent.indexOf(tag) + tag.length();
            int end = markDownContent.indexOf(")", start);
            String id = "";
            try {
                id = markDownContent.substring(start, end);
            } catch (Exception e) {
                LogUtils.d("Cathy", e.toString());
            }
            String imgPath = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/" + id + "?q=80";
            markDownContent = markDownContent.replace(tag + id + ")", "![image](" + imgPath + ")");
        }
        return markDownContent;
    }

    public void updateDigList(InfoListDataBean infoMain){
        if (infoMain == null){
            return;
        }
        // 点赞信息
        if (infoMain.getDigList() != null
                && infoMain.getDigList().size() > 0) {
            mDigListView.setVisibility(VISIBLE);
            mDigListView.setDigCount(infoMain.getDigg_count());
            mDigListView.setPublishTime(infoMain.getUpdated_at());
            mDigListView.setViewerCount(infoMain.getHits());
            // 设置点赞头像
            mDigListView.setDigUserHeadIconInfo(infoMain.getDigList());

            // 设置跳转到点赞列表
            mDigListView.setDigContainerClickListener(digContainer -> {
                Bundle bundle = new Bundle();
//                        bundle.putParcelable(DigListFragment.DIG_LIST_DATA, dynamicBean);
//                        Intent intent = new Intent(mDynamicDetailHeader.getContext(), DigListActivity
//                                .class);
//                        intent.putExtras(bundle);
//                        mDynamicDetailHeader.getContext().startActivity(intent);
            });
        } else {
            mDigListView.setVisibility(GONE);
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

    public void setRelateInfo(InfoListDataBean infoMain){
        List<InfoListDataBean> infoListDataBeen = infoMain.getRelateInfoList();
        if (infoListDataBeen != null && infoListDataBeen.size() > 0){
            mInfoRelateList.setVisibility(VISIBLE);
            mFtlRelate.setVisibility(VISIBLE);
            mRvRelateInfo.setVisibility(VISIBLE);
            // 标签
            List<UserTagBean> tagBeanList = infoMain.getTags();
            if (tagBeanList != null && tagBeanList.size() > 0){
                UserInfoTagsAdapter mUserInfoTagsAdapter = new UserInfoTagsAdapter(tagBeanList, mContext);
                mFtlRelate.setAdapter(mUserInfoTagsAdapter);
            }
            LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mRvRelateInfo.setLayoutManager(manager);
            mRvRelateInfo.setNestedScrollingEnabled(false);
            mRvRelateInfo.setAdapter(new CommonAdapter<InfoListDataBean>(mContext, R.layout.item_info, infoListDataBeen) {

                @Override
                protected void convert(ViewHolder holder, InfoListDataBean infoListDataBean, int position) {
                    final TextView title = holder.getView(R.id.item_info_title);
                    final ImageView imageView = holder.getView(R.id.item_info_imag);
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
                    String infoData = String.format(title.getContext().getString(R.string.info_list_count)
                            , from, infoListDataBean.getHits(), TimeUtils.getTimeFriendlyNormal(infoListDataBean
                                    .getUpdated_at()));
                    holder.setText(R.id.item_info_timeform, infoData);
                    // 是否置顶
                    holder.setVisible(R.id.tv_top_flag, infoListDataBean.isTop() ? View.VISIBLE : View.GONE);
                    holder.itemView.setOnClickListener(v -> {
                        // 跳转到新的咨询页
                    });
                }
            });
        } else {
            mInfoRelateList.setVisibility(GONE);
            mFtlRelate.setVisibility(GONE);
            mRvRelateInfo.setVisibility(GONE);
        }
    }
}
