package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class PersonalCenterDynamicListForZeroImage extends PersonalCenterDynamicListBaseItem {
    public PersonalCenterDynamicListForZeroImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return super.getItemViewLayoutId();
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return position == 0 || (item.getFeed_mark() != null && (item.getFeed().getStorages() == null||item.getFeed().getStorages().isEmpty()));
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
    }
}
