package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.UserMessage;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.LiveChatFollowListHolder;
import com.zhiyicx.zhibolibrary.ui.holder.LiveChatTextListHolder;
import com.zhiyicx.zhibolibrary.ui.holder.LiveChatTipListHolder;

import java.util.List;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class LiveChatListAdapter extends MuiltListAdapter<UserMessage> {
    public LiveChatListAdapter(List<UserMessage> infos) {
        super(infos);
    }

    @Override
    public ZBLBaseHolder<UserMessage> getHolder(View v, int viewType) {
        switch (viewType) {
            case TYPE_ITEM_TEXT:
                return new LiveChatTextListHolder(v);
            case TYPE_ITEM_FLLOW:
                return new LiveChatFollowListHolder(v);
            default:
                return new LiveChatTipListHolder(v);

        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.zb_recycle_item_live_chat;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
