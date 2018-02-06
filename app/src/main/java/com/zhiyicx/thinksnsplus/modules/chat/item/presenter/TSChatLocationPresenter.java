package com.zhiyicx.thinksnsplus.modules.chat.item.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.presenter.EaseChatLocationPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatRowLocation;
import com.zhiyicx.thinksnsplus.modules.chat.location.SendLocationActivity;

import static com.zhiyicx.thinksnsplus.modules.chat.location.SendLocationFragment.BUNDLE_LOCATION_ADDRESS;
import static com.zhiyicx.thinksnsplus.modules.chat.location.SendLocationFragment.BUNDLE_LOCATION_LATITUDE;
import static com.zhiyicx.thinksnsplus.modules.chat.location.SendLocationFragment.BUNDLE_LOCATION_LONGITUDE;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/9
 * @contact email:648129313@qq.com
 */

public class TSChatLocationPresenter extends EaseChatLocationPresenter{

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        return new ChatRowLocation(cxt, message, position, adapter, userInfoBean);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        EMLocationMessageBody locBody = (EMLocationMessageBody) message.getBody();
        Intent intent = new Intent(getContext(), SendLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble(BUNDLE_LOCATION_LATITUDE, locBody.getLatitude());
        bundle.putDouble(BUNDLE_LOCATION_LONGITUDE, locBody.getLongitude());
        bundle.putString(BUNDLE_LOCATION_ADDRESS, locBody.getAddress());
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
}
