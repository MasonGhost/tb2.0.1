package com.zhiyicx.thinksnsplus.widget.chat;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageLoaderStrategy;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.imsdk.entity.MessageType;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe 我发送的文本消息
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class MessageTextItemDelagate implements ItemViewDelegate<ChatItemBean> {

    public static final int MAX_SPACING_TIME = 6;// 显示时间的，最大间隔时间；当两条消息间隔 > MAX_SPACING_TIME 时显示时间
    protected GlideImageLoaderStrategy mImageLoader;

    protected boolean mIsShowName = true;
    protected boolean mIsShowAvatar = true;
    protected Drawable mBubbleBg;


    protected ChatMessageList.MessageListItemClickListener mMessageListItemClickListener;

    public void setMessageListItemClickListener(ChatMessageList.MessageListItemClickListener messageListItemClickListener) {
        mMessageListItemClickListener = messageListItemClickListener;
    }


    public MessageTextItemDelagate(boolean showName, boolean showAvatar, Drawable myBubbleBg, Drawable otherBuddleBg) {
        this.mIsShowName = showName;
        this.mIsShowAvatar = showAvatar;
        if (myBubbleBg != null) {
            this.mBubbleBg = myBubbleBg;
        } else {
            this.mBubbleBg = otherBuddleBg;
        }
        mImageLoader = new GlideImageLoaderStrategy();
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_list_send_text;
    }

    /**
     * 消息类型为文本，是我发送的消息
     *
     * @param item
     * @param position
     * @return
     */
    @Override
    public boolean isForViewType(ChatItemBean item, int position) {
        // TODO: 2017/1/6 需要添加是否是我的消息的判断
        return item.getLastMessage().getType() == MessageType.MESSAGE_TYPE_TEXT;
    }

    @Override
    public void convert(ViewHolder holder, final ChatItemBean chatItemBean, ChatItemBean lastChatItemBean, int position) {
//        holder.getView(R.id.rl_chat_bubble).setBackgroundDrawable(mBubbleBg);
        // 显示时间的，最大间隔时间；当两条消息间隔 > MAX_SPACING_TIME 时显示时间
        if (lastChatItemBean == null || (chatItemBean.getLastMessage().getCreate_time() - lastChatItemBean.getLastMessage().getCreate_time()) >= MAX_SPACING_TIME * ConstantConfig.MIN) {
            holder.setText(R.id.tv_chat_time, TimeUtils.getTimeFriendlyForDetail(chatItemBean.getLastMessage().getCreate_time() / 1000));// 测试数据，暂时使用
            holder.setVisible(R.id.tv_chat_time, View.VISIBLE);
        } else {
            holder.setVisible(R.id.tv_chat_time, View.GONE);
        }
        // 是否需要显示名字
        if (mIsShowName) {
            holder.setVisible(R.id.tv_chat_name, View.VISIBLE);
            holder.setText(R.id.tv_chat_name, chatItemBean.getUserInfo().getName());// 测试数据，暂时使用
        } else {
            holder.setVisible(R.id.tv_chat_name, View.GONE);
        }
        // 是否需要显示头像
        if (mIsShowAvatar) {
            holder.setVisible(R.id.iv_chat_headpic, View.VISIBLE);
            mImageLoader.loadImage(holder.getConvertView().getContext(), GlideImageConfig.builder()
                    .url(chatItemBean.getUserInfo().getUserIcon())
                    .placeholder(R.drawable.shape_default_image_circle)
                    .transformation(new GlideCircleTransform(holder.getConvertView().getContext()))
                    .errorPic(R.drawable.shape_default_image_circle)
                    .imagerView((ImageView) holder.getView(R.id.iv_chat_headpic))
                    .build()
            );
        } else {
            holder.setVisible(R.id.iv_chat_headpic, View.GONE);
        }

        holder.setText(R.id.tv_chat_content, chatItemBean.getLastMessage().getTxt());
        // 响应事件
        if (mMessageListItemClickListener != null) {
            View.OnClickListener mStatusClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessageListItemClickListener.onStatusClick(chatItemBean);
                }
            };

            View.OnClickListener mUserInfoClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessageListItemClickListener.onUserInfoClick(chatItemBean);
                }
            };

            View.OnLongClickListener mUserInfoLongClick = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mMessageListItemClickListener.onUserInfoLongClick(chatItemBean);
                }
            };
            View.OnClickListener mBubbleClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessageListItemClickListener.onBubbleClick(chatItemBean);
                }
            };

            View.OnLongClickListener mBubbleLongClick = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mMessageListItemClickListener.onBubbleLongClick(chatItemBean);

                }
            };
            View.OnClickListener mOnItemClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessageListItemClickListener.onItemClickListener(chatItemBean);
                }
            };

            View.OnLongClickListener mOnItemLongClick = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mMessageListItemClickListener.onItemLongClickListener(chatItemBean);
                }
            };
            holder.setOnClickListener(R.id.msg_status, mStatusClick);
            holder.setOnClickListener(R.id.tv_chat_name, mUserInfoClick);
            holder.setOnLongClickListener(R.id.tv_chat_name, mUserInfoLongClick);
            holder.setOnClickListener(R.id.iv_chat_headpic, mUserInfoClick);
            holder.setOnLongClickListener(R.id.iv_chat_headpic, mUserInfoLongClick);
            holder.setOnClickListener(R.id.rl_chat_bubble, mBubbleClick);
            holder.setOnLongClickListener(R.id.rl_chat_bubble, mBubbleLongClick);
            holder.getConvertView().setOnClickListener(mOnItemClick);
            holder.getConvertView().setOnLongClickListener(mOnItemLongClick);
        }

    }


}

