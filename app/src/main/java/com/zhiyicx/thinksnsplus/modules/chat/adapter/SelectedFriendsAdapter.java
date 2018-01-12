package com.zhiyicx.thinksnsplus.modules.chat.adapter;

import android.content.Context;

import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author Catherine
 * @describe 选中的好友的adapter
 * @date 2018/1/12
 * @contact email:648129313@qq.com
 */

public class SelectedFriendsAdapter extends CommonAdapter<UserInfoBean>{

    public SelectedFriendsAdapter(Context context, List<UserInfoBean> datas) {
        super(context, R.layout.item_selected_friends, datas);
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        UserAvatarView ivUserPortrait = holder.getView(R.id.iv_user_portrait);
        ImageUtils.loadCircleUserHeadPic(userInfoBean, ivUserPortrait);
    }
}
