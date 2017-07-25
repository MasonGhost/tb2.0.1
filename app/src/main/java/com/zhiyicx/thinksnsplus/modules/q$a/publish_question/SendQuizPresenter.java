package com.zhiyicx.thinksnsplus.modules.q$a.publish_question;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class SendQuizPresenter extends BasePresenter<SendQuizContract.Repository, SendQuizContract.View>
        implements SendQuizContract.Presenter{

    @Inject
    public SendQuizPresenter(SendQuizContract.Repository repository, SendQuizContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void postImage() {

    }
}
