package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentFragment;

/**
 * @Describe  消息赞
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */

public class MessageLikeActivity extends TSActivity {


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
