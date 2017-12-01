package com.zhiyicx.thinksnsplus.modules.circle.main.adapter;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/11/14/13:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleTypeItem extends BaseCircleItem {

    public CircleTypeItem(CircleItemItemEvent circleItemItemEvent) {
        super(circleItemItemEvent);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.view_circle_type;
    }

    @Override
    public boolean isForViewType(CircleInfo item, int position) {
        return item.getId() < 0;
    }

    @Override
    public void convert(ViewHolder holder, CircleInfo circleInfo, CircleInfo lastT, int position, int itemCounts) {
        CombinationButton button = holder.getView(R.id.tv_circle_type);
        button.setLeftTextColor(SkinUtils.getColor(R.color.normal_for_assist_text));
        button.setLeftText(circleInfo.getName());
        button.setRightText(circleInfo.getSummary());

        if (circleInfo.getId() == -2) {

        }

        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (mCircleItemItemEvent == null) {
                        return;
                    }
                    if (circleInfo.getId()==BaseCircleItem.MYJOINEDCIRCLE){
                        mCircleItemItemEvent.toAllJoinedCircle(circleInfo);
                    }

                });
    }
}
