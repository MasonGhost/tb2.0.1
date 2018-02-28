package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkMetadata;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicNoPullRecycleView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 动态列表适配器基类
 * requirement document :{@see https://github
 * .com/zhiyicx/thinksns-plus-document/blob/master/document/%E5%8A%A8%E6%80%81%E6%A8%A1%E5%9D%97.md}
 * design document  {@see https://github
 * .com/zhiyicx/thinksns-plus-document/blob/master/document/TS%2B%E8%A7%86%E8%A7%89%E8%A7%84%E8
 * %8C%83%202.0/TS%2B%E8%A7%86%E8%A7%89%E8%A7%84%E8%8C%83%202.0.pdf}
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListBaseItem implements ItemViewDelegate<DynamicDetailBeanV2> {
    protected final String TAG = this.getClass().getSimpleName();
    private static final int CURREN_CLOUMS = 0;
    public static final int DEFALT_IMAGE_HEIGHT = 300;
    protected final int mHightPixels; // 屏幕高度
    protected final int mDiverwith; // 分割先的宽高
    protected final int mImageContainerWith; // 图片容器最大宽度
    protected final int mImageMaxHeight; // 单张图片最大高度
    protected Context mContext;

    protected boolean showToolMenu = true;// 是否显示工具栏:默认显示
    protected boolean showCommentList = false;// 是否显示评论内容:默认显示
    protected boolean showReSendBtn = true;// 是否显示重发按钮

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
    }

    /**
     * 图片点击监听
     */
    protected OnImageClickListener mOnImageClickListener;

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    /**
     * 用户信息点击监听
     */
    protected OnUserInfoClickListener mOnUserInfoClickListener;

    protected TextViewUtils.OnSpanTextClickListener mOnSpanTextClickListener;

    public void setOnMenuItemClickLisitener(OnMenuItemClickLisitener onMenuItemClickLisitener) {
        mOnMenuItemClickLisitener = onMenuItemClickLisitener;
    }

    /**
     * 工具栏被点击
     */
    protected OnMenuItemClickLisitener mOnMenuItemClickLisitener;


    public void setOnReSendClickListener(OnReSendClickListener onReSendClickListener) {
        mOnReSendClickListener = onReSendClickListener;
    }

    protected OnReSendClickListener mOnReSendClickListener;

    public void setOnCommentClickListener(DynamicListCommentView.OnCommentClickListener
                                                  onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    protected DynamicListCommentView.OnCommentClickListener mOnCommentClickListener;

    protected DynamicListCommentView.OnMoreCommentClickListener mOnMoreCommentClickListener;

    public void setOnCommentStateClickListener(DynamicNoPullRecycleView.OnCommentStateClickListener<DynamicCommentBean>
                                                       onCommentStateClickListener) {
        mOnCommentStateClickListener = onCommentStateClickListener;
    }

    protected DynamicNoPullRecycleView.OnCommentStateClickListener<DynamicCommentBean> mOnCommentStateClickListener;

    public void setOnMoreCommentClickListener(DynamicListCommentView.OnMoreCommentClickListener
                                                      onMoreCommentClickListener) {
        mOnMoreCommentClickListener = onMoreCommentClickListener;
    }

    public DynamicListBaseItem(Context context) {
        mContext = context;
        mHightPixels = DeviceUtils.getScreenHeight(context);
        int margin = 2 * context.getResources().getDimensionPixelSize(R.dimen
                .dynamic_list_image_marginright);
        mDiverwith = context.getResources().getDimensionPixelSize(R.dimen.spacing_small);
        mImageContainerWith = DeviceUtils.getScreenWidth(context) - margin;
        // 最大高度是最大宽度的4/3 保持 宽高比 3：4
        mImageMaxHeight = mImageContainerWith * 4 / 3;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_zero_image;
    }

    @Override
    public boolean isForViewType(DynamicDetailBeanV2 item, int position) {
        // 当本地和服务器都没有图片的时候，使用
        return item.getFeed_mark() != null && (item.getImages() != null && item.getImages().size
                () == getImageCounts());
    }

    /**
     * 获取图片数量
     *
     * @return
     */
    protected int getImageCounts() {
        return CURREN_CLOUMS;
    }

    /**
     * @param holder
     * @param dynamicBean
     * @param lastT       android:descendantFocusability
     * @param position
     */
    @Override
    public void convert(ViewHolder holder, DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2
            lastT, final int position, int itemCounts) {
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
            TextView contentView = holder.getView(R.id.tv_content);

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

            String content = dynamicBean.getFriendlyContent();
            contentView.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(content)) {

                boolean canLookWords = dynamicBean.getPaid_node() == null || dynamicBean
                        .getPaid_node().isPaid();

                int startPosition = dynamicBean.getStartPosition();

                if (canLookWords) {
                    TextViewUtils.newInstance(contentView, content)
                            .spanTextColor(SkinUtils.getColor(R
                                    .color.normal_for_assist_text))
                            .position(startPosition, content.length())
                            .dataPosition(holder.getAdapterPosition())
                            .maxLines(contentView.getResources().getInteger(R.integer
                                    .dynamic_list_content_show_lines))
                            .onSpanTextClickListener(mOnSpanTextClickListener)
                            .onTextSpanComplete(() -> ConvertUtils.stringLinkConvert(contentView, setLiknks(dynamicBean, contentView.getText().toString()), false))
                            .disPlayText(true)
                            .build();
                } else {
                    TextViewUtils.newInstance(contentView, content)
                            .spanTextColor(SkinUtils.getColor(R
                                    .color.normal_for_assist_text))
                            .position(startPosition, content.length())
                            .dataPosition(holder.getAdapterPosition())
                            .maxLines(contentView.getResources().getInteger(R.integer
                                    .dynamic_list_content_show_lines))
                            .onSpanTextClickListener(mOnSpanTextClickListener)
                            .note(dynamicBean.getPaid_node().getNode())
                            .amount(dynamicBean.getPaid_node().getAmount())
                            .onTextSpanComplete(() -> ConvertUtils.stringLinkConvert(contentView, setLiknks(dynamicBean, contentView.getText().toString()), false))
                            .disPlayText(false)
                            .build();
                }
                contentView.setVisibility(View.VISIBLE);
            }

            setUserInfoClick(holder.getView(R.id.tv_name), dynamicBean);
            contentView.setOnClickListener(v -> holder.getConvertView().performClick());
            holder.setVisible(R.id.dlmv_menu, showToolMenu ? View.VISIBLE : View.GONE);
            // 分割线跟随工具栏显示隐藏
            holder.setVisible(R.id.v_line, showToolMenu ? View.VISIBLE : View.GONE);
            // user_id = -1 广告
            if (showToolMenu && dynamicBean.getUser_id() > 0) {
                // 显示工具栏
                DynamicListMenuView dynamicListMenuView = holder.getView(R.id.dlmv_menu);
                dynamicListMenuView.setImageNormalResourceIds(getToolImages());
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                        .getFeed_digg_count()), dynamicBean.isHas_digg(), 0);
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                        .getFeed_comment_count()), false, 1);
                // 浏览量没有 0
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                                .getFeed_view_count() == 0 ? 1 : dynamicBean.getFeed_view_count()),
                        false, 2);
                // 控制更多按钮的显示隐藏
                dynamicListMenuView.setItemPositionVisiable(0, getVisibleOne());
                dynamicListMenuView.setItemPositionVisiable(1, getVisibleTwo());
                dynamicListMenuView.setItemPositionVisiable(2, getVisibleThree());
                dynamicListMenuView.setItemPositionVisiable(3, getVisibleFour());
                // 设置工具栏的点击事件
                dynamicListMenuView.setItemOnClick((parent, v, menuPostion) -> {
                    if (mOnMenuItemClickLisitener != null) {
                        mOnMenuItemClickLisitener.onMenuItemClick(v, position, menuPostion);
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
    }

    protected void setUserInfoClick(View view, final DynamicDetailBeanV2 dynamicBean) {
        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mOnUserInfoClickListener != null) {
                        mOnUserInfoClickListener.onUserInfoClick(dynamicBean.getUserInfoBean());
                    }
                });
    }


    /**
     * 设置 imageview 点击事件，以及显示
     *
     * @param view        the target
     * @param dynamicBean item data
     * @param positon     image item position
     * @param part        this part percent of imageContainer
     */
    protected void initImageView(final ViewHolder holder, FilterImageView view, final
    DynamicDetailBeanV2 dynamicBean, final int positon, int part) {
        if (dynamicBean.getImages() != null && dynamicBean.getImages().size() > 0) {
            DynamicDetailBeanV2.ImagesBean imageBean = dynamicBean.getImages().get(positon);
            // 是否是长图
            view.showLongImageTag(imageBean.hasLongImage());

            if (TextUtils.isEmpty(imageBean.getImgUrl())) {
                Glide.with(view.getContext())
                        .load(imageBean.getGlideUrl())
                        .placeholder(R.drawable.shape_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.shape_default_image)
                        .into(view);
            } else {
                BitmapFactory.Options option = DrawableProvider.getPicsWHByFile(imageBean.getImgUrl());
                view.showLongImageTag(isLongImage(option.outHeight, option.outWidth));

                Glide.with(view.getContext())
                        .load(imageBean.getImgUrl())
                        .override(imageBean.getCurrentWith(), imageBean.getCurrentWith())
                        .placeholder(R.drawable.shape_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.shape_default_image)
                        .into(view);
            }
        }

        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener.onImageClick(holder, dynamicBean, positon);
                    }
                });

    }

    /**
     * 获取当前item 的宽
     *
     * @param part
     * @return
     */
    protected int getCurrenItemWith(int part) {
        try {
            return (mImageContainerWith - (getCurrenCloums() - 1) * mDiverwith) / getCurrenCloums() * part;
        } catch (Exception e) {
            LogUtils.d("获取当前 item 的宽 = 0");
        }
        return 0;
    }

    protected int getCurrenCloums() {
        return CURREN_CLOUMS;
    }

    /**
     * 是否是长图
     *
     * @param imageHeight 需要判断的图片的高
     * @param imageWith   需要判断的图片的宽
     * @return
     */
    public boolean isLongImage(int imageHeight, int imageWith) {
        float a = (float) imageHeight * mHightPixels / ((float) imageWith * mHightPixels);

        return a > 3 || a < .3f;
    }

    /**
     * image interface
     */
    public interface OnImageClickListener {

        void onImageClick(ViewHolder holder, DynamicDetailBeanV2 dynamicBean, int position);
    }

    /**
     * like interface
     */
    public interface OnMenuItemClickLisitener {
        void onMenuItemClick(View view, int dataPosition, int viewPosition);
    }

    /**
     * resend interface
     */
    public interface OnReSendClickListener {
        void onReSendClick(int position);
    }

    public DynamicListBaseItem setShowToolMenu(boolean showToolMenu) {
        this.showToolMenu = showToolMenu;
        return this;
    }

    public DynamicListBaseItem setShowCommentList(boolean showCommentList) {
        this.showCommentList = showCommentList;
        return this;
    }

    public DynamicListBaseItem setShowReSendBtn(boolean showReSendBtn) {
        this.showReSendBtn = showReSendBtn;
        return this;
    }

    public void setOnSpanTextClickListener(TextViewUtils.OnSpanTextClickListener
                                                   onSpanTextClickListener) {
        mOnSpanTextClickListener = onSpanTextClickListener;
    }

    protected int[] getToolImages() {
        return null;
    }

    protected int getVisibleOne() {
        return View.VISIBLE;
    }

    protected int getVisibleTwo() {
        return View.VISIBLE;
    }

    protected int getVisibleThree() {
        return View.VISIBLE;
    }

    protected int getVisibleFour() {
        return View.VISIBLE;
    }

    protected List<Link> setLiknks(final DynamicDetailBeanV2 dynamicDetailBeanV2, String content) {
        List<Link> links = new ArrayList<>();
        if (content.contains(Link.DEFAULT_NET_SITE)) {
            Link commentNameLink = new Link(MarkdownConfig.LINK_EMOJI + Link.DEFAULT_NET_SITE)
                    .setTextColor(ContextCompat.getColor(mContext, R.color
                            .themeColor))
                    .setLinkMetadata(LinkMetadata.builder()
                            .putString(LinkMetadata.METADATA_KEY_COTENT, dynamicDetailBeanV2.getFeed_content())
                            .putSerializableObj(LinkMetadata.METADATA_KEY_TYPE, LinkMetadata.SpanType.NET_SITE)
                            .build())
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(mContext, R.color
                            .general_for_hint))
                    .setHighlightAlpha(.8f)
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        LogUtils.d(clickedText);
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(clickedText);
                        intent.setData(content_url);
                        mContext.startActivity(intent);
                    })
                    .setOnLongClickListener((clickedText, linkMetadata) -> {

                    })
                    .setUnderlined(false);
            links.add(commentNameLink);
        }
        return links;
    }

}

