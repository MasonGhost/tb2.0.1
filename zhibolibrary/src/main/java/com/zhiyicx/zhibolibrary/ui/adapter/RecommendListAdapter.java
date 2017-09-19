package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.LiveListHolder;

import java.util.List;

/**
 * Created by zhiyicx on 2016/4/5.
 */
public class RecommendListAdapter extends DefaultAdapter<SearchResult> {

    public RecommendListAdapter(List<SearchResult> infos) {
        super(infos);
    }


    @Override
    public ZBLBaseHolder<SearchResult> getHolder(View v) {
        return new LiveListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.zb_finish_zhiboinfo_card;
    }
}
