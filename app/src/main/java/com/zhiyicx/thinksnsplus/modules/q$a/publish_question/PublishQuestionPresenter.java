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
public class PublishQuestionPresenter extends BasePresenter<PublishQuestionContract.Repository, PublishQuestionContract.View>
        implements PublishQuestionContract.Presenter{

    @Inject
    public PublishQuestionPresenter(PublishQuestionContract.Repository repository, PublishQuestionContract.View rootView) {
        super(repository, rootView);
    }

}
