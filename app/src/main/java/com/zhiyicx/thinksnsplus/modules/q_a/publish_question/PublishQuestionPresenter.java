package com.zhiyicx.thinksnsplus.modules.q_a.publish_question;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.QA_LIstInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class PublishQuestionPresenter extends AppBasePresenter<PublishQuestionContract.Repository, PublishQuestionContract.View>
        implements PublishQuestionContract.Presenter{

    @Inject
    public PublishQuestionPresenter(PublishQuestionContract.Repository repository, PublishQuestionContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<QA_LIstInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QA_LIstInfoBean> data, boolean isLoadMore) {
        return false;
    }
}
