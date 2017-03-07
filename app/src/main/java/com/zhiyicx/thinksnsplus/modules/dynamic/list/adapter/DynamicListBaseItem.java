package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListComment;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean.STATUS_DIGG_FEED_CHECKED;

/**
 * @Describe 动态列表适配器基类
 * requirement document :{@see https://github.com/zhiyicx/thinksns-plus-document/blob/master/document/%E5%8A%A8%E6%80%81%E6%A8%A1%E5%9D%97.md}
 * design document  {@see https://github.com/zhiyicx/thinksns-plus-document/blob/master/document/TS%2B%E8%A7%86%E8%A7%89%E8%A7%84%E8%8C%83%202.0/TS%2B%E8%A7%86%E8%A7%89%E8%A7%84%E8%8C%83%202.0.pdf}
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListBaseItem implements ItemViewDelegate<DynamicBean> {
    private static final int CURREN_CLOUMS = 0;
    private final int mWidthPixels; // 屏幕宽度
    private final int mMargin; // 图片容器的边距
    protected final int mDiverwith; // 分割先的宽高
    protected final int mImageContainerWith; // 图片容器最大宽度
    protected final int mImageMaxHeight; // 单张图片最大高度
    protected int mImageCount = 0;
    protected ImageLoader mImageLoader;
    protected Context mContext;

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
    }

    protected OnImageClickListener mOnImageClickListener; // 图片点击监听

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    protected OnUserInfoClickListener mOnUserInfoClickListener; // 用户信息点击监听

    public void setOnMenuItemClickLisitener(OnMenuItemClickLisitener onMenuItemClickLisitener) {
        mOnMenuItemClickLisitener = onMenuItemClickLisitener;
    }

    protected OnMenuItemClickLisitener mOnMenuItemClickLisitener; // 工具栏被点击


    public void setOnReSendClickListener(OnReSendClickListener onReSendClickListener) {
        mOnReSendClickListener = onReSendClickListener;
    }

    protected OnReSendClickListener mOnReSendClickListener;
    private int mTitleMaxShowNum;
    private int mContentMaxShowNum;


    public DynamicListBaseItem(Context context) {
        mContext = context;
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mTitleMaxShowNum = mContext.getResources().getInteger(R.integer.dynamic_list_title_max_show_size);
        mContentMaxShowNum = mContext.getResources().getInteger(R.integer.dynamic_list_content_max_show_size);
        mWidthPixels = DeviceUtils.getScreenWidth(context);
        mMargin = 2 * context.getResources().getDimensionPixelSize(R.dimen.dynamic_list_image_marginright);
        mDiverwith = context.getResources().getDimensionPixelSize(R.dimen.spacing_small);
        mImageContainerWith = mWidthPixels - mMargin;
        mImageMaxHeight = mImageContainerWith * 4 / 3; // 最大高度是最大宽度的4/3 保持 宽高比 3：4
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_zero_image;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        // 当本地和服务器都没有图片的时候，使用
        return (item.getFeed().getStorages() == null || item.getFeed().getStorages().size() == getImageCounts())
                && (item.getFeed().getLocalPhotos() == null || item.getFeed().getLocalPhotos().size() == getImageCounts());
    }

    /**
     * 获取图片数量
     *
     * @return
     */
    protected int getImageCounts() {
        return mImageCount;
    }

    /**
     * @param holder
     * @param dynamicBean
     * @param lastT
     * @param position
     */
    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, final int position) {
        String userIconUrl = String.format(ApiConfig.IMAGE_PATH, dynamicBean.getUserInfoBean().getAvatar(), ImageZipConfig.IMAGE_38_ZIP);
        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .url(userIconUrl)
                .placeholder(R.drawable.shape_default_image_circle)
                .transformation(new GlideCircleTransform(mContext))
                .errorPic(R.drawable.shape_default_image_circle)
                .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                .build());
        holder.setText(R.id.tv_name, dynamicBean.getUserInfoBean().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(dynamicBean.getFeed().getCreated_at()));
        String title = dynamicBean.getFeed().getTitle();
        if (TextUtils.isEmpty(title)) { // 超过的数据用 ... 表示
            holder.setVisible(R.id.tv_title, View.GONE);
        } else {
            holder.setVisible(R.id.tv_title, View.VISIBLE);
            if (title.length() > mTitleMaxShowNum) {
                title = title.substring(0, mTitleMaxShowNum) + "...";
            }
            holder.setText(R.id.tv_title, title);
        }
        String content = dynamicBean.getFeed().getContent();
        if (content.length() > mContentMaxShowNum) {
            content = content.substring(0, mContentMaxShowNum) + "...";
        }
        holder.setText(R.id.tv_content, content);
        DynamicListMenuView dynamicListMenuView = holder.getView(R.id.dlmv_menu);
        dynamicListMenuView.setItemTextAndStatus(String.valueOf(dynamicBean.getTool().getFeed_digg_count()), dynamicBean.getTool().getIs_digg_feed() == STATUS_DIGG_FEED_CHECKED, 0);
        dynamicListMenuView.setItemTextAndStatus(String.valueOf(dynamicBean.getTool().getFeed_comment_count()), false, 1);
        dynamicListMenuView.setItemTextAndStatus(String.valueOf(dynamicBean.getTool().getFeed_view_count()), false, 2);

        dynamicListMenuView.setItemOnClick(new DynamicListMenuView.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View v, int menuPostion) {
                if (mOnMenuItemClickLisitener != null) {
                    mOnMenuItemClickLisitener.onMenuItemClick(v, position, menuPostion);
                }
            }
        });
        setUserInfoClick(holder.getView(R.id.iv_headpic), dynamicBean);
        setUserInfoClick(holder.getView(R.id.tv_name), dynamicBean);
        // 设置动态状态
        if (dynamicBean.getState() == DynamicBean.SEND_ERROR) {
            holder.setVisible(R.id.fl_tip, View.VISIBLE);
        } else {
            holder.setVisible(R.id.fl_tip, View.GONE);
        }
        RxView.clicks(holder.getView(R.id.fl_tip))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnReSendClickListener != null) {
                            mOnReSendClickListener.onReSendClick(position);
                        }
                    }
                });
        System.out.println("dynamicCommentBean = " + dynamicBean.getComments());
        DynamicListComment comment = holder.getView(R.id.fl_comment);
        comment.setData(dynamicBean.getComments());
    }

    private void setUserInfoClick(View view, final DynamicBean dynamicBean) {
        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnUserInfoClickListener != null) {
                            mOnUserInfoClickListener.onUserInfoClick(dynamicBean);
                        }
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
    protected void initImageView(final ViewHolder holder, ImageView view, final DynamicBean dynamicBean, final int positon, int part) {
        String url;
        if (dynamicBean.getFeed().getStorages() != null && dynamicBean.getFeed().getStorages().size() > 0) {
            url = String.format(ApiConfig.IMAGE_PATH, dynamicBean.getFeed().getStorages().get(positon).getStorage_id(), getProportion(view, dynamicBean, part));
        } else {
            url = dynamicBean.getFeed().getLocalPhotos().get(positon);
        }
        System.out.println("url = " + url);

        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .url(url)
                .imagerView(view)
                .build());
        if (dynamicBean.getFeed().getStorages() != null) {
            dynamicBean.getFeed().getStorages().get(positon).setPart(part);
        }
        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnImageClickListener != null) {
                            mOnImageClickListener.onImageClick(holder, dynamicBean, positon);
                        }
                    }
                });
    }


    /**
     * 计算压缩比例
     *
     * @param view
     * @param dynamicBean
     * @param part        比例，总大小的份数
     * @return
     */
    protected int getProportion(ImageView view, DynamicBean dynamicBean, int part) {
        /**
         * 一张图时候，需要对宽高做限制
         */
        int with;
        int height;
        int proportion; // 压缩比例
        int currentWith = getCurrenItemWith(part);
        if (dynamicBean.getFeed().getStorages() == null || dynamicBean.getFeed().getStorages().size() == 0) {// 本地图片
            BitmapFactory.Options option = DrawableProvider.getPicsWHByFile(dynamicBean.getFeed().getLocalPhotos().get(0));
            with = option.outWidth > currentWith ? currentWith : option.outWidth;
        } else {
            with = (int) dynamicBean.getFeed().getStorages().get(0).getWidth() > currentWith ? currentWith : (int) dynamicBean.getFeed().getStorages().get(0).getWidth();
        }
        height = with;
        proportion = (int) ((with / dynamicBean.getFeed().getStorages().get(0).getWidth()) * 100);
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
            return (mImageContainerWith - (getCurrenCloums() - 1) * mDiverwith) / getCurrenCloums() * part;
        } catch (Exception e) {
            e.printStackTrace();
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

        void onImageClick(ViewHolder holder, DynamicBean dynamicBean, int position);
    }

    /**
     * user info interface
     */
    public interface OnUserInfoClickListener {

        void onUserInfoClick(DynamicBean dynamicBean);
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

}

