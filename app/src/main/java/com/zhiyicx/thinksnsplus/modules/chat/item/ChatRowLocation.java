package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe 位置消息
 * @date 2018/1/9
 * @contact email:648129313@qq.com
 */

public class ChatRowLocation extends ChatBaseRow{

    private TextView mTvLocation;

    public ChatRowLocation(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter, userInfoBean);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.item_chat_list_receive_location : R.layout.item_chat_list_send_location, this);
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        mTvLocation = (TextView) findViewById(R.id.tv_location);
    }

    @Override
    protected void onSetUpView() {
        super.onSetUpView();
        EMLocationMessageBody locBody = (EMLocationMessageBody) message.getBody();
        mTvLocation.setText(locBody.getAddress());
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        super.onViewUpdate(msg);
    }
}
