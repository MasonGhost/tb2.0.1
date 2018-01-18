package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe 我的好友列表adapter
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */

public class MyFriendsListAdapter extends CommonAdapter<UserInfoBean> {


    public MyFriendsListAdapter(Context context, List<UserInfoBean> datas) {
        super(context, R.layout.item_my_friends_list, datas);
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        setItemData(holder, userInfoBean, position);
    }

    private void setItemData(final ViewHolder holder, final UserInfoBean userInfoBean, final int position) {
        if (userInfoBean == null) {
            // 这种情况一般不会发生，为了防止崩溃，做处理
            return;
        }
        RxView.clicks(holder.getView(R.id.iv_to_chat))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    // 点击跳转聊天
                    MessageItemBeanV2 messageItemBean = new MessageItemBeanV2();
                    messageItemBean.setUserInfo(userInfoBean);
                    Intent to = new Intent(mContext, ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ChatFragment.BUNDLE_CHAT_USER, userInfoBean);
                    bundle.putString(ChatFragment.BUNDLE_CHAT_ID, String.valueOf(userInfoBean.getUser_id()));
                    to.putExtras(bundle);
                    mContext.startActivity(to);
                });
        // 设置用户名，用户简介
        holder.setText(R.id.tv_name, userInfoBean.getName());
        holder.setText(R.id.tv_user_signature, TextUtils.isEmpty(userInfoBean.getIntro()) ? getContext().getString(R.string.intro_default) : userInfoBean.getIntro());
        // 修改点赞数量颜色
        String digCountString = userInfoBean.getExtra().getLikes_count() + "";
        // 当前没有获取到点赞数量，设置为0，否则ColorPhrase会抛出异常
        if (TextUtils.isEmpty(digCountString)) {
            digCountString = 0 + "";
        }
        String digContent = "点赞 " + "<" + digCountString + ">";
        CharSequence charSequence = ColorPhrase.from(digContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        TextView digCount = holder.getView(R.id.tv_dig_count);
        digCount.setText(charSequence);
        // 头像加载
        ImageUtils.loadCircleUserHeadPic(userInfoBean, holder.getView(R.id.iv_headpic));
        // 添加点击事件
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(getContext(), userInfoBean));
    }


    /**
     * 前往用户个人中心
     */
    private void toUserCenter(Context context, UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }
}
