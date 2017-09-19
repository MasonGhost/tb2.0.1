package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.FilterMessage;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.FilterHolder;

import java.util.List;

/**
 * Created by jess on 8/19/16 18:14
 * Contact with jess.yan.effort@gmail.com
 */
public class FilterAdapter extends DefaultAdapter<FilterMessage> {
    public FilterAdapter(List<FilterMessage> infos) {
        super(infos);
    }

    @Override
    public ZBLBaseHolder<FilterMessage> getHolder(View v) {
        return new FilterHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.zb_recycle_filter_list;
    }
}
