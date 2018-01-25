package com.zhiyicx.thinksnsplus.modules.chat.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/12
 * @contact email:648129313@qq.com
 */

public class SelectFriendsAllAdapter extends CommonAdapter<UserInfoBean> {

    private OnUserSelectedListener mListener;
    private static final int STATE_SELECTED = 1;
    private static final int STATE_UNSELECTED = 0;
    private static final int STATE_CAN_NOT_BE_CHANGED = -1;

    public SelectFriendsAllAdapter(Context context, List<UserInfoBean> datas, OnUserSelectedListener listener) {
        super(context, R.layout.item_select_friends, datas);
        this.mListener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        ImageView cbFriends = holder.getView(R.id.cb_friends);
        UserAvatarView ivUserPortrait = holder.getView(R.id.iv_user_portrait);
        TextView tvUserName = holder.getTextView(R.id.tv_user_name);
        ImageUtils.loadCircleUserHeadPic(userInfoBean, ivUserPortrait);
        tvUserName.setText(userInfoBean.getName());
        setSelectedState(cbFriends, userInfoBean);
        RxView.clicks(holder.getConvertView())
                .subscribe(aVoid -> {
                    if (mListener != null && userInfoBean.getIsSelected() != STATE_CAN_NOT_BE_CHANGED) {
                        if (userInfoBean.getIsSelected() == STATE_SELECTED){
                            userInfoBean.setIsSelected(STATE_UNSELECTED);
                        } else {
                            userInfoBean.setIsSelected(STATE_SELECTED);
                        }
                        setSelectedState(cbFriends, userInfoBean);
                        mListener.onUserSelected(userInfoBean);
                    }
                });
    }

    /**
     * 设置选中状态
     *
     * @param imageView    icon
     * @param userInfoBean user
     */
    private void setSelectedState(ImageView imageView, UserInfoBean userInfoBean) {
        switch (userInfoBean.getIsSelected()) {
            case STATE_SELECTED:
                imageView.setImageResource(R.mipmap.msg_box_choose_now);
                break;
            case STATE_UNSELECTED:
                imageView.setImageResource(R.mipmap.msg_box);
                break;
            case STATE_CAN_NOT_BE_CHANGED:
                imageView.setImageResource(R.mipmap.msg_box_choose_before);
                break;
            default:
        }
    }

    /**
     * 选中item监听
     */
    public interface OnUserSelectedListener {
        /**
         * 选中好友
         *
         * @param userInfoBean 用户信息
         */
        void onUserSelected(UserInfoBean userInfoBean);
    }
}
