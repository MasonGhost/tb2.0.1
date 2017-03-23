package com.zhiyicx.thinksnsplus.modules.home.message;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
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
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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

public class MessageAdapter extends CommonAdapter<MessageItemBean> implements SwipeItemMangerInterface, SwipeAdapterInterface {
    private SwipeItemMangerImpl mItemManger;

    public void setOnSwipItemClickListener(OnSwipItemClickListener onSwipItemClickListener) {
        mOnSwipItemClickListener = onSwipItemClickListener;
    }

    private OnSwipItemClickListener mOnSwipItemClickListener;

    public MessageAdapter(Context context, int layoutId, List<MessageItemBean> datas) {
        super(context, layoutId, datas);
        mItemManger = new SwipeItemRecyclerMangerImpl(this);
    }

    @Override
    protected void convert(ViewHolder holder, MessageItemBean messageItemBean, int position) {
        setItemData(holder, messageItemBean, position);
    }

    /**
     * 设置item 数据
     *
     * @param holder          控件管理器
     * @param messageItemBean 当前数据
     * @param position        当前数据位置
     */
    private void setItemData(ViewHolder holder, final MessageItemBean messageItemBean, final int position) {
        switch (messageItemBean.getConversation().getType()) {
            case ChatType.CHAT_TYPE_PRIVATE:// 私聊
                AppApplication.AppComponentHolder.getAppComponent().imageLoader().loadImage(mContext, GlideImageConfig.builder()
                        .url(ImageUtils.imagePathConvert(messageItemBean.getUserInfo().getAvatar(), ImageZipConfig.IMAGE_38_ZIP))
                        .transformation(new GlideCircleTransform(mContext))
                        .errorPic(R.drawable.shape_default_image_circle)
                        .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                        .build()
                );
                holder.setText(R.id.tv_name, messageItemBean.getUserInfo().getName());     // 响应事件
                setUserInfoClick(holder.getView(R.id.tv_name), messageItemBean.getUserInfo());
                setUserInfoClick(holder.getView(R.id.iv_headpic), messageItemBean.getUserInfo());

                break;
            case ChatType.CHAT_TYPE_GROUP:// 群组
                holder.setImageResource(R.id.iv_headpic, R.drawable.shape_default_image_circle);
                holder.setText(R.id.tv_name, TextUtils.isEmpty(messageItemBean.getConversation().getName())
                        ? mContext.getString(R.string.default_message_group) : messageItemBean.getConversation().getName());
                break;
            default:
        }
        holder.setText(R.id.tv_content, messageItemBean.getConversation().getLast_message_text());
        if (messageItemBean.getConversation().getLast_message_time() == 0) {
            holder.setText(R.id.tv_time, "");
        } else {
            holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(messageItemBean.getConversation().getLast_message_time())));
        }
        ((BadgeView) holder.getView(R.id.tv_tip)).setBadgeCount(messageItemBean.getUnReadMessageNums());
        // 右边
        final SwipeLayout swipeLayout = holder.getView(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        RxView.clicks(holder.getView(R.id.tv_right))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnSwipItemClickListener != null) {
                            mOnSwipItemClickListener.onRightClick(position);
                        }
                        mItemManger.closeAllItems();
                    }
                });
        RxView.clicks(holder.getView(R.id.rl_left))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnSwipItemClickListener != null && !mItemManger.isOpen(position)) {
                            mOnSwipItemClickListener.onLeftClick(position);
                        }
                        mItemManger.closeAllItems();
                    }
                });
        mItemManger.bindView(holder.getConvertView(), position);

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
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }

    public interface OnSwipItemClickListener {
        void onLeftClick(int position);

        void onRightClick(int position);
    }
}
