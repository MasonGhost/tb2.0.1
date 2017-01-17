package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Describe  消息评论
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */

public class MessageCommentActivity extends TSActivity {


    @Override
    protected void componentInject() {
//       DaggerRegisterComponent
//               .builder()
//               .appComponent(AppApplication.AppComponentHolder.getAppComponent())
//               .registerPresenterModule(new RegisterPresenterModule((RegisterContract.View) mContanierFragment))
//               .build()
//               .inject(this);

    }
    @Override
    protected Fragment getFragment() {
        return MessageCommentFragment.newInstance();
    }

}
