package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.holder.LiveListNoUserInfoHolder;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;

import java.util.List;

/**
 * Created by zhiyicx on 2016/3/31.
 */
public class LiveListAdapter extends MoreAdapter<SearchResult> {
    private boolean isNeedShowUserInfo;

    public LiveListAdapter(List<SearchResult> infos, boolean isNeedShowUserInfo) {
        super(infos);
        this.isNeedShowUserInfo = isNeedShowUserInfo;
    }

    @Override
    public ZBLBaseHolder<SearchResult> getHolder(View v) {
//        if (isNeedShowUserInfo)
//            return new LiveListHolder(v);
//        else
            return new LiveListNoUserInfoHolder(v);
    }

    @Override
    public int getLayoutId() {

//        if (isNeedShowUserInfo)
//            return R.layout.zb_recycle_item_live;
//        else
            return R.layout.zb_recycle_item_live_nouserinfo;
    }


}
