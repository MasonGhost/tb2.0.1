package com.zhiyicx.thinksnsplus.modules.channel.mine;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.modules.channel.list.ChannelListFragmentAdapter;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/25
 * @contact email:648129313@qq.com
 */

public class MyGroupFragment extends TSListFragment<MyGroupContract.Presenter, GroupInfoBean> implements MyGroupContract.View{

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null/*new ChannelListFragmentAdapter(getContext()
                , R.layout.item_channel_list, mListDatas, mPresenter)*/;
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
}
