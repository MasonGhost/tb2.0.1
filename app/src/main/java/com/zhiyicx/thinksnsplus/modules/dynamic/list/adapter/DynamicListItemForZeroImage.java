package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.textview.SpanTextViewWithEllipsize;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class DynamicListItemForZeroImage extends DynamicListBaseItem {
    private OnFollowClickLisitener mOnFollowlistener;

    public DynamicListItemForZeroImage(Context context) {
        super(context);
    }


    public void setOnFollowlistener(OnFollowClickLisitener onFollowlistener) {
        mOnFollowlistener = onFollowlistener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_zero_image_for_tb;
    }

    @Override
    public boolean isForViewType(DynamicDetailBeanV2 item, int position) {
        return item.getFeed_mark() != null && (item.getImages() == null || item.getImages().isEmpty());
    }

    @Override
    public void convert(ViewHolder holder, DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position, itemCounts);
        SpanTextViewWithEllipsize contentView = holder.getView(R.id.tv_content);
        contentView.setMaxlines(contentView.getResources().getInteger(R.integer
                .dynamic_list_content_show_lines));
        holder.setVisible(R.id.tv_follow, dynamicBean.getUserInfoBean().getUser_id() == AppApplication.getMyUserIdWithdefault() || dynamicBean
                .getUserInfoBean().getFollower() ? View.INVISIBLE : View.VISIBLE);
        if (mOnFollowlistener != null) {
            RxView.clicks(holder.getView(R.id.tv_follow))
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> {
                        mOnFollowlistener.onFollowClick(dynamicBean.getUserInfoBean());
                        holder.setVisible(R.id.tv_follow, View.INVISIBLE);
                        dynamicBean.getUserInfoBean().setFollower(true);
                    });
        }
    }

    public interface OnFollowClickLisitener {
        void onFollowClick(UserInfoBean userInfoBean);
    }
}
