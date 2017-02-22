package com.zhiyicx.thinksnsplus.modules.dynamic.list;


import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageLoaderStrategy;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe 我发送的文本消息
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListBaseItem implements ItemViewDelegate<DynamicBean> {

    protected GlideImageLoaderStrategy mImageLoader;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return false;
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, int position) {

    }


}

