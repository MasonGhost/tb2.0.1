package com.zhiyicx.thinksnsplus.modules.chat.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
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

public class SelectFriendsAllAdapter extends CommonAdapter<UserInfoBean>{

    private  OnUserSelectedListener mListener;

    public SelectFriendsAllAdapter(Context context, List<UserInfoBean> datas, OnUserSelectedListener listener) {
        super(context, R.layout.item_select_friends, datas);
        this.mListener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        AppCompatCheckBox cbFriends = holder.getView(R.id.cb_friends);
        UserAvatarView ivUserPortrait = holder.getView(R.id.iv_user_portrait);
        TextView tvUserName = holder.getTextView(R.id.tv_user_name);
        cbFriends.setChecked(userInfoBean.isSelected());
        ImageUtils.loadCircleUserHeadPic(userInfoBean, ivUserPortrait);
        tvUserName.setText(userInfoBean.getName());
        RxView.clicks(holder.getConvertView())
                .subscribe(aVoid -> {
                    if (mListener != null){
                        userInfoBean.setSelected(!userInfoBean.isSelected());
                        cbFriends.setChecked(userInfoBean.isSelected());
                        mListener.onUserSelected(userInfoBean);
                    }
                });
    }

    /**
     * 选中item监听
     */
    public interface OnUserSelectedListener{
        /**
         * 选中好友
         * @param userInfoBean 用户信息
         */
        void onUserSelected(UserInfoBean userInfoBean);
    }
}
