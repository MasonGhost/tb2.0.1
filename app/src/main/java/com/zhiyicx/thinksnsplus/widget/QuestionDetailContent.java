package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;

/**
 * @author Catherine
 * @describe 问题的正文 如果有图的情况下，显示一张图和一排正文 展开全部；没图的情况下 显示三排内容，展开全部。 展开之后，正常图文混排，不可收起。
 * @date 2017/8/16
 * @contact email:648129313@qq.com
 */

public class QuestionDetailContent extends FrameLayout {

    private Context mContext;

    private LinearLayout mLlContentPreview;
    private ImageView mItemInfoImage;
    private ExpandableTextView mTvQuestionContent;
    private MarkdownView mMdvQuestionContent;
    private ArrayList<AnimationRectBean> animationRectBeanArrayList;

    public QuestionDetailContent(@NonNull Context context) {
        super(context);
        init(context);
    }

    public QuestionDetailContent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QuestionDetailContent(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        animationRectBeanArrayList = new ArrayList<>();
        LayoutInflater.from(context).inflate(R.layout.view_question_detail_content, this);
        mLlContentPreview = (LinearLayout) findViewById(R.id.ll_content_preview);
        mItemInfoImage = (ImageView) findViewById(R.id.item_info_imag);
        mTvQuestionContent = (ExpandableTextView) findViewById(R.id.tv_question_content);
        mMdvQuestionContent = (MarkdownView) findViewById(R.id.mdv_question_content);
    }

    /**
     * 设置正文的内容
     *
     * @param questionDetail bean
     */
    public void setQuestionDetail(QAListInfoBean questionDetail) {
        String content = questionDetail.getBody();
        String preContent = ""; // 预览的文字
        List<ImageBean> list = new ArrayList<>();
        while (RegexUtils.getImageIdFromMarkDown(MarkdownConfig.IMAGE_FORMAT, content) != -1) {
            // 取出id
            int id = RegexUtils.getImageIdFromMarkDown(MarkdownConfig.IMAGE_FORMAT, content);
            // 替换图片地址
            String url = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/" + id + "?q=80";
            String format = String.format(MarkdownConfig.IMAGE_RESULT, url);
            content = RegexUtils.getReplaceAll(content, MarkdownConfig.IMAGE_FORMAT, format);
            // 处理图片列表
            ImageBean imageBean = new ImageBean();
            imageBean.setStorage_id(id);
            imageBean.setImgUrl(url);
            list.add(imageBean);
            AnimationRectBean rect = AnimationRectBean.buildFromImageView(mItemInfoImage);// 动画矩形
            animationRectBeanArrayList.add(rect);
        }
        if (list.size() > 0) {
            dealContent(content, list);
            // 如果有图片 那么显示封面
            mItemInfoImage.setVisibility(VISIBLE);
            int w = DeviceUtils.getScreenWidth(mContext);
            int h = mContext.getResources().getDimensionPixelOffset(R.dimen.qa_info_iamge_height);
            String url = ImageUtils.imagePathConvertV2(list.get(0).getStorage_id(), w, h, ImageZipConfig.IMAGE_80_ZIP);
            Glide.with(mContext).load(url)
                    .asBitmap()
                    .override(w, h)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mItemInfoImage.setImageBitmap(resource);
                        }
                    });
            mTvQuestionContent.setMaxLinesOnShrink(1);
            mTvQuestionContent.setText(preContent);
            mTvQuestionContent.setExpandListener(new ExpandableTextView.OnExpandListener() {
                @Override
                public void onExpand(ExpandableTextView view) {
                    // 展开后 隐藏内容，显示图文混排内容
                    mLlContentPreview.setVisibility(GONE);
                    mMdvQuestionContent.setVisibility(GONE);
                }

                @Override
                public void onShrink(ExpandableTextView view) {

                }
            });
        } else {
            mItemInfoImage.setVisibility(GONE);
            mTvQuestionContent.setText(content);
        }
    }

    /**
     * 处理图文混排
     * @param content 正文
     * @param list 图片列表
     */
    private void dealContent(String content, List<ImageBean> list){
        InternalStyleSheet css = new Github();
        css.addRule("body", "line-height: 1.6", "padding: 10px");
        mMdvQuestionContent.addStyleSheet(css);
        mMdvQuestionContent.loadMarkdown(content);
        mMdvQuestionContent.setOnElementListener(new MarkdownView.OnElementListener() {
            @Override
            public void onButtonTap(String s) {

            }

            @Override
            public void onCodeTap(String s, String s1) {

            }

            @Override
            public void onHeadingTap(int i, String s) {

            }

            @Override
            public void onImageTap(String s, int width, int height) {
                LogUtils.d("Cathy", "onImageTap // " + s);
                int position = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getImgUrl().equals(s)) {
                        position = i;
                    }
                }
                GalleryActivity.startToGallery(mContext, position, list, animationRectBeanArrayList);
            }

            @Override
            public void onLinkTap(String s, String s1) {

            }

            @Override
            public void onKeystrokeTap(String s) {

            }

            @Override
            public void onMarkTap(String s) {

            }
        });
    }
}
