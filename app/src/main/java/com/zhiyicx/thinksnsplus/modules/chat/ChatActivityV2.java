package com.zhiyicx.thinksnsplus.modules.chat;

import android.support.v4.app.Fragment;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.base.TSActivity;

import java.util.List;

/**
 * @author Catherine
 * @describe 聊天详情页
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatActivityV2 extends TSActivity{

    @Override
    protected Fragment getFragment() {
        return new ChatFragmentV2().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {

    }
}
