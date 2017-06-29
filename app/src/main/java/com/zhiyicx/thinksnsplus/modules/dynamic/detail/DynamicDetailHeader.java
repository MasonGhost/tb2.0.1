package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListFragment;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe 动态详情头部信息
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class DynamicDetailHeader {

    private LinearLayout mPhotoContainer;
    private TextView mContent;
    private TextView mTitle;
    private View mDynamicDetailHeader;
    private FrameLayout fl_comment_count_container;
    private Context mContext;
    private int screenWidth;
    private int picWidth;
    private Bitmap sharBitmap;

    private DynamicDetailAdvertHeader mDynamicDetailAdvertHeader;

    public View getDynamicDetailHeader() {
        return mDynamicDetailHeader;
    }

    public DynamicDetailHeader(Context context, List<SystemConfigBean.Advert> adverts) {
        this.mContext = context;
        mDynamicDetailHeader = LayoutInflater.from(context).inflate(R.layout
                .view_header_dynamic_detial, null);
        mDynamicDetailHeader.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mTitle = (TextView) mDynamicDetailHeader.findViewById(R.id.tv_dynamic_title);
        mContent = (TextView) mDynamicDetailHeader.findViewById(R.id.tv_dynamic_content);
        initAdvert(context, adverts);
        fl_comment_count_container = (FrameLayout) mDynamicDetailHeader.findViewById(R.id
                .fl_comment_count_container);
        mPhotoContainer = (LinearLayout) mDynamicDetailHeader.findViewById(R.id
                .ll_dynamic_photos_container);
        screenWidth = UIUtils.getWindowWidth(context);
        picWidth = UIUtils.getWindowWidth(context) - context.getResources().getDimensionPixelSize
                (R.dimen.spacing_normal) * 2;
    }

    private void initAdvert(Context context, List<SystemConfigBean.Advert> adverts) {
        mDynamicDetailAdvertHeader = new DynamicDetailAdvertHeader(context, mDynamicDetailHeader
                .findViewById(R.id.ll_advert));
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT) {
            mDynamicDetailAdvertHeader.hideAdvert();
            return;
        }
        mDynamicDetailAdvertHeader.setTitle("广告");
        mDynamicDetailAdvertHeader.setAdverts(adverts);
        mDynamicDetailAdvertHeader.setOnItemClickListener(new DynamicDetailAdvertHeader
                .OnItemClickListener() {
            @Override
            public void onItemClik(View v, int position, String url) {
                ToastUtils.showToast(position + "");
            }
        });
    }

    /**
     * 设置头部动态信息
     *
     * @param dynamicBean
     */
    public void setDynamicDetial(DynamicDetailBeanV2 dynamicBean) {
        String titleText = "";
        if (TextUtils.isEmpty(titleText)) {
            mTitle.setVisibility(View.GONE);
        } else {
            mTitle.setText(titleText);
        }
        String contentText = dynamicBean.getFeed_content();
        if (TextUtils.isEmpty(contentText)) {
            mContent.setVisibility(View.GONE);
        } else {
            mContent.setText(contentText);
        }

        final Context context = mTitle.getContext();
        // 设置图片
        List<DynamicDetailBeanV2.ImagesBean> photoList = dynamicBean.getImages();
        if (photoList == null || photoList.isEmpty()) {
            mPhotoContainer.setVisibility(View.GONE);
            sharBitmap = ConvertUtils.drawBg4Bitmap(0xffffff, BitmapFactory.decodeResource(context
                    .getResources(), R.mipmap.icon_256).copy(Bitmap.Config.RGB_565, true));
        } else {
            mPhotoContainer.setVisibility(View.VISIBLE);
            for (int i = 0; i < photoList.size(); i++) {
                showContentImage(context, photoList, i, dynamicBean.getUser_id().intValue(), i == photoList.size() - 1, mPhotoContainer);
            }
            FilterImageView imageView = (FilterImageView) mPhotoContainer.getChildAt(0).findViewById(R.id.dynamic_content_img);
            sharBitmap = ConvertUtils.drawable2BitmapWithWhiteBg(mContext, imageView
                    .getDrawable(), R.mipmap.icon_256);
            setImageClickListener(photoList);
        }
    }

    public Bitmap getSharBitmap() {
        return sharBitmap;
    }

    /**
     * 更新喜欢的人
     *
     * @param dynamicBean
     */
    public void updateHeaderViewData(final DynamicDetailBeanV2 dynamicBean) {

        DynamicHorizontalStackIconView dynamicHorizontalStackIconView =
                (DynamicHorizontalStackIconView) mDynamicDetailHeader.findViewById(R.id
                        .detail_dig_view);

        dynamicHorizontalStackIconView.setDigCount(dynamicBean.getFeed_digg_count());
        dynamicHorizontalStackIconView.setPublishTime(dynamicBean.getCreated_at());
        dynamicHorizontalStackIconView.setViewerCount(dynamicBean.getFeed_view_count());
        // 设置点赞头像
        List<FollowFansBean> userInfoList = dynamicBean.getDigUserInfoList();
        List<ImageBean> imageBeanList = null;
        if (userInfoList != null && !userInfoList.isEmpty()) {
            imageBeanList = new ArrayList<>();
            for (int i = userInfoList.size() - 1; i >= 0; i--) {
                ImageBean imageBean = new ImageBean();
                imageBean.setStorage_id(TextUtils.isEmpty(userInfoList.get(i).getTargetUserInfo()
                        .getAvatar()) ? 0 : Integer.parseInt(userInfoList.get(i)
                        .getTargetUserInfo().getAvatar()));
                imageBeanList.add(imageBean);
            }
        }
        dynamicHorizontalStackIconView.setDigUserHeadIcon(imageBeanList);

        // 设置跳转到点赞列表
        dynamicHorizontalStackIconView.setDigContainerClickListener(new DynamicHorizontalStackIconView.DigContainerClickListener() {
            @Override
            public void digContainerClick(View digContainer) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(DigListFragment.DIG_LIST_DATA, dynamicBean);
                Intent intent = new Intent(mDynamicDetailHeader.getContext(), DigListActivity
                        .class);
                intent.putExtras(bundle);
                mDynamicDetailHeader.getContext().startActivity(intent);
            }
        });
        if (dynamicBean.getFeed_comment_count() <= 0) {
            fl_comment_count_container.setVisibility(View.GONE);
        } else {
            ((TextView) mDynamicDetailHeader.findViewById(R.id.tv_comment_count)).setText
                    (mDynamicDetailHeader.getResources().getString(R.string
                            .dynamic_comment_count, ConvertUtils.numberConvert(dynamicBean
                            .getFeed_comment_count())));
            fl_comment_count_container.setVisibility(View.VISIBLE);
        }
    }

    private void showContentImage(Context context, List<DynamicDetailBeanV2.ImagesBean> photoList, final int position, final int user_id,
                                  boolean lastImg, LinearLayout photoContainer) {
        DynamicDetailBeanV2.ImagesBean imageBean = photoList.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.view_dynamic_detail_photos, null);
        FilterImageView imageView = (FilterImageView) view.findViewById(R.id.dynamic_content_img);
        // 隐藏最后一张图的下间距
        if (lastImg) {
            view.findViewById(R.id.img_divider).setVisibility(View.GONE);
        }

        String imgUrl = "";
        // 如果有本地图片，优先显示本地图片
        int height = 0;// 图片需要显示的高度
        height = (imageBean.getHeight() * picWidth / imageBean.getWidth());
        if (TextUtils.isEmpty(imageBean.getImgUrl())) {
            int part = (picWidth / imageBean.getWidth() * 100);
            if (part > 100) {
                part = 100;
            }
            imgUrl = ImageUtils.imagePathConvertV2(imageBean.getFile(), picWidth, height, part);
        } else {
            imgUrl = imageBean.getImgUrl();
        }
        if (user_id != AppApplication.getmCurrentLoginAuth().getUser_id()) {
            if (imageBean.isPaid() != null && !imageBean.isPaid()) {// 没有付费的他人图片
                imgUrl = "1234";
            }
        }
        // 提前设置图片控件的大小，使得占位图显示
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWidth, height);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(layoutParams);

        AppApplication.AppComponentHolder.getAppComponent().imageLoader()
                .loadImage(context, GlideImageConfig.builder()
                        .placeholder(R.drawable.shape_default_image)
                        .errorPic(R.mipmap.pic_locked)
                        .url(imgUrl)
                        .imagerView(imageView)
                        .build()
                );
        photoContainer.addView(view);
    }

    /**
     * 设置图片点击事件
     */
    private void setImageClickListener(final List<DynamicDetailBeanV2.ImagesBean> photoList) {
        final ArrayList<AnimationRectBean> animationRectBeanArrayList
                = new ArrayList<>();
        final List<ImageBean> imageBeanList = new ArrayList<>();
        for (int i = 0; i < mPhotoContainer.getChildCount(); i++) {
            final View photoView = mPhotoContainer.getChildAt(i);
            ImageView imageView = (ImageView) photoView.findViewById(R.id.dynamic_content_img);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animationRectBeanArrayList.clear();
                    for (int i = 0; i < mPhotoContainer.getChildCount(); i++) {
                        View photoView = mPhotoContainer.getChildAt(i);
                        ImageView imageView = (ImageView) photoView.findViewById(R.id
                                .dynamic_content_img);
                        ImageBean imageBean = new ImageBean();
                        imageBean.setStorage_id(photoList.get(i).getFile());
                        imageBean.setImgUrl(photoList.get(i).getImgUrl());
                        imageBeanList.add(imageBean);
                        AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView);
                        animationRectBeanArrayList.add(rect);
                    }
                    GalleryActivity.startToGallery(mContext, mPhotoContainer.indexOfChild
                            (photoView), imageBeanList, animationRectBeanArrayList);
                }
            });
        }
    }
}
