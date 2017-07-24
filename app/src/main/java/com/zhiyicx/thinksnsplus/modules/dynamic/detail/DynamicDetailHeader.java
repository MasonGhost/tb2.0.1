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

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
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

    private OnImageClickLisenter mOnImageClickLisenter;
    private DynamicDetailAdvertHeader mDynamicDetailAdvertHeader;
    private TextViewUtils.OnSpanTextClickListener mOnSpanTextClickListener;

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
        mDynamicDetailAdvertHeader.setOnItemClickListener((v, position1, url) -> ToastUtils.showToast(position1 + ""));
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
//            dealTollWords(dynamicBean, contentText);// 处理文字收费
            mContent.setVisibility(View.VISIBLE);
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
            setImageClickListener(photoList, dynamicBean);
        }
    }

    private void dealTollWords(DynamicDetailBeanV2 dynamicBean, String contentText) {
        if (contentText.length() == 50 && dynamicBean.getPaid_node() != null && !dynamicBean.getPaid_node().isPaid()) {
            contentText += mContext.getString(R.string.words_holder);
        }

        TextViewUtils textViewUtils = TextViewUtils.newInstance(mContent, contentText)
                .spanTextColor(SkinUtils.getColor(R
                        .color.normal_for_assist_text))
                .position(50, contentText.length())
                .maxLines(mContext.getResources().getInteger(R.integer.dynamic_list_content_show_lines))
                .onSpanTextClickListener(mOnSpanTextClickListener)
                .disPlayText(true);

        if (dynamicBean.getPaid_node() != null) {// 有文字收费
            textViewUtils.note(dynamicBean.getPaid_node().getNode())
                    .amount(dynamicBean.getPaid_node().getAmount())
                    .disPlayText(dynamicBean.getPaid_node().isPaid());
        }
        textViewUtils.build();
    }

    public Bitmap getSharBitmap() {
        return sharBitmap;
    }

    public void updateImage(DynamicDetailBeanV2 dynamicBean) {
        List<DynamicDetailBeanV2.ImagesBean> photoList = dynamicBean.getImages();
        mPhotoContainer.removeAllViews();
        for (int i = 0; i < photoList.size(); i++) {
            showContentImage(mContext, photoList, i, dynamicBean.getUser_id().intValue(), i == photoList.size() - 1, mPhotoContainer);
        }
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
        List<DynamicDigListBean> userInfoList = dynamicBean.getDigUserInfoList();
        dynamicHorizontalStackIconView.setDigUserHeadIcon(userInfoList);

        // 设置跳转到点赞列表
        dynamicHorizontalStackIconView.setDigContainerClickListener(digContainer -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DigListFragment.DIG_LIST_DATA, dynamicBean);
            Intent intent = new Intent(mDynamicDetailHeader.getContext(), DigListActivity
                    .class);
            intent.putExtras(bundle);
            mDynamicDetailHeader.getContext().startActivity(intent);
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

        // 如果有本地图片，优先显示本地图片
        int height = (imageBean.getHeight() * picWidth / imageBean.getWidth());
        // 提前设置图片控件的大小，使得占位图显示
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWidth, height);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(layoutParams);

        if (TextUtils.isEmpty(imageBean.getImgUrl())) {
            int part = (picWidth / imageBean.getWidth()) * 100;
            if (part > 100) {
                part = 100;
            }
            Boolean canLook = !(imageBean.isPaid() != null && !imageBean.isPaid() && imageBean.getType().equals(Toll.LOOK_TOLL_TYPE));
            if (!canLook) {
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
            imageView.setLayoutParams(layoutParams);
            DrawableRequestBuilder requestBuilder =
                    Glide.with(mContext)
                            .load(ImageUtils.imagePathConvertV2(canLook, imageBean.getFile(), picWidth,
                                    height, part, AppApplication.getTOKEN()))
                            .placeholder(R.drawable.shape_default_image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.pic_locked);
            if (!canLook) {// 切换展位图防止闪屏
                requestBuilder.placeholder(R.mipmap.pic_locked);
            }
            requestBuilder.into(imageView);
        } else {
            Glide.with(mContext)
                    .load(imageBean.getImgUrl())
                    .override(picWidth, height)
                    .placeholder(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.pic_locked)
                    .into(imageView);
        }
        photoContainer.addView(view);
    }

    /**
     * 设置图片点击事件
     */
    private void setImageClickListener(final List<DynamicDetailBeanV2.ImagesBean> photoList, final DynamicDetailBeanV2 dynamicBean) {
        final ArrayList<AnimationRectBean> animationRectBeanArrayList
                = new ArrayList<>();
        final List<ImageBean> imageBeans = new ArrayList<>();
        for (int i = 0; i < mPhotoContainer.getChildCount(); i++) {
            final View photoView = mPhotoContainer.getChildAt(i);
            int imagePosition = i;
            ImageView imageView = (ImageView) photoView.findViewById(R.id.dynamic_content_img);
            imageView.setOnClickListener(v -> {
                animationRectBeanArrayList.clear();
                DynamicDetailBeanV2.ImagesBean img = photoList.get(imagePosition);
                Boolean canLook = !(img.isPaid() != null && !img.isPaid() && img.getType().equals(Toll.LOOK_TOLL_TYPE));
                if (!canLook) {
                    mOnImageClickLisenter.onImageClick(imagePosition, img.getAmount(), photoList.get(imagePosition).getPaid_node());
                    return;
                }
                imageBeans.clear();
                for (int i1 = 0; i1 < mPhotoContainer.getChildCount(); i1++) {
                    View photoView1 = mPhotoContainer.getChildAt(i1);
                    ImageView imageView1 = (ImageView) photoView1.findViewById(R.id
                            .dynamic_content_img);
                    DynamicDetailBeanV2.ImagesBean task = photoList.get(i1);
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImgUrl(task.getImgUrl());// 本地地址，也许有
                    Toll toll = new Toll(); // 收费信息
                    toll.setPaid(task.isPaid());// 是否已經付費
                    toll.setToll_money((float) task.getAmount());// 付费金额
                    toll.setToll_type_string(task.getType());// 付费类型
                    toll.setPaid_node(task.getPaid_node());// 付费节点
                    imageBean.setToll(toll);
                    imageBean.setFeed_id(dynamicBean.getId());// 动态id
                    imageBean.setWidth(task.getWidth());// 图片宽高
                    imageBean.setHeight(task.getHeight());
                    imageBean.setStorage_id(task.getFile());// 图片附件id
                    imageBeans.add(imageBean);
                    AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView1);// 动画矩形
                    animationRectBeanArrayList.add(rect);
                }

                GalleryActivity.startToGallery(mContext, mPhotoContainer.indexOfChild
                        (photoView), imageBeans, animationRectBeanArrayList);
            });
        }
    }

    public OnImageClickLisenter getOnImageClickLisenter() {
        return mOnImageClickLisenter;
    }

    public void setOnImageClickLisenter(OnImageClickLisenter onImageClickLisenter) {
        mOnImageClickLisenter = onImageClickLisenter;
    }

    public interface OnImageClickLisenter {
        void onImageClick(int iamgePosition, double amount, int note);
    }

}
