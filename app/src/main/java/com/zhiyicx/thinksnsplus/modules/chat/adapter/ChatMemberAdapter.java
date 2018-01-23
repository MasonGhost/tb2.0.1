package com.zhiyicx.thinksnsplus.modules.chat.adapter;

import android.content.Context;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author Catherine
 * @describe 群信息的成员item
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatMemberAdapter extends CommonAdapter<ChatUserInfoBean>{


    public ChatMemberAdapter(Context context, List<ChatUserInfoBean> datas) {
        super(context, R.layout.item_chat_member, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ChatUserInfoBean chatUserInfoBean, int position) {
        UserAvatarView ivUserPortrait = holder.getView(R.id.iv_user_portrait);
        if (chatUserInfoBean.getUser_id() == -1L){
            // 加号
            ivUserPortrait.getIvAvatar().setImageResource(R.mipmap.btn_chatdetail_add);
        } else if (chatUserInfoBean.getUser_id() == -2L){
            ivUserPortrait.getIvAvatar().setImageResource(R.mipmap.btn_chatdetail_reduce);
        } else {
            ImageUtils.loadUserHead(chatUserInfoBean, ivUserPortrait, false);
            holder.setText(R.id.tv_user_name, chatUserInfoBean.getName());
        }

    }
}
