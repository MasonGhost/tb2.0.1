package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.RankListHolder;

import java.util.List;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class RankingListAdapter extends MoreLinearAdapter<SearchResult> {
    public RankingListAdapter(List<SearchResult> infos) {
        super(infos);
    }

    @Override
    public ZBLBaseHolder<SearchResult> getHolder(View v) {
        return new RankListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.zb_recycle_item_ranking;
    }
}
