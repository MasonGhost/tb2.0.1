package com.zhiyicx.thinksnsplus.modules.circle.mine;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.modules.circle.list.ChannelListFragmentAdapter;

/**
 * @Describe 我的圈子列表
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class MyGroupFragment extends TSListFragment<MyGroupContract.Presenter, GroupInfoBean> implements MyGroupContract.View{

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ChannelListFragmentAdapter(getContext()
                , R.layout.item_channel_list, mListDatas, mPresenter);
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.group_mine);
    }

    @Override
    public void updateGroupJoinState(int position, GroupInfoBean groupInfoBean) {
        if (groupInfoBean.getIs_member() == 0){
            mListDatas.remove(position);
            refreshData();
        }
    }
}
