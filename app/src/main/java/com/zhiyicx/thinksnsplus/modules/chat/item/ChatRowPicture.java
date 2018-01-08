package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;

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

    private AppCompatImageView mIvChatContent;

    public ChatRowPicture(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean chatUserInfoBean) {
        super(context, message, position, adapter, chatUserInfoBean);
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
    protected void onViewUpdate(EMMessage msg) {

    }

    @Override
    protected void onSetUpView() {
        super.onSetUpView();
        EMImageMessageBody imageMessageBody = (EMImageMessageBody) message.getBody();
        // 图片地址
        String url = TextUtils.isEmpty(imageMessageBody.getRemoteUrl()) ? imageMessageBody.getLocalUrl() : imageMessageBody.getRemoteUrl();
        int width = imageMessageBody.getWidth() == 0 ? 200 : imageMessageBody.getWidth();
        int height = imageMessageBody.getHeight() == 0 ? 200 : imageMessageBody.getHeight();
        // 图片内容
        Glide.with(getContext())
                .load(url)
                .override(width, height)
                .placeholder(R.drawable.ease_default_image)
                .error(R.drawable.ease_default_image)
                .into(mIvChatContent);
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
                    imageBean.setWidth(width);
                    imageBean.setHeight(height);
                    imageBeanList.add(imageBean);
                    AnimationRectBean rect = AnimationRectBean.buildFromImageView(mIvChatContent);
                    animationRectBeanArrayList.add(rect);
                    GalleryActivity.startToGallery(getContext(), 0, imageBeanList,
                            animationRectBeanArrayList);
                });
    }

    public AppCompatImageView getIvChatContent() {
        return mIvChatContent;
    }
}
