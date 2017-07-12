package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/7/5/20:55
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MessageReviewFragment extends TSListFragment<MessageReviewContract.Presenter,
        TopDynamicCommentBean> implements MessageReviewContract.View, MultiItemTypeAdapter.OnItemClickListener {

    private ImageLoader mImageLoader;

    private ActionPopupWindow mReviewPopWindow;

    public MessageReviewFragment() {
    }

    public static MessageReviewFragment newInstance() {
        MessageReviewFragment fragment = new MessageReviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.review);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, 1, 0, 0, ContextCompat.getDrawable(getContext(), R.drawable.shape_recyclerview_divider));
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
    protected void initData() {
        super.initData();
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
    }

    @Override
    protected CommonAdapter<TopDynamicCommentBean> getAdapter() {
        CommonAdapter<TopDynamicCommentBean> adapter = new CommonAdapter<TopDynamicCommentBean>
                (getContext(), R.layout.item_message_review_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, TopDynamicCommentBean
                    topDynamicCommentBean, int position) {

                if (position == getItemCount() - 1) {
                    holder.setVisible(R.id.v_bottom_line, View.GONE);
                } else {
                    holder.setVisible(R.id.v_bottom_line, View.VISIBLE);
                }

                int storegeId;
                String userIconUrl;
                try {
                    storegeId = Integer.parseInt(topDynamicCommentBean.getUserInfoBean().getAvatar());
                    userIconUrl = ImageUtils.imagePathConvertV2(storegeId
                            , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                            , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                            , ImageZipConfig.IMAGE_38_ZIP);
                } catch (Exception e) {
                    userIconUrl = topDynamicCommentBean.getUserInfoBean().getAvatar();
                }
                mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                        .url(userIconUrl)
                        .transformation(new GlideCircleTransform(getContext()))
                        .errorPic(R.mipmap.pic_default_portrait1)
                        .placeholder(R.mipmap.pic_default_portrait1)
                        .imagerView(holder.getView(R.id.iv_headpic))
                        .build());

                holder.setVisible(R.id.iv_detail_image, View.GONE);
                holder.setVisible(R.id.tv_deatil, View.VISIBLE);
                holder.setText(R.id.tv_deatil, topDynamicCommentBean.getFeed().getContent());

                String content = String.format(getString(R.string.review_description), (float) topDynamicCommentBean.getAmount(),
                        topDynamicCommentBean.getComment().getContent());

                TextView contentView = holder.getView(R.id.tv_content);
                contentView.setText(content);
                List<Link> links = setLiknks(holder, String.format(getString(R.string.dynamic_send_toll_select_money),
                        (float) topDynamicCommentBean.getAmount()), topDynamicCommentBean.getComment().getContent());
                contentView.setLinksClickable(false);// 不能消费了点击事件啊
                if (!links.isEmpty()) {
                    ConvertUtils.stringLinkConvert(contentView, links);
                }

                holder.setText(R.id.tv_name, topDynamicCommentBean.getUserInfoBean().getName());
                holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(topDynamicCommentBean.getCreated_at()));
                // 响应事件
                RxView.clicks(holder.getView(R.id.tv_name))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> toUserCenter(topDynamicCommentBean.getUserInfoBean()));
                RxView.clicks(holder.getView(R.id.iv_headpic))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> toUserCenter(topDynamicCommentBean.getUserInfoBean()));
                RxView.clicks(holder.getConvertView())
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> initReviewPopWindow(topDynamicCommentBean));
            }
        };
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nothing;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getActivity(), userInfoBean);
    }

    private void initReviewPopWindow(TopDynamicCommentBean topDynamicCommentBean) {
        if (mReviewPopWindow != null) {
            mReviewPopWindow.show();
            return;
        }
        mReviewPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.review_approved))
                .item2Str(getString(R.string.review_refuse))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mPresenter.approvedTopComment((long) topDynamicCommentBean.getFeed().getId(),
                            topDynamicCommentBean.getComment().getId(), topDynamicCommentBean.getId().intValue());
                    mReviewPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    mPresenter.refuseTopComment(topDynamicCommentBean.getId().intValue());
                    mReviewPopWindow.hide();
                })
                .bottomClickListener(() -> mReviewPopWindow.hide())
                .build();
        mReviewPopWindow.show();
    }

    private List<Link> setLiknks(ViewHolder holder, final String money, final String comment) {
        List<Link> links = new ArrayList<>();
        Link moneyLink = new Link(money)
                .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_note))                  // optional, defaults to holo blue
                .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                .setHighlightAlpha(.5f)
                .setUnderlined(false);

        Link commentLink = new Link(comment)
                .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.normal_for_disable_button_text))                  // optional, defaults to holo blue
                .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                .setHighlightAlpha(.5f)
                .setUnderlined(false);

        links.add(moneyLink);
        links.add(commentLink);
        return links;
    }
}
