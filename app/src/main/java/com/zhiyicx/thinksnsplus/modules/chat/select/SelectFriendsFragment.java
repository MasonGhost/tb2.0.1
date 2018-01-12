package com.zhiyicx.thinksnsplus.modules.chat.select;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class SelectFriendsFragment extends TSListFragment<SelectFriendsContract.Presenter, UserInfoBean>
        implements SelectFriendsContract.View{

    @Override
    public void onNetResponseSuccess(List<UserInfoBean> data, boolean isLoadMore) {

    }

    @Override
    public void onCacheResponseSuccess(List<UserInfoBean> data, boolean isLoadMore) {

    }

    @Override
    public void refreshData(List<UserInfoBean> datas) {

    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
