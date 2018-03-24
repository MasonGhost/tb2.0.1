package com.zhiyicx.thinksnsplus.modules.tb.detail;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.tbmerchianmessage.MerchianMassageBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/23
 * @contact master.jungle68@gmail.com
 */
public class MerchainMessageListItemDynamic implements ItemViewDelegate<MerchianMassageBean.DataBean> {
    private UserInfoBean mUserInfoBean;

    public MerchainMessageListItemDynamic(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_merchian_message_dynamic;
    }

    @Override
    public boolean isForViewType(MerchianMassageBean.DataBean item, int position) {
        return "feed".equals(item.getType());
    }

    @Override
    public void convert(ViewHolder holder, MerchianMassageBean.DataBean dataBean, MerchianMassageBean.DataBean lastT, int position, int itemCounts) {
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyForDetail(dataBean.getCreated_at()));
        holder.setText(R.id.tv_title, mUserInfoBean.getName());
        holder.setText(R.id.tv_des, dataBean.getFeed_content());
        ImageUtils.loadImageDefault(holder.getView(R.id.iv_avatar), mUserInfoBean.getAvatar());
    }
}
