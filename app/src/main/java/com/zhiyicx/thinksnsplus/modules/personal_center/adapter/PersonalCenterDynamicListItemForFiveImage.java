package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class PersonalCenterDynamicListItemForFiveImage extends DynamicListItemForFiveImage {
    public PersonalCenterDynamicListItemForFiveImage(Context context) {
        super(context);
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_list_five_image;
    }
}
