package com.zhiyicx.thinksnsplus.modules.circle.detail.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkMetadata;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.comment.GroupDynamicListCommentView;
import com.zhiyicx.thinksnsplus.widget.comment.GroupDynamicNoPullRecycleView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2.DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE;

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

public class GroupDynamicListBaseItem implements ItemViewDelegate<GroupDynamicListBean> {
    protected final String TAG = this.getClass().getSimpleName();
    private static final int CURREN_CLOUMS = 0;
    protected static final int DEFALT_IMAGE_HEIGHT = 300;
    private final int mWidthPixels; // 屏幕宽度
    private final int mHightPixels;
    private final int mMargin; // 图片容器的边距
    protected final int mDiverwith; // 分割先的宽高
    protected final int mImageContainerWith; // 图片容器最大宽度
    protected final int mImageMaxHeight; // 单张图片最大高度
    protected ImageLoader mImageLoader;
    protected Context mContext;
    protected AuthBean mAuthBean;

    private boolean showToolMenu = true;// 是否显示工具栏:默认显示
    private boolean showCommentList = true;// 是否显示评论内容:默认显示
    private boolean showReSendBtn = true;// 是否显示重发按钮

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
    }

    protected OnImageClickListener mOnImageClickListener; // 图片点击监听

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    protected OnUserInfoClickListener mOnUserInfoClickListener; // 用户信息点击监听

    protected TextViewUtils.OnSpanTextClickListener mOnSpanTextClickListener;

    public void setOnMenuItemClickLisitener(OnMenuItemClickLisitener onMenuItemClickLisitener) {
        mOnMenuItemClickLisitener = onMenuItemClickLisitener;
    }

    protected OnMenuItemClickLisitener mOnMenuItemClickLisitener; // 工具栏被点击


    public void setOnReSendClickListener(OnReSendClickListener onReSendClickListener) {
        mOnReSendClickListener = onReSendClickListener;
    }

    protected OnReSendClickListener mOnReSendClickListener;

    public void setOnCommentClickListener(GroupDynamicListCommentView.OnCommentClickListener
                                                  onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    protected GroupDynamicListCommentView.OnCommentClickListener mOnCommentClickListener;

    protected GroupDynamicListCommentView.OnMoreCommentClickListener mOnMoreCommentClickListener;

    public void setOnCommentStateClickListener(GroupDynamicNoPullRecycleView
                                                       .OnCommentStateClickListener
                                                       onCommentStateClickListener) {
        mOnCommentStateClickListener = onCommentStateClickListener;
    }

    protected GroupDynamicNoPullRecycleView.OnCommentStateClickListener mOnCommentStateClickListener;

    public void setOnMoreCommentClickListener(GroupDynamicListCommentView.OnMoreCommentClickListener
                                                      onMoreCommentClickListener) {
        mOnMoreCommentClickListener = onMoreCommentClickListener;
    }

    private int mTitleMaxShowNum;
    private int mContentMaxShowNum;


    public GroupDynamicListBaseItem(Context context) {
        mAuthBean = AppApplication.getmCurrentLoginAuth();
        mContext = context;
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mTitleMaxShowNum = mContext.getResources().getInteger(R.integer
                .dynamic_list_title_max_show_size);
        mContentMaxShowNum = DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE;
        mWidthPixels = DeviceUtils.getScreenWidth(context);
        mHightPixels = DeviceUtils.getScreenHeight(context);
        mMargin = 2 * context.getResources().getDimensionPixelSize(R.dimen
                .dynamic_list_image_marginright);
        mDiverwith = context.getResources().getDimensionPixelSize(R.dimen.spacing_small);
        mImageContainerWith = mWidthPixels - mMargin;
        // 最大高度是最大宽度的4/3 保持 宽高比 3：4
        mImageMaxHeight = mImageContainerWith * 4 / 3;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_zero_image;
    }

    @Override
    public boolean isForViewType(GroupDynamicListBean item, int position) {
        // 当本地和服务器都没有图片的时候，使用
        boolean isForViewType =
                item.getId() != null && (item.getImages() != null && item.getImages().size
                        () == getImageCounts());
        return isForViewType;
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
    public void convert(ViewHolder holder, GroupDynamicListBean dynamicBean, GroupDynamicListBean
            lastT, final int position, int itemCounts) {

        try {

            ImageUtils.loadCircleUserHeadPic(dynamicBean.getUserInfoBean(), holder.getView(R.id.iv_headpic));

            holder.setText(R.id.tv_name, dynamicBean.getUserInfoBean().getName());
            holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(dynamicBean
                    .getCreated_at()));
///            holder.setText(R.id.tv_title,dynamicBean.getTitle());
            holder.setVisible(R.id.tv_title, View.GONE);

            String content = dynamicBean.getContent();
            TextView contentView = holder.getView(R.id.tv_content);

            try { // 置顶标识 ,防止没有置顶布局错误
                // 待审核 也隐藏
                TextView topFlagView = holder.getView(R.id.tv_top_flag);
                topFlagView.setVisibility(View.GONE);
            } catch (Exception e) {

            }

            if (TextUtils.isEmpty(content)) {
                contentView.setVisibility(View.GONE);
            } else {
                content = content.replaceAll(MarkdownConfig.NETSITE_FORMAT, MarkdownConfig.LINK_EMOJI + Link.DEFAULT_NET_SITE);
                contentView.setText(content);
                ConvertUtils.stringLinkConvert(contentView, setLiknks(dynamicBean, contentView.getText().toString()), false);
                contentView.setVisibility(View.VISIBLE);
            }
            setUserInfoClick(holder.getView(R.id.iv_headpic), dynamicBean);
            setUserInfoClick(holder.getView(R.id.tv_name), dynamicBean);

            holder.setVisible(R.id.dlmv_menu, showToolMenu ? View.VISIBLE : View.GONE);
            // 分割线跟随工具栏显示隐藏
            holder.setVisible(R.id.v_line, showToolMenu ? View.VISIBLE : View.GONE);
            if (showToolMenu) {
                // 显示工具栏
                DynamicListMenuView dynamicListMenuView = holder.getView(R.id.dlmv_menu);
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                        .getDiggs()), dynamicBean.getHas_like(), 0);
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                        .getComments_count()), false, 1);
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                                .getViews() == 0 ? 1 : dynamicBean.getViews()),
                        false, 2);// 浏览量没有 0
                // 控制更多按钮的显示隐藏
                dynamicListMenuView.setItemPositionVisiable(3, View.VISIBLE);
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
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> {
                            if (mOnReSendClickListener != null) {
                                mOnReSendClickListener.onReSendClick(position);
                            }
                        });
            }

            holder.setVisible(R.id.dcv_comment, View.GONE);
            if (showCommentList) {
                // 设置评论内容
                GroupDynamicListCommentView comment = holder.getView(R.id.group_comment);
                if (dynamicBean.getNew_comments() == null || dynamicBean.getNew_comments().isEmpty()) {
                    comment.setVisibility(View.GONE);
                } else {
                    comment.setVisibility(View.VISIBLE);
                }

                comment.setData(dynamicBean);
                comment.setOnCommentClickListener(mOnCommentClickListener);
                comment.setOnMoreCommentClickListener(mOnMoreCommentClickListener);
                comment.setOnCommentStateClickListener(mOnCommentStateClickListener);

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setUserInfoClick(View view, final GroupDynamicListBean dynamicBean) {
        RxView.clicks(view)
                // 两秒钟之内只取一个点击事件，防抖操作
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
    GroupDynamicListBean dynamicBean, final int positon, int part) {
        int propPart = getProportion(view, dynamicBean, part);
        int w, h;
        w = h = getCurrenItemWith(part);
        if (dynamicBean.getImages() != null && dynamicBean.getImages().size() > 0) {
            GroupDynamicListBean.ImagesBean imageBean = dynamicBean.getImages().get(positon);
            if (TextUtils.isEmpty(imageBean.getImgUrl())) {
                Boolean canLook = true;
                // 是否是长图
                view.showLongImageTag(isLongImage(imageBean.getHeight(), imageBean.getWidth()));
                Glide.with(mContext)
                        .load(ImageUtils.imagePathConvertV2(canLook, imageBean.getFile_id(), w, h,
                                propPart, AppApplication.getTOKEN()))
                        .override(w, h)
                        .placeholder(canLook ? R.drawable.shape_default_image : R.mipmap.pic_locked)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(canLook ? R.drawable.shape_default_image : R.mipmap.pic_locked)
                        .into(view);
            } else {
                // 是否是长图
                BitmapFactory.Options option = DrawableProvider.getPicsWHByFile(imageBean.getImgUrl());
                view.showLongImageTag(isLongImage(option.outHeight, option.outWidth));
                Glide.with(mContext)
                        .load(imageBean.getImgUrl())
                        .override(w, h)
                        .placeholder(R.drawable.shape_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.pic_locked)
                        .into(view);
            }
        }

        if (dynamicBean.getImages() != null) {
            dynamicBean.getImages().get(positon).setPropPart(propPart);
        }
        RxView.clicks(view)
                // 两秒钟之内只取一个点击事件，防抖操作
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener.onImageClick(holder, dynamicBean, positon);
                    }
                });

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
     * 计算压缩比例
     *
     * @param view
     * @param dynamicBean
     * @param part        比例，总大小的份数
     * @return
     */
    protected int getProportion(ImageView view, GroupDynamicListBean dynamicBean, int part) {
        /**
         * 一张图时候，需要对宽高做限制
         */
        int with;
        int proportion; // 压缩比例
        int currentWith = getCurrenItemWith(part);
        GroupDynamicListBean.ImagesBean imageBean = dynamicBean.getImages().get(0);
        if (imageBean.getSize() == null || imageBean.getSize().isEmpty()) {
            return 70;
        }
        with = imageBean.getWidth() > currentWith ? currentWith : imageBean.getWidth();
        proportion = ((with / imageBean.getWidth()) * 100);
        proportion = proportion > 100 ? 100 : proportion;
        return proportion;
    }

    /**
     * 获取当前item 的宽
     *
     * @param part
     * @return
     */
    protected int getCurrenItemWith(int part) {
        try {
            return (mImageContainerWith - (getCurrenCloums() - 1) * mDiverwith) / getCurrenCloums
                    () * part;
        } catch (Exception e) {
            LogUtils.d("获取当前 item 的宽 = 0");
        }
        return 0;
    }

    protected int getCurrenCloums() {
        return CURREN_CLOUMS;
    }


    /**
     * image interface
     */
    public interface OnImageClickListener {

        void onImageClick(ViewHolder holder, GroupDynamicListBean dynamicBean, int position);
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

    public GroupDynamicListBaseItem setShowToolMenu(boolean showToolMenu) {
        this.showToolMenu = showToolMenu;
        return this;
    }

    public GroupDynamicListBaseItem setShowCommentList(boolean showCommentList) {
        this.showCommentList = showCommentList;
        return this;
    }

    public GroupDynamicListBaseItem setShowReSendBtn(boolean showReSendBtn) {
        this.showReSendBtn = showReSendBtn;
        return this;
    }

    public void setOnSpanTextClickListener(TextViewUtils.OnSpanTextClickListener
                                                   onSpanTextClickListener) {
        mOnSpanTextClickListener = onSpanTextClickListener;
    }

    protected List<Link> setLiknks(final GroupDynamicListBean dynamicDetailBeanV2, String content) {
        List<Link> links = new ArrayList<>();
        if (content.contains(Link.DEFAULT_NET_SITE)) {
            Link commentNameLink = new Link(MarkdownConfig.LINK_EMOJI + Link.DEFAULT_NET_SITE)
                    .setTextColor(ContextCompat.getColor(mContext, R.color
                            .themeColor))
                    .setLinkMetadata(LinkMetadata.builder()
                            .putString(LinkMetadata.METADATA_KEY_COTENT, dynamicDetailBeanV2.getContent())
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

