package com.zhiyicx.thinksnsplus.modules.rank.adapter;

import android.content.Context;
import android.widget.RelativeLayout;

import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class RankIndexUserAdapter extends CommonAdapter<UserInfoBean> {

    public RankIndexUserAdapter(Context context, List<UserInfoBean> datas) {
        super(context, R.layout.item_rank_index_user, datas);
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        RelativeLayout rlUserContainer = holder.getView(R.id.rl_user_container);
        UserAvatarView userAvatarView = holder.getView(R.id.iv_user_portrait);
        int width = UIUtils.getWindowWidth(mContext) -
                2 * mContext.getResources().getDimensionPixelSize(R.dimen.spacing_mid);
        rlUserContainer.getLayoutParams().width = width / 5;
        userAvatarView.getLayoutParams().width = width / 5 - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.spacing_mid_small);
        userAvatarView.getLayoutParams().height = width / 5 - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.spacing_mid_small);
        ImageUtils.loadCircleUserHeadPic(userInfoBean, userAvatarView);
        holder.setText(R.id.tv_user_name, userInfoBean.getName());
    }
}
