package com.zhiyicx.thinksnsplus.modules.tb.message;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBean;

/**
 * Created by lx on 2018/3/24.
 */

public class MessageListFragment extends TSListFragment<MessageListContract.Presenter, TbMessageBean>
        implements MessageListContract.View{

    public static MessageListFragment newInstance() {
        MessageListFragment messageListFragment = new MessageListFragment();
        return messageListFragment;
    }

    @Override
    public void setPresenter(MessageListContract.Presenter presenter) {

    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
