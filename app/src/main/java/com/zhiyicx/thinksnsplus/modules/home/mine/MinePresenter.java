package com.zhiyicx.thinksnsplus.modules.home.mine;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/9
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class MinePresenter extends BasePresenter<MineContract.Repository, MineContract.View> implements MineContract.Presenter {
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public MinePresenter(MineContract.Repository repository, MineContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void getUserInfoFromDB() {
        // 尝试从数据库获取当前用户的信息
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache((long) authBean.getUser_id());
        mRootView.setUserInfo(userInfoBean);
    }
}
