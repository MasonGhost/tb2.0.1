package com.zhiyicx.zhibolibrary.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.UserMessage;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class LiveChatTipListHolder extends ZBLBaseHolder<UserMessage> {

    TextView mContentTV;


    public LiveChatTipListHolder(View itemView) {
        super(itemView);
        mContentTV = (TextView) itemView.findViewById(R.id.tv_live_chat_tip);
    }

    @Override
    public void setData(UserMessage data) {


        mContentTV.setText(data.msg.txt);


    }


}
