package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
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

    public void setOnLikeClickListener(OnLikeClickListener onLikeClickListener) {
        mOnLikeClickListener = onLikeClickListener;
    }

    protected OnLikeClickListener mOnLikeClickListener; // 工具栏被点击


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
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_zero_image;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        // 当本地和服务器都没有图片的时候，使用
        return (item.getFeed().getStorage_task_ids() == null || item.getFeed().getStorage_task_ids().size() == getImageCounts())
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

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, final int position) {
        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .url(dynamicBean.getUserInfoBean() != null ? dynamicBean.getUserInfoBean().getUserIcon() : "")
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
                if (mOnLikeClickListener != null) {
                    mOnLikeClickListener.onLikeButtonClick(position);
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
     * @param positon     item position
     */
    protected void initImageView(ImageView view, final DynamicBean dynamicBean, final int positon) {
        String url;
        if (dynamicBean.getFeed().getStorage_task_ids() != null && dynamicBean.getFeed().getStorage_task_ids().size() > 0) {
            url = String.format(ApiConfig.IMAGE_PATH, dynamicBean.getFeed().getStorage_task_ids().get(positon).getStorage_id(), 20);
        } else {
            url = dynamicBean.getFeed().getLocalPhotos().get(positon);
        }
        System.out.println("url = " + url);
        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnImageClickListener != null) {
                            mOnImageClickListener.onImageClick(dynamicBean, positon);
                        }
                    }
                });
        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .url(url)
                .imagerView(view)
                .build());
    }

    /**
     * image interface
     */
    public interface OnImageClickListener {

        void onImageClick(DynamicBean dynamicBean, int position);
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
    public interface OnLikeClickListener {
        void onLikeButtonClick(int position);
    }

    /**
     * resend interface
     */
    public interface OnReSendClickListener {
        void onReSendClick(int position);
    }

}

