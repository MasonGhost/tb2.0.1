package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelike.DaggerMessageLikeComponent;

/**
 * @Author Jliuer
 * @Date 2017/7/5/21:25
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MessageReviewActivity extends TSActivity<MessageReviewPresenter,MessageReviewFragment> {

    @Override
    protected void componentInject() {
       DaggerMessageReviewComponent
               .builder()
               .appComponent(AppApplication.AppComponentHolder.getAppComponent())
               .messageReviewPresenterModule(new MessageReviewPresenterModule(mContanierFragment))
               .build()
               .inject(this);
    }
    @Override
    protected MessageReviewFragment getFragment() {
        return MessageReviewFragment.newInstance();
    }

}
