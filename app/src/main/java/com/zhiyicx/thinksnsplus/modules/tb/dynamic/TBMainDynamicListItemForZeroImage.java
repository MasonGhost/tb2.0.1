package com.zhiyicx.thinksnsplus.modules.tb.dynamic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.baseproject.widget.textview.SpanTextViewWithEllipsize;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
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

public class TBMainDynamicListItemForZeroImage extends DynamicListBaseItem {

    protected
    @DrawableRes
    int[] mImageNormalResourceIds = new int[]{
            R.mipmap.ico_share,
            R.mipmap.home_ico_comment_normal,
            R.mipmap.ico_zan,
            R.mipmap.home_ico_more,
    };// 图片 ids 正常状态
    protected
    @DrawableRes
    int[] mImageCheckedResourceIds = new int[]{
            R.mipmap.ico_share,
            R.mipmap.home_ico_comment_normal,
            R.mipmap.ico_zan_on,
            R.mipmap.home_ico_more,
    };// 图片 ids 选中状态

    public TBMainDynamicListItemForZeroImage(Context context) {
        super(context);
    }


    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_zero_image_for_tb_main;
    }

    @Override
    public boolean isForViewType(DynamicDetailBeanV2 item, int position) {
        return item.getFeed_mark() != null && (item.getImages() == null || item.getImages().isEmpty());
    }

    @Override
    public void convert(ViewHolder holder, DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {

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
            dynamicListMenuView.setItemTextAndStatus(
                    dynamicBean.getFeed_digg_count() == 0 ? "" : ConvertUtils.numberConvert(dynamicBean
                            .getFeed_digg_count())
                    , dynamicBean.isHas_digg()
                    , 2
            );
            // 分享数量
            dynamicListMenuView.setItemTextAndStatus(
                    dynamicBean.getShare_count() == 0 ? "" : ConvertUtils.numberConvert(dynamicBean.getShare_count()),
                    false
                    , 0
            );
            // 评论数量
            dynamicListMenuView.setItemTextAndStatus(
                    dynamicBean.getFeed_comment_count() == 0 ? "" : ConvertUtils.numberConvert(dynamicBean.getFeed_comment_count())
                    , false
                    , 1
            );
            // 控制更多按钮的显示隐藏
            dynamicListMenuView.setItemPositionVisiable(0, getVisibleOne());
            dynamicListMenuView.setItemPositionVisiable(1, getVisibleTwo());
            dynamicListMenuView.setItemPositionVisiable(2, getVisibleThree());
            dynamicListMenuView.setItemPositionVisiable(3, getVisibleFour());
            // 设置工具栏的点击事件
            dynamicListMenuView.setItemOnClick((parent, v, menuPostion) -> {
                if (mOnMenuItemClickLisitener != null) {
                    mOnMenuItemClickLisitener.onMenuItemClick(holder.getConvertView(), position, menuPostion);
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


        holder.setText(R.id.tv_time, TimeUtils.getYeayMonthDay(TimeUtils.utc2LocalLong(dynamicBean.getCreated_at())));
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
    protected int getVisibleFour() {
        return View.GONE;
    }
}
