package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Describe  消息赞
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */

public class MessageLikeActivity extends TSActivity<MessageLikePresenter,MessageLikeFragment> {


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
    protected MessageLikeFragment getFragment() {
        return MessageLikeFragment.newInstance();
    }

}
