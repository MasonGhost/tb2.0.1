package com.zhiyicx.thinksnsplus.modules.system_conversation;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/04/26
 * @Contact master.jungle68@gmail.com
 */
public class SystemConversationFragment extends TSListFragment<SystemConversationContract.Presenter,SystemConversationBean> implements SystemConversationContract.View {

    public static SystemConversationFragment newInstance() {
        Bundle args = new Bundle();
        SystemConversationFragment fragment = new SystemConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.ts_helper);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

}
