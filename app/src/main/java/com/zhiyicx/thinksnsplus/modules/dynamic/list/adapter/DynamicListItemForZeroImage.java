package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.baseproject.widget.textview.SpanTextViewWithEllipsize;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class DynamicListItemForZeroImage extends DynamicListBaseItem {

    protected
    @DrawableRes
    int[] mImageNormalResourceIds = new int[]{
            R.mipmap.ico_zan,
            com.zhiyicx.baseproject.R.mipmap.home_ico_comment_normal,
            R.mipmap.ico_share,
            com.zhiyicx.baseproject.R.mipmap.home_ico_more,
    };// 图片 ids 正常状态
    protected
    @DrawableRes
    int[] mImageCheckedResourceIds = new int[]{
            R.mipmap.ico_zan_on,
            com.zhiyicx.baseproject.R.mipmap.home_ico_comment_normal,
            R.mipmap.ico_share,
            com.zhiyicx.baseproject.R.mipmap.home_ico_more,
    };// 图片 ids 选中状态

    private OnFollowClickLisitener mOnFollowlistener;

    public DynamicListItemForZeroImage(Context context) {
        super(context);
    }


    public void setOnFollowlistener(OnFollowClickLisitener onFollowlistener) {
        mOnFollowlistener = onFollowlistener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_zero_image_for_tb;
    }

    @Override
    public boolean isForViewType(DynamicDetailBeanV2 item, int position) {
        return item.getFeed_mark() != null && (item.getImages() == null || item.getImages().isEmpty());
    }

    @Override
    public void convert(ViewHolder holder, DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        try {
            // 防止个人中心没后头像错误
            try {
                ImageUtils.loadCircleUserHeadPic(dynamicBean.getUserInfoBean(), holder.getView(R.id.iv_headpic));
                setUserInfoClick(holder.getView(R.id.iv_headpic), dynamicBean);
            } catch (Exception ignored) {
            }
            holder.setText(R.id.tv_name, dynamicBean.getUserInfoBean().getName());
            holder.setText(R.id.tv_time, dynamicBean.getFriendlyTime());
            holder.setVisible(R.id.tv_title, View.GONE);
            // 置顶标识 ,防止没有置顶布局错误
            try {
                // 待审核 也隐藏
                TextView topFlagView = holder.getView(R.id.tv_top_flag);
                topFlagView.setVisibility(dynamicBean.getTop() == DynamicDetailBeanV2.TOP_SUCCESS ?
                        View.VISIBLE : View.GONE);
                topFlagView.setText(mContext.getString(dynamicBean.getTop() ==
                        DynamicDetailBeanV2.TOP_REVIEW ?
                        R.string.review_ing : R.string.dynamic_top_flag));
            } catch (Exception ignored) {
            }

            setUserInfoClick(holder.getView(R.id.tv_name), dynamicBean);
            holder.setVisible(R.id.dlmv_menu, showToolMenu ? View.VISIBLE : View.GONE);
            // 分割线跟随工具栏显示隐藏
            holder.setVisible(R.id.v_line, showToolMenu ? View.VISIBLE : View.GONE);
            // user_id = -1 广告
            if (showToolMenu && dynamicBean.getUser_id() > 0) {
                // 显示工具栏
                DynamicListMenuView dynamicListMenuView = holder.getView(R.id.dlmv_menu);
                dynamicListMenuView.setImageNormalResourceIds(mImageNormalResourceIds);
                dynamicListMenuView.setImageCheckedResourceIds(mImageCheckedResourceIds);
                // 点赞
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                        .getFeed_digg_count()), dynamicBean.isHas_digg(), 0);
                // 分享数量
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean.getShare_count()),
                        false, 2);
                // 控制更多按钮的显示隐藏
                dynamicListMenuView.setItemPositionVisiable(0, getVisibleOne());
                dynamicListMenuView.setItemPositionVisiable(1, getVisibleTwo());
                dynamicListMenuView.setItemPositionVisiable(2, getVisibleThree());
                dynamicListMenuView.setItemPositionVisiable(3, getVisibleFour());
                // 设置工具栏的点击事件
                dynamicListMenuView.setItemOnClick((parent, v, menuPostion) -> {
                    if (mOnMenuItemClickLisitener != null) {
                        mOnMenuItemClickLisitener.onMenuItemClick(holder.getView(R.id.tv_follow), position, menuPostion);
                    }
                });
            }

            holder.setVisible(R.id.fl_tip, showReSendBtn ? View.VISIBLE : View.GONE);
            if (showReSendBtn) {
                // 设置动态发送状态
                if (dynamicBean.getState() == DynamicBean.SEND_ERROR) {
                    holder.setVisible(R.id.fl_tip, View.VISIBLE);
                } else {
                    holder.setVisible(R.id.fl_tip, View.GONE);
                }
                RxView.clicks(holder.getView(R.id.fl_tip))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> {
                            if (mOnReSendClickListener != null) {
                                mOnReSendClickListener.onReSendClick(position);
                            }
                        });
            }

            if (showCommentList) {
                holder.setVisible(R.id.dcv_comment, View.VISIBLE);

                // 设置评论内容
                DynamicListCommentView comment = holder.getView(R.id.dcv_comment);
                if (dynamicBean.getComments() == null || dynamicBean.getComments().isEmpty()) {
                    comment.setVisibility(View.GONE);
                } else {
                    comment.setVisibility(View.VISIBLE);
                }

                comment.setData(dynamicBean);
                comment.setOnCommentClickListener(mOnCommentClickListener);
                comment.setOnMoreCommentClickListener(mOnMoreCommentClickListener);
                comment.setOnCommentStateClickListener(mOnCommentStateClickListener);

            } else {
                holder.setVisible(R.id.dcv_comment, View.GONE);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        holder.setVisible(R.id.tv_follow, dynamicBean.getUserInfoBean().getUser_id() == AppApplication.getMyUserIdWithdefault() || dynamicBean
                .getUserInfoBean().getFollower() ? View.INVISIBLE : View.VISIBLE);
        if (mOnFollowlistener != null) {
            RxView.clicks(holder.getView(R.id.tv_follow))
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> {
                        mOnFollowlistener.onFollowClick(dynamicBean.getUserInfoBean(),holder.getView(R.id.tv_follow));
                        holder.setVisible(R.id.tv_follow, View.INVISIBLE);
                        dynamicBean.getUserInfoBean().setFollower(true);
                    });
        }
        /*
        文本内容处理
         */
        SpanTextViewWithEllipsize contentView = holder.getView(R.id.tv_content);
        contentView.setOnClickListener(v -> holder.getConvertView().performClick());
        contentView.setText(dynamicBean.getFriendlyContent());
        ConvertUtils.stringLinkConvert(contentView, setLiknks(dynamicBean, contentView.getText().toString()), false);
        if (dynamicBean.isOpen()) {
            contentView.setMaxLines(Integer.MAX_VALUE);
        } else {
            contentView.setMovementMethod(LinkMovementMethod.getInstance());
            contentView.setMaxLines(mMaxlinesShow);
            contentView.setEllipsize(TextUtils.TruncateAt.END);
        }
        contentView.setShowDot(!dynamicBean.isOpen(), mMaxlinesShow);
        contentView.setOnClickListener(v -> {
            if (dynamicBean.isOpen()) {
                dynamicBean.setOpen(false);
                contentView.setMovementMethod(LinkMovementMethod.getInstance());
                ((TextView) v).setMaxLines(mMaxlinesShow);
                contentView.setEllipsize(TextUtils.TruncateAt.END);

            } else {
                dynamicBean.setOpen(true);
                contentView.setMaxLines(Integer.MAX_VALUE);
                contentView.setText(dynamicBean.getFriendlyContent());
                ConvertUtils.stringLinkConvert(contentView, setLiknks(dynamicBean, contentView.getText().toString()), false);
            }
            contentView.setShowDot(!dynamicBean.isOpen(), mMaxlinesShow);
        });

    }

    @Override
    protected int getVisibleTwo() {
        return View.GONE;
    }

    /**
     * 关注点击监听
     */
    public interface OnFollowClickLisitener {
        void onFollowClick(UserInfoBean userInfoBean,TextView followView);
    }
}
