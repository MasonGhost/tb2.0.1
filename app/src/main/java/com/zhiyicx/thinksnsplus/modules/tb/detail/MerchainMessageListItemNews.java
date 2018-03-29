package com.zhiyicx.thinksnsplus.modules.tb.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.tbmerchianmessage.MerchianMassageBean;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_ID;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;

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
            if(dataBean.getContent().length() > 15){
                holder.setText(R.id.tv_des, dataBean.getContent().substring(0, 15) + "...");
            } else {
                holder.setText(R.id.tv_des, dataBean.getContent());
            }
        } else {
            if(dataBean.getText_content().length() > 15){
                holder.setText(R.id.tv_des, dataBean.getText_content().substring(0, 15) + "...");
            } else {
                holder.setText(R.id.tv_des, dataBean.getText_content());
            }
        }
        ImageUtils.loadImageDefault(holder.getView(R.id.iv_head), ImageUtils.imagePathConvertV2(dataBean.getImage() == null ? 0 : dataBean
                .getImage().getId(), 0, 0, 0));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppApplication.sOverRead.contains(dataBean.getId())) {
                    AppApplication.sOverRead.add(dataBean.getId());
                }
                FileUtils.saveBitmapToFile(holder.getConvertView().getContext(), ConvertUtils.drawable2BitmapWithWhiteBg(holder.getConvertView()
                                .getContext()
                        , ((ImageView) holder.getView(R.id.iv_head)).getDrawable(), R.mipmap.icon), "info_share");
                Intent intent = new Intent(holder.getConvertView().getContext(), InfoDetailsActivity.class);
                Bundle bundle = new Bundle();
                InfoListDataBean infoListDataBean = new InfoListDataBean();
                infoListDataBean.setId((long) dataBean.getId());
                infoListDataBean.setTitle(dataBean.getTitle());
                bundle.putLong(BUNDLE_INFO_ID, dataBean.getId());
                bundle.putSerializable(BUNDLE_INFO, infoListDataBean);
                intent.putExtra(BUNDLE_INFO, bundle);
                holder.getConvertView().getContext().startActivity(intent);
            }
        });

    }
}
