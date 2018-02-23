package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe 图片消息
 * @date 2018/1/8
 * @contact email:648129313@qq.com
 */

public class ChatRowPicture extends ChatBaseRow {
    private static final int DEFAULT_IMAGE_SIZE = 460;
    /**
     * 显示本地图片最大为屏幕 1/3
     */
    private int mMaxLocalImageWith;
    /**
     * 网络图片通过计算去掉周边数据获得 / 或者使用和本地图片一样的规则
     */
    private int mMaxNetImageWith;


    private AppCompatImageView mIvChatContent;

    public ChatRowPicture(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean chatUserInfoBean) {
        super(context, message, position, adapter, chatUserInfoBean);
        mMaxLocalImageWith = DeviceUtils.getScreenWidth(context)  / 3;
//        mMaxNetImageWith = DeviceUtils.getScreenWidth(context) - 3 * getResources().getDimensionPixelOffset(R.dimen.spacing_large) - getResources()
//                .getDimensionPixelOffset(R.dimen.headpic_for_list);
        mMaxNetImageWith = DeviceUtils.getScreenWidth(context) / 3;

    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.SEND ?
                R.layout.item_chat_list_send_picture : R.layout.item_chat_list_receive_picture, this);
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        mIvChatContent = (AppCompatImageView) findViewById(R.id.iv_chat_content);
    }

    @Override
    protected void onSetUpView() {
        super.onSetUpView();
        EMImageMessageBody imageMessageBody = (EMImageMessageBody) message.getBody();
        // 图片地址
        int width;
        int height;
        String url = TextUtils.isEmpty(imageMessageBody.getRemoteUrl()) ? imageMessageBody.getLocalUrl() : imageMessageBody.getRemoteUrl();
        if (TextUtils.isEmpty(imageMessageBody.getRemoteUrl())) {
            // 本地
            BitmapFactory.Options option = DrawableProvider.getPicsWHByFile(imageMessageBody.getLocalUrl());
            if (option.outWidth == 0) {
                width = DEFAULT_IMAGE_SIZE;
                height = DEFAULT_IMAGE_SIZE;
            } else {
                width = option.outWidth;
                height = option.outHeight;
                if (width > mMaxLocalImageWith) {
                    height = (int) (((float) mMaxLocalImageWith / width) * height);
                    width = mMaxLocalImageWith;
                }
            }

        } else {
            width = imageMessageBody.getWidth();
            height = imageMessageBody.getHeight();
            if (width > mMaxNetImageWith) {
                height = (int) (((float) mMaxNetImageWith / width) * height);
                width = mMaxNetImageWith;
            }

        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIvChatContent.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mIvChatContent.setLayoutParams(layoutParams);
        ImageUtils.loadImageDefault(mIvChatContent, url);

        int finalWidth = width;
        int finalHeight = height;
        RxView.clicks(mIvChatContent)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    // 跳转查看大图
                    List<ImageBean> imageBeanList = new ArrayList<>();
                    ArrayList<AnimationRectBean> animationRectBeanArrayList
                            = new ArrayList<>();
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImgUrl(url);
                    imageBean.setStorage_id(0);
                    imageBean.setWidth(finalWidth);
                    imageBean.setHeight(finalHeight);
                    imageBeanList.add(imageBean);
                    AnimationRectBean rect = AnimationRectBean.buildFromImageView(mIvChatContent);
                    animationRectBeanArrayList.add(rect);
                    GalleryActivity.startToGallery(getContext(), 0, imageBeanList,
                            animationRectBeanArrayList);
                });
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        super.onViewUpdate(msg);
        switch (msg.status()) {
            case CREATE:
                break;
            case SUCCESS:
                onSetUpView();
                break;
            case FAIL:
                break;
            case INPROGRESS:
                break;
            default:
        }
    }

    public AppCompatImageView getIvChatContent() {
        return mIvChatContent;
    }
}
