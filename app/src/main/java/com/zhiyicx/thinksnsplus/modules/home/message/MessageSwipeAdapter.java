package com.zhiyicx.thinksnsplus.modules.home.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.util.Attributes;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/20
 * @Contact master.jungle68@gmail.com
 */

public class MessageSwipeAdapter extends RecyclerSwipeAdapter<MessageSwipeAdapter.MessageListHolder> {

    List<MessageItemBean> mDatas;
    Context mContext;

    public MessageSwipeAdapter(Context context, List<MessageItemBean> datas) {
        this.mDatas = datas;
        mContext = context;
        setMode(Attributes.Mode.Single);
    }


    /**
     * 设置item 数据
     *
     * @param holder          控件管理器
     * @param messageItemBean 当前数据
     * @param position        当前数据位置
     */
    private void setItemData(final MessageListHolder holder, final MessageItemBean messageItemBean, final int position) {
        switch (messageItemBean.getConversation().getType()) {
            case ChatType.CHAT_TYPE_PRIVATE:// 私聊
                AppApplication.AppComponentHolder.getAppComponent().imageLoader().loadImage(mContext, GlideImageConfig.builder()
                        .url(ImageUtils.imagePathConvert(messageItemBean.getUserInfo().getAvatar(), ImageZipConfig.IMAGE_38_ZIP))
                        .transformation(new GlideCircleTransform(mContext))
                        .errorPic(R.drawable.shape_default_image_circle)
                        .imagerView(holder.mIvHeadpic)
                        .build()
                );
                holder.mTvName.setText(messageItemBean.getUserInfo().getName());     // 响应事件
                setUserInfoClick(holder.mTvName, messageItemBean.getUserInfo());
                setUserInfoClick(holder.mIvHeadpic, messageItemBean.getUserInfo());

                break;
            case ChatType.CHAT_TYPE_GROUP:// 群组
                holder.mIvHeadpic.setImageResource(R.drawable.shape_default_image_circle);
                holder.mTvName.setText(TextUtils.isEmpty(messageItemBean.getConversation().getName())
                        ? mContext.getString(R.string.default_message_group) : messageItemBean.getConversation().getName());
                break;
            default:
        }
        holder.mTvContent.setText(messageItemBean.getConversation().getLast_message_text());
        if (messageItemBean.getConversation().getLast_message_time() == 0) {
            holder.mTvName.setText("");
        } else {
            holder.mTvTime.setText(TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(messageItemBean.getConversation().getLast_message_time())));
        }
        holder.mTvTip.setBadgeCount(messageItemBean.getUnReadMessageNums());
        // 右边
        holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.mSwipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        RxView.clicks(holder.mTvRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mItemManger.removeShownLayouts(holder.mSwipeLayout);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, mDataset.size());
                        notifyDataSetChanged();
                        mItemManger.closeAllItems();
                    }
                });
            mItemManger.bindView(holder.itemView, position);

    }

    private void setUserInfoClick(View v, final UserInfoBean userInfoBean) {
        RxView.clicks(v)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter(userInfoBean);
                    }
                });
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(mContext, userInfoBean);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public MessageListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list, parent, false);
        return new MessageListHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageListHolder viewHolder, int position) {
        setItemData(viewHolder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class MessageListHolder extends RecyclerView.ViewHolder {
        // right
        SwipeLayout mSwipeLayout;
        TextView mTvRight;
        // left
        ImageView mIvHeadpic;
        TextView mTvName;
        TextView mTvContent;
        TextView mTvTime;
        BadgeView mTvTip;

        public MessageListHolder(View itemView) {
            super(itemView);
            mSwipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            mTvRight = (TextView) itemView.findViewById(R.id.tv_right);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvTip = (BadgeView) itemView.findViewById(R.id.tv_tip);
            mIvHeadpic = (ImageView) itemView.findViewById(R.id.iv_headpic);
        }
    }
}
