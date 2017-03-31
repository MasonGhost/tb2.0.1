package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBoundTransform;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;

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
    private ImageView iv_dig_head1, iv_dig_head2, iv_dig_head3, iv_dig_head4, iv_dig_head5;
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
        mImageViews[4] = iv_dig_head1 = (ImageView) findViewById(R.id.iv_dig_head1);
        mImageViews[3] = iv_dig_head2 = (ImageView) findViewById(R.id.iv_dig_head2);
        mImageViews[2] = iv_dig_head3 = (ImageView) findViewById(R.id.iv_dig_head3);
        mImageViews[1] = iv_dig_head4 = (ImageView) findViewById(R.id.iv_dig_head4);
        mImageViews[0] = iv_dig_head5 = (ImageView) findViewById(R.id.iv_dig_head5);
        publishTime = (TextView) findViewById(R.id.tv_dynamic_publish_time);
        viewerCount = (TextView) findViewById(R.id.tv_dynamic_viewer_count);
    }

    /**
     * 设置点赞的人的头像，最多五个
     */
    public void setDigUserHeadIcon(List<ImageBean> imageBeanList) {
        if (imageBeanList == null) {
            showNoDig();
        } else {
            for (int i = 0; i < mImageViews.length; i++) {
                // 需要显示的图片控件
                if (i < imageBeanList.size()) {
                    ImageBean imageBean = imageBeanList.get(i);
                    AppApplication.AppComponentHolder.getAppComponent().imageLoader()
                            .loadImage(mContext, GlideImageConfig.builder()
                                    .transformation(new GlideCircleBoundTransform(mContext))
                                    .placeholder(R.drawable.shape_default_image_circle_with_strike)
                                    .errorPic(R.drawable.shape_default_image_circle_with_strike)
                                    .imagerView(mImageViews[i])
                                    .url(ImageUtils.imagePathConvert(imageBean.getStorage_id()+"", ImageZipConfig.IMAGE_26_ZIP))
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
        this.digCount.setText(mContext.getString(R.string.dynamic_dig_count, digCount));
    }

    /**
     * 设置浏览人数
     */
    public void setViewerCount(int viewerCount) {
        this.viewerCount.setText(mContext.getString(R.string.dynamic_viewer_count, viewerCount));
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
