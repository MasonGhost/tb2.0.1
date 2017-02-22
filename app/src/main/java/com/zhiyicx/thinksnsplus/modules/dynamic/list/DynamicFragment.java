package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListRecycleItem;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 消息评论
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class DynamicFragment extends TSListFragment<DynamicContract.Presenter, DynamicBean> implements DynamicContract.View {

    @Inject
    DynamicPresenter mDynamicPresenter;  // 仅用于构造

    private List<DynamicBean> mDynamicBeens = new ArrayList<>();

    public static DynamicFragment newInstance() {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected MultiItemTypeAdapter<DynamicBean> getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mDynamicBeens);
        adapter.addItemViewDelegate(new DynamicListRecycleItem());
        return adapter;
    }


    @Override
    protected void initData() {
        DaggerDynamicComponent // 在 super.initData();之前，因为initdata 会使用到 presenter
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicPresenterModule(new DynamicPresenterModule(this))
                .build().inject(this);

        super.initData();
    }

    /**
     * 进入聊天页
     *
     * @param messageItemBean
     */
    private void toChat(MessageItemBean messageItemBean) {
        Intent to = new Intent(getActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ChatFragment.BUNDLE_MESSAGEITEMBEAN, messageItemBean);
        to.putExtras(bundle);
        startActivity(to);
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter() {
        Intent to = new Intent(getActivity(), UserInfoActivity.class);
        startActivity(to);
    }

    @Override
    public void setPresenter(DynamicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        ToastUtils.showToast(message);
    }
}
