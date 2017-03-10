package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForNineImage;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class PersonalCenterDynamicListItemForNineImage extends DynamicListItemForNineImage {
    public PersonalCenterDynamicListItemForNineImage(Context context) {
        super(context);
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_list_nine_image;
    }
}
