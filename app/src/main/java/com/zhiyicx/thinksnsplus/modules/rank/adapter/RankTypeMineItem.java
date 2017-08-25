package com.zhiyicx.thinksnsplus.modules.rank.adapter;

import android.content.Context;
import android.view.View;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.rank.type_list.RankTypeListContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public class RankTypeMineItem extends RankTypeItem {

    public RankTypeMineItem(Context context, String rankType, RankTypeListContract.Presenter presenter) {
        super(context, rankType, presenter);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_rank_type_mine;
    }

    @Override
    public boolean isForViewType(UserInfoBean item, int position) {
        return position == 0 && item.getUser_id().equals(AppApplication.getmCurrentLoginAuth().getUser_id());
    }

    @Override
    public void convert(ViewHolder holder, UserInfoBean userInfoBean, UserInfoBean lastT, int position, int itemCounts) {
        super.convert(holder, userInfoBean, lastT, position, itemCounts);
        holder.setVisible(R.id.tv_rank, View.GONE);
        holder.setVisible(R.id.iv_user_follow, View.GONE);
    }
}
