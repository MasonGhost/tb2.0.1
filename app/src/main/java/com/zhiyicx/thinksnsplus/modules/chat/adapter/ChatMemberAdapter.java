package com.zhiyicx.thinksnsplus.modules.chat.adapter;

import android.content.Context;
import android.view.View;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Objects;

/**
 * @author Catherine
 * @describe 群信息的成员item
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatMemberAdapter extends CommonAdapter<UserInfoBean>{

    private Long mOwnerId;

    public ChatMemberAdapter(Context context, List<UserInfoBean> datas, long ownerId) {
        super(context, R.layout.item_chat_member, datas);
        this.mOwnerId = ownerId;
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean chatUserInfoBean, int position) {
        UserAvatarView ivUserPortrait = holder.getView(R.id.iv_user_portrait);
        if (chatUserInfoBean.getUser_id() == -1L){
            // 加号
            ivUserPortrait.getIvAvatar().setImageResource(R.mipmap.btn_chatdetail_add);
            holder.setVisible(R.id.tv_owner_flag, View.GONE);
            holder.setVisible(R.id.tv_user_name, View.GONE);
            ivUserPortrait.getIvVerify().setVisibility(View.GONE);
        } else if (chatUserInfoBean.getUser_id() == -2L){
            ivUserPortrait.getIvAvatar().setImageResource(R.mipmap.btn_chatdetail_reduce);
            holder.setVisible(R.id.tv_owner_flag, View.GONE);
            holder.setVisible(R.id.tv_user_name, View.GONE);
            ivUserPortrait.getIvVerify().setVisibility(View.GONE);
        } else {
            ImageUtils.loadUserHead(chatUserInfoBean, ivUserPortrait, false);
            holder.setText(R.id.tv_user_name, chatUserInfoBean.getName());
            holder.setVisible(R.id.tv_owner_flag, mOwnerId.equals(chatUserInfoBean.getUser_id()) ? View.VISIBLE : View.GONE);
            holder.setVisible(R.id.tv_user_name, View.VISIBLE);
            ivUserPortrait.getIvVerify().setVisibility(View.VISIBLE);
        }
    }

    public void setOwnerId(Long ownerId) {
        mOwnerId = ownerId;
    }
}
