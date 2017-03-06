package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */

public class DigListAdapter extends CommonAdapter<DynamicDigListBean> {
    public DigListAdapter(Context context, int layoutId, List<DynamicDigListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, DynamicDigListBean dynamicDigListBean, int position) {

    }
}
