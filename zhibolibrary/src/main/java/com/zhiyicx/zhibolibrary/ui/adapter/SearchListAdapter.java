package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.SearchListHolder;

import java.util.List;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class SearchListAdapter extends MoreLinearAdapter<SearchResult> {
    public SearchListAdapter(List<SearchResult> infos) {
        super(infos);
    }

    @Override
    public ZBLBaseHolder<SearchResult> getHolder(View v) {
        return new SearchListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.zb_recycle_item_search;
    }
}
