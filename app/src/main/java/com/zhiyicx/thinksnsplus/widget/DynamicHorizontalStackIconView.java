package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBorderTransform;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe 动态详情页面，带有五个层叠的小图标，点赞数量，浏览数量，发布时间的控件
 * @date 2017/2/28
 * @contact email:450127106@qq.com
 */

public class DynamicHorizontalStackIconView extends FrameLayout {
    // 点赞数量
    private TextView digCount;
    private ImageView[] mImageViews;
    // 发布时间
    private TextView publishTime;
    // 浏览量
    private TextView viewerCount;
    // 点赞容器
    private LinearLayout digContainer;

    private DigContainerClickListener mDigContainerClickListener;

    private Context mContext;

    public DynamicHorizontalStackIconView(Context context) {
        super(context);
        init(context);
    }

    public DynamicHorizontalStackIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DynamicHorizontalStackIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_dynamic_horizontal_stack_icon, this);
        digCount = (TextView) findViewById(R.id.tv_dig_count);
        digContainer = (LinearLayout) findViewById(R.id.ll_dig_container);
        setDigRxClickListener();
        // 将图片存到图片数组中,倒序存放
        mImageViews = new ImageView[5];
        mImageViews[0] = (ImageView) findViewById(R.id.iv_dig_head1);
        mImageViews[1] = (ImageView) findViewById(R.id.iv_dig_head2);
        mImageViews[2] = (ImageView) findViewById(R.id.iv_dig_head3);
        mImageViews[3] = (ImageView) findViewById(R.id.iv_dig_head4);
        mImageViews[4] = (ImageView) findViewById(R.id.iv_dig_head5);
        publishTime = (TextView) findViewById(R.id.tv_dynamic_publish_time);
        viewerCount = (TextView) findViewById(R.id.tv_dynamic_viewer_count);
    }

    /**
     * 设置点赞的人的头像，最多五个
     */
    public void setDigUserHeadIcon(List<DynamicDigListBean> dynamicDigListBeanList) {
        if (dynamicDigListBeanList == null || dynamicDigListBeanList.size() == 0) {
            showNoDig();
        } else {
            for (int i = 0; i < mImageViews.length; i++) {
                // 需要显示的图片控件
                if (i < dynamicDigListBeanList.size()) {
                    DynamicDigListBean dynamicDigListBean = dynamicDigListBeanList.get(i);
                    int defaultAvatar = ImageUtils.getDefaultAvatar(dynamicDigListBean.getDiggUserInfo());

                    AppApplication.AppComponentHolder.getAppComponent().imageLoader()
                            .loadImage(mContext, GlideImageConfig.builder()
                                    .transformation(new GlideCircleBorderTransform(mContext, mContext.getResources().getDimensionPixelOffset(R
                                            .dimen.spacing_small_4dp), ContextCompat.getColor(mContext, R.color.white)))
                                    .placeholder(defaultAvatar)
                                    .errorPic(defaultAvatar)
                                    .imagerView(mImageViews[i])
                                    .url(ImageUtils.getUserAvatar(dynamicDigListBean.getDiggUserInfo()))
                                    .build()
                            );
                    mImageViews[i].setVisibility(VISIBLE);
                    digCount.setVisibility(VISIBLE);
                } else {// 没有显示的图片控件隐藏
                    mImageViews[i].setVisibility(GONE);
                }
            }
        }

    }

    /**
     * 设置点赞的人的头像，最多五个
     */
    public void setDigUserHeadIconInfo(List<InfoDigListBean> postDigListBeans) {
        if (postDigListBeans == null || postDigListBeans.size() == 0) {
            showNoDig();
        } else {
            for (int i = 0; i < mImageViews.length; i++) {
                // 需要显示的图片控件
                if (i < postDigListBeans.size()) {
                    InfoDigListBean dynamicDigListBean = postDigListBeans.get(i);
                    int defaultAvatar = ImageUtils.getDefaultAvatar(dynamicDigListBean.getDiggUserInfo());

                    AppApplication.AppComponentHolder.getAppComponent().imageLoader()
                            .loadImage(mContext, GlideImageConfig.builder()
                                    .transformation(new GlideCircleBorderTransform(mContext, mContext.getResources().getDimensionPixelSize(R.dimen
                                            .spacing_tiny), ContextCompat.getColor(mContext, R.color.white)))
                                    .placeholder(defaultAvatar)
                                    .errorPic(defaultAvatar)
                                    .imagerView(mImageViews[i])
                                    .url(ImageUtils.getUserAvatar(dynamicDigListBean.getDiggUserInfo()))
                                    .build()
                            );
                    mImageViews[i].setVisibility(VISIBLE);
                    digCount.setVisibility(VISIBLE);
                } else {// 没有显示的图片控件隐藏
                    mImageViews[i].setVisibility(GONE);
                }
            }
        }
    }

    /**
     * 设置点赞的人的头像，最多五个
     */
    public void setDigUserHeadIconPost(List<PostDigListBean> postDigListBeans) {
        if (postDigListBeans == null || postDigListBeans.size() == 0) {
            showNoDig();
        } else {
            for (int i = 0; i < mImageViews.length; i++) {
                // 需要显示的图片控件
                if (i < postDigListBeans.size()) {
                    PostDigListBean dynamicDigListBean = postDigListBeans.get(i);
                    int defaultAvatar = ImageUtils.getDefaultAvatar(dynamicDigListBean.getDiggUserInfo());

                    AppApplication.AppComponentHolder.getAppComponent().imageLoader()
                            .loadImage(mContext, GlideImageConfig.builder()
                                    .transformation(new GlideCircleBorderTransform(mContext, mContext.getResources().getDimensionPixelSize(R.dimen
                                            .spacing_tiny), ContextCompat.getColor(mContext, R.color.white)))
                                    .placeholder(defaultAvatar)
                                    .errorPic(defaultAvatar)
                                    .imagerView(mImageViews[i])
                                    .url(ImageUtils.getUserAvatar(dynamicDigListBean.getDiggUserInfo()))
                                    .build()
                            );
                    mImageViews[i].setVisibility(VISIBLE);
                    digCount.setVisibility(VISIBLE);
                } else {// 没有显示的图片控件隐藏
                    mImageViews[i].setVisibility(GONE);
                }
            }
        }

    }

    /**
     * 设置点赞的人的头像，最多五个
     */
    public void setDigUserHeadIconAnswer(List<AnswerDigListBean> answerDigListBeanList) {
        if (answerDigListBeanList == null || answerDigListBeanList.size() == 0) {
            showNoDig();
        } else {
            for (int i = 0; i < mImageViews.length; i++) {
                // 需要显示的图片控件
                if (i < answerDigListBeanList.size()) {
                    AnswerDigListBean dynamicDigListBean = answerDigListBeanList.get(i);
                    int defaultAvatar = ImageUtils.getDefaultAvatar(dynamicDigListBean.getDiggUserInfo());

                    AppApplication.AppComponentHolder.getAppComponent().imageLoader()
                            .loadImage(mContext, GlideImageConfig.builder()
                                    .transformation(new GlideCircleBorderTransform(mContext, mContext.getResources().getDimensionPixelSize(R.dimen
                                            .spacing_tiny), ContextCompat.getColor(mContext, R.color.white)))
                                    .placeholder(defaultAvatar)
                                    .errorPic(defaultAvatar)
                                    .imagerView(mImageViews[i])
                                    .url(ImageUtils.getUserAvatar(dynamicDigListBean.getDiggUserInfo()))
                                    .build()
                            );
                    mImageViews[i].setVisibility(VISIBLE);
                    digCount.setVisibility(VISIBLE);
                } else {// 没有显示的图片控件隐藏
                    mImageViews[i].setVisibility(GONE);
                }
            }
        }

    }

    /**
     * 设置点赞数量
     */
    public void setDigCount(int digCount) {
        this.digCount.setText(mContext.getString(R.string.dynamic_dig_count, ConvertUtils.numberConvert(digCount)));
    }

    /**
     * 设置浏览人数
     */
    public void setViewerCount(int viewerCount) {
        this.viewerCount.setText(mContext.getString(R.string.dynamic_viewer_count, ConvertUtils.numberConvert(viewerCount)));
    }

    /**
     * 设置发布时间
     */
    public void setPublishTime(String publishTime) {
        this.publishTime.setText(mContext.getString(R.string.dynamic_publish_time,
                TimeUtils.getTimeFriendlyForDetail(publishTime)));
    }

    /**
     * 没有人点赞
     */
    private void showNoDig() {
        this.digCount.setVisibility(GONE);
        for (ImageView imageView : mImageViews) {
            imageView.setVisibility(GONE);
        }
    }

    private void setDigRxClickListener() {
        RxView.clicks(digContainer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                //.compose(.<Void>bindToLifecycle()) 瞬时操作，不需要绑定生命周期
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mDigContainerClickListener != null) {
                            mDigContainerClickListener.digContainerClick(digContainer);
                        }
                    }
                });
    }

    public void setDigContainerClickListener(DigContainerClickListener digContainerClickListener) {
        mDigContainerClickListener = digContainerClickListener;
    }

    public interface DigContainerClickListener {
        void digContainerClick(View digContainer);
    }
}
