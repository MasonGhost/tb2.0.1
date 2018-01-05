package com.zhiyicx.thinksnsplus.widget.chat;

import android.content.Context;
import android.util.AttributeSet;

import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatPrimaryMenuBase;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/5
 * @contact email:648129313@qq.com
 */

public class TSChatInputMenu extends EaseChatInputMenu {

    public TSChatInputMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TSChatInputMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TSChatInputMenu(Context context) {
        super(context);
    }

    @Override
    public EaseChatPrimaryMenuBase setPrimaryMenuView() {
        return (TSChatPrimaryMenu)layoutInflater.inflate(R.layout.view_ts_chat_primary_menu, null);
    }
}
