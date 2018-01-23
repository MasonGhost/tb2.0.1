package com.zhiyicx.thinksnsplus.modules.chat.adapter;

import android.content.Context;
import android.widget.TextView;

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
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class EditGroupOwnerAdapter extends CommonAdapter<UserInfoBean>{

    public EditGroupOwnerAdapter(Context context, List<UserInfoBean> datas) {
        super(context, R.layout.item_edit_group_owner, datas);
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        UserAvatarView ivUserPortrait = holder.getView(R.id.iv_user_portrait);
        TextView tvUserName = holder.getView(R.id.tv_user_name);
        ImageUtils.loadUserHead(userInfoBean, ivUserPortrait, false);
        tvUserName.setText(userInfoBean.getName());
    }
}
