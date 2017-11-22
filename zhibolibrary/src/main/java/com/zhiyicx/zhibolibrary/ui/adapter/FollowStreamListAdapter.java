package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.FollowStreamListHolder;

import java.util.List;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class FollowStreamListAdapter extends MoreLinearAdapter<SearchResult> {

    public FollowStreamListAdapter(List<SearchResult> infos) {
        super(infos);
    }

    @Override
    public ZBLBaseHolder<SearchResult> getHolder(View v) {
        return new FollowStreamListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.zb_recycle_item_stream_follow;
    }
}
