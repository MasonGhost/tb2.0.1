package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class MyJoinedCircleFragment extends BaseCircleListFragment {

    public static MyJoinedCircleFragment newInstance(boolean isNeedToolBar) {
        MyJoinedCircleFragment circleListFragment = new MyJoinedCircleFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_IS_NEED_TOOLBAR, isNeedToolBar);
        circleListFragment.setArguments(bundle);
        return circleListFragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.joined_group);
    }

    @Override
    public CircleClient.MineCircleType getMineCircleType() {
        return CircleClient.MineCircleType.JOIN;
    }
}
