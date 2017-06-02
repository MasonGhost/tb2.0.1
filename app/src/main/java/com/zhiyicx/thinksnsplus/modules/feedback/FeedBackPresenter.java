package com.zhiyicx.thinksnsplus.modules.feedback;

import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/02/17:23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class FeedBackPresenter extends AppBasePresenter<FeedBackContract.Repository, FeedBackContract.View>
        implements FeedBackContract.Presenter {

    @Inject
    public FeedBackPresenter(FeedBackContract.Repository repository, FeedBackContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void submitFeedBack(String content, String contract) {
        if (!(RegexUtils.isMobileExact(contract) || RegexUtils.isEmail(content))) {
            mRootView.showWithdrawalsInstructionsPop();
        }else{

        }
    }
}
