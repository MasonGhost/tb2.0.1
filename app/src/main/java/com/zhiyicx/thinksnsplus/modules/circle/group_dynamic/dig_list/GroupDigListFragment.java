package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */

public class GroupDigListFragment extends TSListFragment<GroupDigListContract.Presenter, DynamicDigListBean> implements GroupDigListContract.View{

    public static final String GROUP_DIG_LIST_DATA = "dig_list_data";// 传入点赞榜的数据

    private GroupDynamicListBean mGroupDynamicListBean;

    public static GroupDigListFragment instance(Bundle bundle){
        GroupDigListFragment fragment = new GroupDigListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void upDataFollowState(int position) {
        refreshData(position);
    }

    @Override
    public GroupDynamicListBean getDynamicBean() {
        mGroupDynamicListBean = getArguments().getParcelable(GROUP_DIG_LIST_DATA);
        return mGroupDynamicListBean;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new GroupDigListAdapter(getContext(), mListDatas, mPresenter);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.dig_list);
    }
}
