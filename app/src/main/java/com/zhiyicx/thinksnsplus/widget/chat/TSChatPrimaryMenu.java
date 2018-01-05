package com.zhiyicx.thinksnsplus.widget.chat;

import android.content.Context;
import android.util.AttributeSet;

import com.hyphenate.easeui.widget.EaseChatPrimaryMenu;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/4
 * @contact email:648129313@qq.com
 */

public class TSChatPrimaryMenu extends EaseChatPrimaryMenu {

    public TSChatPrimaryMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TSChatPrimaryMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TSChatPrimaryMenu(Context context) {
        super(context);
    }

    @Override
    public int getBodyLayoutId() {
        // 重写 但是要记得id不能变哦
        return R.layout.view_chat_input_menu;
    }
}
