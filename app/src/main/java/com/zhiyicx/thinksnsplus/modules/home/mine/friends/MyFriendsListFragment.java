package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.search.SearchFriendsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.OnClick;

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
        return new MyFriendsListAdapter(getContext(), mListDatas, mPresenter);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_my_friends_list;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected Long getMaxId(@NotNull List<UserInfoBean> data) {
        return (long) mListDatas.size();
    }

    @OnClick({R.id.tv_toolbar_left, R.id.tv_toolbar_right, R.id.tv_toolbar_center})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_toolbar_left:
                // 退出当前页面
                getActivity().finish();
                break;
            case R.id.tv_toolbar_right:
                // 进入找人页面
                Intent itFollow = new Intent(getActivity(), FindSomeOneContainerActivity.class);
                Bundle bundleFollow = new Bundle();
                itFollow.putExtras(bundleFollow);
                startActivity(itFollow);
                break;
            case R.id.tv_toolbar_center:
                // 进入搜索页面
                startActivity(new Intent(getActivity(), SearchFriendsActivity.class));
                break;
            default:
        }
    }
}
