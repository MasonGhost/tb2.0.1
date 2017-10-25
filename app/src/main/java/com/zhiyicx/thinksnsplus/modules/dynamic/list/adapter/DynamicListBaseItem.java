package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkMetadata;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.baseproject.widget.textview.CenterImageSpan;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.TimeUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

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
    protected static final int DEFALT_IMAGE_HEIGHT = 300;
    protected final int mWidthPixels; // 屏幕宽度
    protected final int mHightPixels; // 屏幕高度
    private final int mMargin; // 图片容器的边距
    protected final int mDiverwith; // 分割先的宽高
    protected final int mImageContainerWith; // 图片容器最大宽度
    protected final int mImageMaxHeight; // 单张图片最大高度
    protected ImageLoader mImageLoader;
    protected Context mContext;
    protected AuthBean mAuthBean;

    protected boolean showToolMenu = true;// 是否显示工具栏:默认显示
    protected boolean showCommentList = true;// 是否显示评论内容:默认显示
    protected boolean showReSendBtn = true;// 是否显示重发按钮

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

    private int mTitleMaxShowNum;
    private int mContentMaxShowNum;


    public DynamicListBaseItem(Context context) {
        mAuthBean = AppApplication.getmCurrentLoginAuth();
        mContext = context;
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mTitleMaxShowNum = mContext.getResources().getInteger(R.integer
                .dynamic_list_title_max_show_size);
        mContentMaxShowNum = mContext.getResources().getInteger(R.integer
                .dynamic_list_content_max_show_size);
        mWidthPixels = DeviceUtils.getScreenWidth(context);
        mHightPixels = DeviceUtils.getScreenHeight(context);
        mMargin = 2 * context.getResources().getDimensionPixelSize(R.dimen
                .dynamic_list_image_marginright);
        mDiverwith = context.getResources().getDimensionPixelSize(R.dimen.spacing_small);
        mImageContainerWith = mWidthPixels - mMargin;
        mImageMaxHeight = mImageContainerWith * 4 / 3; // 最大高度是最大宽度的4/3 保持 宽高比 3：4
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
            if (holder.getView(R.id.iv_headpic).getVisibility() == View.VISIBLE) {
                ImageUtils.loadCircleUserHeadPic(dynamicBean.getUserInfoBean(), holder.getView(R.id.iv_headpic));
            }

            holder.setText(R.id.tv_name, dynamicBean.getUserInfoBean().getName());
            holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(dynamicBean
                    .getCreated_at()));
            holder.setVisible(R.id.tv_title, View.GONE);

            String content = dynamicBean.getFeed_content();
            TextView contentView = holder.getView(R.id.tv_content);

            // 置顶标识 ,防止没有置顶布局错误
            try {
                TextView topFlagView = holder.getView(R.id.tv_top_flag);// 待审核 也隐藏
                topFlagView.setVisibility(dynamicBean.getTop() == DynamicDetailBeanV2.TOP_SUCCESS ?
                        View.VISIBLE : View.GONE);
                topFlagView.setText(mContext.getString(dynamicBean.getTop() ==
                        DynamicDetailBeanV2.TOP_REVIEW ?
                        R.string.review_ing : R.string.dynamic_top_flag));
            } catch (Exception e) {
            }

            contentView.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(content)) {

                content = content.replaceAll(MarkdownConfig.NETSITE_FORMAT, MarkdownConfig.LINK_EMOJI + Link.DEFAULT_NET_SITE);


                if (content.length() > mContentMaxShowNum) {
                    content = content.substring(0, mContentMaxShowNum) + "...";
                }

                boolean canLookWords = dynamicBean.getPaid_node() == null || dynamicBean
                        .getPaid_node().isPaid();

                int contentLenght = content.length();

                if (!canLookWords) {
                    content += mContext.getString(R.string.words_holder);
                }

                if (canLookWords) {
                    TextViewUtils.newInstance(contentView, content)
                            .spanTextColor(SkinUtils.getColor(R
                                    .color.normal_for_assist_text))
                            .position(contentLenght, content.length())
                            .dataPosition(holder.getAdapterPosition())
                            .maxLines(contentView.getResources().getInteger(R.integer
                                    .dynamic_list_content_show_lines))
                            .onSpanTextClickListener(mOnSpanTextClickListener)
                            .disPlayText(true)
                            .build();
                } else {
                    int test_position = holder.getAdapterPosition();
                    TextViewUtils.newInstance(contentView, content)
                            .spanTextColor(SkinUtils.getColor(R
                                    .color.normal_for_assist_text))
                            .position(contentLenght, content.length())
                            .dataPosition(test_position)
                            .maxLines(contentView.getResources().getInteger(R.integer
                                    .dynamic_list_content_show_lines))
                            .onSpanTextClickListener(mOnSpanTextClickListener)
                            .note(dynamicBean.getPaid_node().getNode())
                            .amount(dynamicBean.getPaid_node().getAmount())
                            .disPlayText(false)
                            .build();
                }
                Observable.timer(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> ConvertUtils.stringLinkConvert(contentView, setLiknks(dynamicBean, contentView.getText().toString()), false));

                contentView.setVisibility(View.VISIBLE);
            }

            setUserInfoClick(holder.getView(R.id.iv_headpic), dynamicBean);
            setUserInfoClick(holder.getView(R.id.tv_name), dynamicBean);

            holder.setVisible(R.id.dlmv_menu, showToolMenu ? View.VISIBLE : View.GONE);
            // 分割线跟随工具栏显示隐藏
            holder.setVisible(R.id.v_line, showToolMenu ? View.VISIBLE : View.GONE);
            if (showToolMenu && dynamicBean.getUser_id() > 0) {// user_id = -1 广告
                // 显示工具栏
                DynamicListMenuView dynamicListMenuView = holder.getView(R.id.dlmv_menu);
                dynamicListMenuView.setImageNormalResourceIds(getToolImages());
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                        .getFeed_digg_count()), dynamicBean.isHas_digg(), 0);
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                        .getFeed_comment_count()), false, 1);
                dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(dynamicBean
                                .getFeed_view_count() == 0 ? 1 : dynamicBean.getFeed_view_count()),
                        false, 2);// 浏览量没有 0
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
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
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

    private void setUserInfoClick(View view, final DynamicDetailBeanV2 dynamicBean) {
        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
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
        int propPart = getProportion(view, dynamicBean, positon, part);
        int w, h;
        w = h = getCurrenItemWith(part);


        if (dynamicBean.getImages() != null && dynamicBean.getImages().size() > 0) {
            DynamicDetailBeanV2.ImagesBean imageBean = dynamicBean.getImages().get(positon);
            view.showLongImageTag(isLongImage(imageBean.getHeight(), imageBean.getWidth())); // 是否是长图

            if (TextUtils.isEmpty(imageBean.getImgUrl())) {
                Boolean canLook = !(imageBean.isPaid() != null && !imageBean.isPaid() &&
                        imageBean.getType().equals(Toll.LOOK_TOLL_TYPE));
                Glide.with(view.getContext())
                        .load(ImageUtils.imagePathConvertV2(canLook, imageBean.getFile(), w, h,
                                propPart, AppApplication.getTOKEN()))
//                        .override(w, h)
//                        .placeholder(canLook ? R.drawable.shape_default_image : R.mipmap.pic_locked)
                        .placeholder(R.drawable.shape_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.shape_default_image)
                        .into(view);
                LogUtils.i("dynamic item image" + ImageUtils.imagePathConvertV2(canLook, imageBean.getFile(), w, h,
                        propPart, AppApplication.getTOKEN()));

            } else {
                BitmapFactory.Options option = DrawableProvider.getPicsWHByFile(imageBean.getImgUrl());
                view.showLongImageTag(isLongImage(option.outHeight, option.outWidth)); // 是否是长图

                Glide.with(view.getContext())
                        .load(imageBean.getImgUrl())
                        .override(w, h)
                        .placeholder(R.drawable.shape_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.shape_default_image)
                        .into(view);
            }
        }

        if (dynamicBean.getImages() != null) {
            dynamicBean.getImages().get(positon).setPropPart(propPart);
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
     * 计算压缩比例
     *
     * @param view
     * @param dynamicBean
     * @param positon
     * @param part        比例，总大小的份数  @return
     */
    protected int getProportion(ImageView view, DynamicDetailBeanV2 dynamicBean, int positon, int part) {
        /**
         * 一张图时候，需要对宽高做限制
         */
        int with;
        int proportion; // 压缩比例
        int currentWith = getCurrenItemWith(part);
        DynamicDetailBeanV2.ImagesBean imageBean = dynamicBean.getImages().get(positon);
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

