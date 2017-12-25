package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Catherine
 * @describe 我的好友列表 有搜索和跳转聊天的功能
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */

public class MyFriendsListFragment extends TSListFragment<MyFriendsListContract.Presenter, UserInfoBean>
        implements MyFriendsListContract.View{

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MyFriendsListAdapter(getContext(), mListDatas);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_my_friends);
    }

    @Override
    protected Long getMaxId(@NotNull List<UserInfoBean> data) {
        return (long) mListDatas.size();
    }
}
