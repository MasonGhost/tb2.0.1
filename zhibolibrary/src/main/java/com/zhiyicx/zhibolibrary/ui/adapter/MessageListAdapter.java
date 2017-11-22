package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.model.entity.UserMessage;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.LiveChatTextListHolder;
import com.zhiyicx.zhibolibrary.ui.holder.LiveChatTipListHolder;

import java.util.List;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class MessageListAdapter extends MuiltChatListAdapter<UserMessage> {
    public MessageListAdapter(List<UserMessage> infos) {
        super(infos);
    }

    @Override
    public ZBLBaseHolder<UserMessage> getHolder(View v, int viewType) {
        switch (viewType) {
            case TYPE_ITEM_ME:
                return new LiveChatTextListHolder(v);
            case TYPE_ITEM_OTHER:
            default:
                return new LiveChatTipListHolder(v);

        }

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
