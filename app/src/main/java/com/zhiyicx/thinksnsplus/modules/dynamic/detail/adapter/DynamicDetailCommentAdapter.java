package com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class DynamicDetailCommentAdapter extends CommonAdapter<DynamicCommentBean> {

    public DynamicDetailCommentAdapter(Context context, int layoutId, List<DynamicCommentBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, DynamicCommentBean dynamicCommentBean, int position) {

    }
}
