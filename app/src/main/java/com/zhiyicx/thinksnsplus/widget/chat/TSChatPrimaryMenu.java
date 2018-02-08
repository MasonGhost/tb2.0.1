package com.zhiyicx.thinksnsplus.widget.chat;

import android.Manifest;
import android.content.Context;
import android.util.AttributeSet;

import com.hyphenate.easeui.widget.EaseChatPrimaryMenu;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.chat.call.TSEMHyphenate;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/4
 * @contact email:648129313@qq.com
 */
public class TSChatPrimaryMenu extends EaseChatPrimaryMenu {

    private RxPermissions mRxPermissions;

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
    protected void setModeVoice() {
        if (mRxPermissions == null) {
            mRxPermissions = new RxPermissions(TSEMHyphenate.getInstance().getTopActivity());
        }
        mRxPermissions
                .requestEach(Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 权限被允许
                        super.setModeVoice();
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 权限没有被彻底禁止
                    } else {
                        // 权限被彻底禁止
                    }
                });
    }

    @Override
    public int getBodyLayoutId() {
        // 重写 但是要记得id不能变哦
        return R.layout.view_chat_input_menu;
    }
}
