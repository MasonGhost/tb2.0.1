package com.zhiyicx.thinksnsplus.modules.tb.detail;

import android.text.TextUtils;

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
public class MerchainMessageListItemNews implements ItemViewDelegate<MerchianMassageBean.DataBean> {

    private UserInfoBean mUserInfoBean;

    public MerchainMessageListItemNews(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_merchian_message_news;
    }

    @Override
    public boolean isForViewType(MerchianMassageBean.DataBean item, int position) {
        return "news".equals(item.getType());
    }

    @Override
    public void convert(ViewHolder holder, MerchianMassageBean.DataBean dataBean, MerchianMassageBean.DataBean lastT, int position, int itemCounts) {
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyForDetail(dataBean.getCreated_at()));
        holder.setText(R.id.tv_title, dataBean.getSubject());
        if (TextUtils.isEmpty(dataBean.getText_content())) {
            holder.setText(R.id.tv_des, dataBean.getContent());
        } else {
            holder.setText(R.id.tv_des, dataBean.getText_content());
        }
        ImageUtils.loadImageDefault(holder.getView(R.id.iv_head), ImageUtils.imagePathConvertV2(dataBean.getImage() == null ? 0 : dataBean
                .getImage().getId(), 0, 0, 0));

    }
}
