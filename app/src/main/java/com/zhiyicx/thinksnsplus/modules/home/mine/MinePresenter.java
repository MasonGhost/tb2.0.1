package com.zhiyicx.thinksnsplus.modules.home.mine;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import org.simple.eventbus.Subscriber;

import java.util.List;

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
    FlushMessageBeanGreenDaoImpl mFlushMessageBeanGreenDao;

    @Inject
    public MinePresenter(MineContract.Repository repository, MineContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void getUserInfoFromDB() {
        // 尝试从数据库获取当前用户的信息
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (authBean != null) {
            UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache((long) authBean.getUser_id());
            mRootView.setUserInfo(userInfoBean);
            FlushMessages flushMessages = mFlushMessageBeanGreenDao.getFlushMessgaeByKey(ApiConfig.FLUSHMESSAGES_KEY_FOLLOWS);
            if (flushMessages != null) {
                mRootView.setNewFollowTip(flushMessages.getCount());
            } else {
                mRootView.setNewFollowTip(0);
            }
        }
    }

    /**
     * 用户信息在后台更新后，在该处进行刷新，这儿获取的是自己的用户信息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_USERINFO_UPDATE)
    public void upDataUserInfo(List<UserInfoBean> data) {
        com.zhiyicx.common.utils.log.LogUtils.i("upDataUserInfo-->" + data.toString());
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (data != null) {
            for (UserInfoBean userInfoBean : data) {
                if (userInfoBean.getUser_id() == authBean.getUser_id()) {
                    mRootView.setUserInfo(userInfoBean);
                    break;
                }
            }
        }
    }

}
