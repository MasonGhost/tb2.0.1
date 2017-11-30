package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.VideoListHolder;

import java.util.List;

/**
 * Created by zhiyicx on 2016/3/31.
 */
public class VideoListAdapter extends MoreAdapter<SearchResult> {


    public VideoListAdapter(List<SearchResult> infos) {
        super(infos);
    }

    @Override
    public ZBLBaseHolder<SearchResult> getHolder(View v) {
        return new VideoListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.zb_recycle_item_video;
    }


}
