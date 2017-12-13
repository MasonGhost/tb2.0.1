package com.zhiyicx.thinksnsplus.widget.chat;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.chat.EMMessage;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageLoaderStrategy;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.imsdk.entity.MessageStatus;
import com.zhiyicx.imsdk.entity.MessageType;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
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
        return item.getLastMessage().getType() == MessageType.MESSAGE_TYPE_TEXT;
    }

    @Override
    public void convert(final ViewHolder holder, final ChatItemBean chatItemBean, ChatItemBean lastChatItemBean, int position, int itemCounts) {

        if (position == itemCounts - 1) {
            // 为最后一项加上间距
            holder.getConvertView().setPadding(holder.getConvertView().getPaddingLeft(), holder.getConvertView().getPaddingTop(), holder.getConvertView().getPaddingRight(), holder.getConvertView().getResources().getDimensionPixelSize(R.dimen.spacing_mid));
        } else {
            holder.getConvertView().setPadding(holder.getConvertView().getPaddingLeft(), holder.getConvertView().getPaddingTop(), holder.getConvertView().getPaddingRight(), 0);
        }
        // 显示时间的，最大间隔时间；当两条消息间隔 > MAX_SPACING_TIME 时显示时间
        if (lastChatItemBean == null || (chatItemBean.getMessage().getMsgTime() - lastChatItemBean.getMessage().getMsgTime()) >= (MAX_SPACING_TIME * ConstantConfig.MIN)) {
            holder.setText(R.id.tv_chat_time, TimeUtils.getTimeFriendlyForChat(chatItemBean.getMessage().getMsgTime()));
            holder.setVisible(R.id.tv_chat_time, View.VISIBLE);
        } else {
            holder.setVisible(R.id.tv_chat_time, View.GONE);
        }
        // 消息状态
        if (chatItemBean.getMessage().status() ==  EMMessage.Status.FAIL){
            holder.setImageResource(R.id.msg_status, R.mipmap.msg_box_remind);
            holder.setVisible(R.id.msg_status, View.VISIBLE);
        } else {
            holder.setVisible(R.id.msg_status, View.GONE);
        }
        if (chatItemBean.getUserInfo() == null) {
            return;
        }
        // 是否需要显示名字
        if (mIsShowName) {
            holder.setVisible(R.id.tv_chat_name, View.VISIBLE);
            // 测试数据，暂时使用
            holder.setText(R.id.tv_chat_name, chatItemBean.getUserInfo().getName());
        } else {
            holder.setVisible(R.id.tv_chat_name, View.GONE);
        }
        // 是否需要显示头像
        if (mIsShowAvatar) {
            holder.setVisible(R.id.iv_chat_headpic, View.VISIBLE);
            // TS 助手
            if (chatItemBean.getUserInfo().getName().equals(holder.getConvertView().getContext().getString(R.string.ts_helper))) {
                ((ImageView) holder.getView(R.id.iv_chat_headpic)).setImageResource(R.mipmap.ico_ts_assistant);
            } else {
                ImageUtils.loadCircleUserHeadPic(chatItemBean.getUserInfo(), holder.getView(R.id.iv_chat_headpic));
            }
        } else {
            holder.setVisible(R.id.iv_chat_headpic, View.GONE);
        }

        holder.setText(R.id.tv_chat_content, chatItemBean.getLastMessage().getTxt());
        // 响应事件
        if (mMessageListItemClickListener != null) {
            View.OnClickListener mStatusClick = v -> mMessageListItemClickListener.onStatusClick(chatItemBean);

            View.OnClickListener mUserInfoClick = v -> mMessageListItemClickListener.onUserInfoClick(chatItemBean);

            View.OnLongClickListener mUserInfoLongClick = v -> mMessageListItemClickListener.onUserInfoLongClick(chatItemBean);
            View.OnClickListener mBubbleClick = v -> mMessageListItemClickListener.onBubbleClick(chatItemBean);

            View.OnLongClickListener mBubbleLongClick = v -> mMessageListItemClickListener.onBubbleLongClick(chatItemBean);

            holder.setOnClickListener(R.id.msg_status, mStatusClick);
            holder.setOnClickListener(R.id.tv_chat_name, mUserInfoClick);
            holder.setOnLongClickListener(R.id.tv_chat_name, mUserInfoLongClick);
            holder.setOnClickListener(R.id.iv_chat_headpic, mUserInfoClick);
            holder.setOnLongClickListener(R.id.iv_chat_headpic, mUserInfoLongClick);
            holder.setOnClickListener(R.id.rl_chat_bubble, mBubbleClick);
//            holder.setOnLongClickListener(R.id.rl_chat_bubble, mBubbleLongClick);
        }

    }

}

