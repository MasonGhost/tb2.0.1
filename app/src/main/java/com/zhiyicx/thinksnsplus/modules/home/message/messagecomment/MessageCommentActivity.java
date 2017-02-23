<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 消息评论
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */

public class MessageCommentActivity extends TSActivity<MessageCommentPresenter, MessageCommentFragment> {


    @Override
    protected void componentInject() {
        DaggerMessageCommentComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messageCommentPresenterModule(new MessageCommentPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected MessageCommentFragment getFragment() {
        return MessageCommentFragment.newInstance();
    }

}
=======
package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 消息评论
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */

public class MessageCommentActivity extends TSActivity<MessageCommentPresenter, MessageCommentFragment> {


    @Override
    protected void componentInject() {
        DaggerMessageCommentComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messageCommentPresenterModule(new MessageCommentPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected MessageCommentFragment getFragment() {
        return MessageCommentFragment.newInstance();
    }

}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
