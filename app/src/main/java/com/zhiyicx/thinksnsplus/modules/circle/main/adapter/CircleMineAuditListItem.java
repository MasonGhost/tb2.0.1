package com.zhiyicx.thinksnsplus.modules.circle.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/11/14/13:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMineAuditListItem extends BaseCircleItem {

    private Drawable mPayDrawable;

    public CircleMineAuditListItem(Context context, CircleItemItemEvent circleItemItemEvent) {
        super(circleItemItemEvent);
        mPayDrawable = UIUtils.getCompoundDrawables(context, R.mipmap.musici_pic_pay02);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_circle_mine_audit_list;
    }

    @Override
    public boolean isForViewType(CircleInfo item, int position) {
        return item.getId() > 0;
    }

    @Override
    public void convert(ViewHolder holder, CircleInfo circleInfo, CircleInfo lastT, int position, int itemCounts) {

        // 封面
        ImageView circleCover = holder.getView(R.id.iv_circle_cover);

        // 名字
        TextView circleName = holder.getView(R.id.tv_circle_name);
        circleName.setCompoundDrawables(null, null, CircleInfo.CirclePayMode.PAID.value.equals(circleInfo.getMode()) ? mPayDrawable : null, null);
        Context context = holder.getConvertView().getContext();

        // 设置封面
        Glide.with(context)
                .load(circleInfo.getAvatar())
                .error(R.drawable.shape_default_image)
                .placeholder(R.drawable.shape_default_image)
                .into(circleCover);

        circleName.setText(circleInfo.getName());

        // 状态
        holder.setText(R.id.tv_aduit_status,
                context.getString(AppApplication.getMyUserIdWithdefault() == circleInfo.getUser_id() ? R.string.create_wait_audit : R.string
                        .join_wait_audit)
        );
        // 时间
        holder.setText(R.id.tv_circle_time, TimeUtils.getStandardTimeWithYeay(TimeUtils.utc2LocalLong(circleInfo.getCreated_at())));
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mCircleItemItemEvent == null) {
                        return;
                    }
                    mCircleItemItemEvent.toCircleDetail(circleInfo);
                });
        RxView.clicks(holder.getView(R.id.iv_circle_cover))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {

                });
    }

}
