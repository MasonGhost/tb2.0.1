package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;


import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.GoldHistoryJson;
import com.zhiyicx.zhibolibrary.ui.holder.GoldHistoryListHolder;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;

import java.util.List;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class GoldHistoryListAdapter extends MoreLinearAdapter<GoldHistoryJson> {
    public GoldHistoryListAdapter(List<GoldHistoryJson> infos) {
        super(infos);
    }

    @Override
    public ZBLBaseHolder<GoldHistoryJson> getHolder(View v) {
        return new GoldHistoryListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.zb_recycle_item_gold_history;
    }
}
