package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkMetadata;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list.GroupDigListActivity;
import com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list.GroupDigListFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe 圈子动态头部
 * @date 2017/7/19
 * @contact email:648129313@qq.com
 */

public class GroupDynamicDetailHeader {
    private LinearLayout mPhotoContainer;
    private TextView mContent;
    private TextView mTitle;
    private View mDynamicDetailHeader;
    private View mReward;
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

    public GroupDynamicDetailHeader(Context context, List<RealAdvertListBean> adverts) {
        this.mContext = context;
        mDynamicDetailHeader = LayoutInflater.from(context).inflate(R.layout
                .view_header_dynamic_detial, null);
        mReward = mDynamicDetailHeader.findViewById(R.id.v_reward);
        mReward.setVisibility(View.GONE);
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

    private void initAdvert(Context context, List<RealAdvertListBean> adverts) {
        mDynamicDetailAdvertHeader = new DynamicDetailAdvertHeader(context, mDynamicDetailHeader
                .findViewById(R.id.ll_advert));
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || adverts.isEmpty()) {
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
    public void setDynamicDetial(GroupDynamicListBean dynamicBean) {
        String titleText = dynamicBean.getTitle();
        if (TextUtils.isEmpty(titleText)) {
            mTitle.setVisibility(View.GONE);
        } else {
            mTitle.setText(titleText);
        }
        String contentText = dynamicBean.getContent();
        if (TextUtils.isEmpty(contentText)) {
            mContent.setVisibility(View.GONE);
        } else {
//            dealTollWords(dynamicBean, contentText);// 处理文字收费
            mContent.setVisibility(View.VISIBLE);
            dealLinkWords(dynamicBean, contentText);
        }

        final Context context = mTitle.getContext();
        // 设置图片
        List<GroupDynamicListBean.ImagesBean> photoList = dynamicBean.getImages();
        if (photoList == null || photoList.isEmpty()) {
            mPhotoContainer.setVisibility(View.GONE);
            sharBitmap = ConvertUtils.drawBg4Bitmap(0xffffff, BitmapFactory.decodeResource(context
                    .getResources(), R.mipmap.icon).copy(Bitmap.Config.RGB_565, true));
        } else {
            mPhotoContainer.setVisibility(View.VISIBLE);
            mPhotoContainer.removeAllViews();
            for (int i = 0; i < photoList.size(); i++) {
                showContentImage(context, photoList, i, dynamicBean.getUser_id().intValue(), i == photoList.size() - 1, mPhotoContainer);
            }
            FilterImageView imageView = (FilterImageView) mPhotoContainer.getChildAt(0).findViewById(R.id.dynamic_content_img);
            sharBitmap = ConvertUtils.drawable2BitmapWithWhiteBg(mContext, imageView
                    .getDrawable(), R.mipmap.icon);
            setImageClickListener(photoList, dynamicBean);
        }
    }

    private void dealLinkWords(GroupDynamicListBean dynamicBean, String content) {
        content = content.replaceAll(MarkdownConfig.NETSITE_FORMAT, MarkdownConfig.LINK_EMOJI + Link.DEFAULT_NET_SITE);
        mContent.setText(content);
        ConvertUtils.stringLinkConvert(mContent, setLiknks(dynamicBean, mContent.getText().toString()), false);
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

    public void updateImage(GroupDynamicListBean dynamicBean) {
        List<GroupDynamicListBean.ImagesBean> photoList = dynamicBean.getImages();
        mPhotoContainer.removeAllViews();
        for (int i = 0; i < photoList.size(); i++) {
            showContentImage(mContext, photoList, i, dynamicBean.getUser_id().intValue(), i == photoList.size() - 1, mPhotoContainer);
        }
        FilterImageView imageView = (FilterImageView) mPhotoContainer.getChildAt(0).findViewById(R.id.dynamic_content_img);
        sharBitmap = ConvertUtils.drawable2BitmapWithWhiteBg(mContext, imageView
                .getDrawable(), R.mipmap.icon);
        setImageClickListener(photoList, dynamicBean);
    }

    /**
     * 更新喜欢的人
     *
     * @param dynamicBean
     */
    public void updateHeaderViewData(final GroupDynamicListBean dynamicBean) {

        DynamicHorizontalStackIconView dynamicHorizontalStackIconView =
                (DynamicHorizontalStackIconView) mDynamicDetailHeader.findViewById(R.id
                        .detail_dig_view);

        dynamicHorizontalStackIconView.setDigCount(dynamicBean.getDiggs());
        dynamicHorizontalStackIconView.setPublishTime(dynamicBean.getCreated_at());
        dynamicHorizontalStackIconView.setViewerCount(dynamicBean.getViews());
        // 设置点赞头像
        List<DynamicDigListBean> imageBeanList = dynamicBean.getMGroupDynamicLikeListBeanList();

        dynamicHorizontalStackIconView.setDigUserHeadIcon(imageBeanList);

        // 设置跳转到点赞列表
        dynamicHorizontalStackIconView.setDigContainerClickListener(digContainer -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(GroupDigListFragment.GROUP_DIG_LIST_DATA, dynamicBean);
            Intent intent = new Intent(mDynamicDetailHeader.getContext(), GroupDigListActivity
                    .class);
            intent.putExtra(GroupDigListFragment.GROUP_DIG_LIST_DATA, bundle);
            mDynamicDetailHeader.getContext().startActivity(intent);
        });
        if (dynamicBean.getComments_count() <= 0) {
            fl_comment_count_container.setVisibility(View.GONE);
        } else {
            ((TextView) mDynamicDetailHeader.findViewById(R.id.tv_comment_count)).setText
                    (mDynamicDetailHeader.getResources().getString(R.string
                            .dynamic_comment_count, ConvertUtils.numberConvert(dynamicBean
                            .getComments_count())));
            fl_comment_count_container.setVisibility(View.VISIBLE);
        }
    }

    private void showContentImage(Context context, List<GroupDynamicListBean.ImagesBean> photoList, final int position, final int user_id,
                                  boolean lastImg, LinearLayout photoContainer) {
        GroupDynamicListBean.ImagesBean imageBean = photoList.get(position);
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
            Boolean canLook = true;
            if (!canLook) {
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
            imageView.setLayoutParams(layoutParams);
            DrawableRequestBuilder requestBuilder =
                    Glide.with(mContext)
                            .load(ImageUtils.imagePathConvertV2(canLook, imageBean.getFile_id(), picWidth,
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
    private void setImageClickListener(final List<GroupDynamicListBean.ImagesBean> photoList, final GroupDynamicListBean dynamicBean) {
        final ArrayList<AnimationRectBean> animationRectBeanArrayList
                = new ArrayList<>();
        final List<ImageBean> imageBeans = new ArrayList<>();
        for (int i = 0; i < mPhotoContainer.getChildCount(); i++) {
            final View photoView = mPhotoContainer.getChildAt(i);
            int imagePosition = i;
            ImageView imageView = (ImageView) photoView.findViewById(R.id.dynamic_content_img);
            imageView.setOnClickListener(v -> {
                animationRectBeanArrayList.clear();
                GroupDynamicListBean.ImagesBean img = photoList.get(imagePosition);
                Boolean canLook = true; /*!(img.isPaid() != null && !img.isPaid() && img.getType().equals(Toll.LOOK_TOLL_TYPE));
                if (!canLook) {
                    mOnImageClickLisenter.onImageClick(imagePosition, img.getAmount(), photoList.get(imagePosition).getPaid_node());
                    return;
                }*/
                imageBeans.clear();
                for (int i1 = 0; i1 < mPhotoContainer.getChildCount(); i1++) {
                    View photoView1 = mPhotoContainer.getChildAt(i1);
                    ImageView imageView1 = (ImageView) photoView1.findViewById(R.id
                            .dynamic_content_img);
                    GroupDynamicListBean.ImagesBean task = photoList.get(i1);
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImgUrl(task.getImgUrl());// 本地地址，也许有
                    Toll toll = new Toll(); // 收费信息
                    toll.setPaid(true);// 是否已經付費
                    toll.setToll_money(100);// 付费金额
                    toll.setToll_type_string("1");// 付费类型
                    toll.setPaid_node(1);// 付费节点
                    imageBean.setToll(toll);
                    imageBean.setFeed_id(dynamicBean.getId());// 动态id
                    imageBean.setWidth(task.getWidth());// 图片宽高
                    imageBean.setHeight(task.getHeight());
                    imageBean.setStorage_id(task.getFile_id());// 图片附件id
                    imageBeans.add(imageBean);
                    AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView1);// 动画矩形
                    animationRectBeanArrayList.add(rect);
                }

                GalleryActivity.startToGallery(mContext, mPhotoContainer.indexOfChild
                        (photoView), imageBeans, animationRectBeanArrayList);
            });
        }
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
