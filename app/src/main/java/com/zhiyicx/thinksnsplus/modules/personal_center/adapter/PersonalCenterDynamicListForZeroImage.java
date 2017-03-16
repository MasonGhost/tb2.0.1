package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;

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
    public boolean isForViewType(DynamicBean item, int position) {
        return position == 0 || super.isForViewType(item, position);
    }
}
