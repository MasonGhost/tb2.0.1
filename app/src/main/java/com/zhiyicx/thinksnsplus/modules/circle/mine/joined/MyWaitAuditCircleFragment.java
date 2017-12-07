package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleMineAuditListItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class MyWaitAuditCircleFragment extends BaseCircleListFragment  {


    public static MyWaitAuditCircleFragment newInstance(boolean isNeedToolBar) {
        MyWaitAuditCircleFragment circleListFragment = new MyWaitAuditCircleFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_IS_NEED_TOOLBAR, isNeedToolBar);
        circleListFragment.setArguments(bundle);
        return circleListFragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        adapter.addItemViewDelegate(new CircleMineAuditListItem(getContext(), this));
        return adapter;
    }

    @Override
    public CircleClient.MineCircleType getMineCircleType() {
        return CircleClient.MineCircleType.AUDIT;
    }
}
