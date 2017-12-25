package com.zhiyicx.thinksnsplus.modules.home.mine.friends.search;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */

public class SearchFriendsFragment extends TSListFragment<SearchFriendsContract.Presenter, UserInfoBean>
        implements SearchFriendsContract.View{

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_search;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }
}
