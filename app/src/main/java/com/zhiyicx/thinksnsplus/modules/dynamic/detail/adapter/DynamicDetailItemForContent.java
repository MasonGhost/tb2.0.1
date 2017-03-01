package com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter;

import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author LiuChao
 * @describe 动态详情内容item
 * @date 2017/3/1
 * @contact email:450127106@qq.com
 */

public class DynamicDetailItemForContent implements ItemViewDelegate {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_content;
    }

    @Override
    public boolean isForViewType(Object item, int position) {
        return false;
    }

    @Override
    public void convert(ViewHolder holder, Object o, Object lastT, int position) {

    }
}
