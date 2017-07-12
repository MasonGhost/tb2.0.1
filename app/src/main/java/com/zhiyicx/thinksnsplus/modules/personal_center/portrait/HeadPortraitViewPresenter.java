package com.zhiyicx.thinksnsplus.modules.personal_center.portrait;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class HeadPortraitViewPresenter extends BasePresenter<HeadPortraitViewContract.Repository, HeadPortraitViewContract.View>
        implements HeadPortraitViewContract.Presenter{

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public HeadPortraitViewPresenter(HeadPortraitViewContract.Repository repository, HeadPortraitViewContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public UserInfoBean getCurrentUser(long user_id) {
        return mUserInfoBeanGreenDao.getSingleDataFromCache(user_id);
    }
}
