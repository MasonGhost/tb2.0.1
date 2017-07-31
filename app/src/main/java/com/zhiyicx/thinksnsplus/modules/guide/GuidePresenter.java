package com.zhiyicx.thinksnsplus.modules.guide;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.LaunchAdvertBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.WalletRepository;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */

public class GuidePresenter extends BasePresenter<GuideContract.Repository, GuideContract.View> implements GuideContract.Presenter {

    @Inject
    AuthRepository mIAuthRepository;
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    WalletRepository mWalletRepository;

    @Inject
    public GuidePresenter(GuideContract.Repository repository, GuideContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void checkLogin() {
        // 系统扩展配置信息处理
        mSystemRepository.getBootstrappersInfoFromServer();
        if (mIAuthRepository.isLogin()) {
            // TODO: 2017/2/10 刷新 Token 时间，过期前一天刷新
//        mIAuthRepository.refreshToken();
            // 钱包信息我也不知道在哪儿获取
            mWalletRepository.getWalletConfigWhenStart(mIAuthRepository.getAuthBean().getUser_id());
            mRootView.startActivity(HomeActivity.class);
        } else {
            mRootView.startActivity(LoginActivity.class);
        }
    }

    @Override
    public void getLaunchAdverts() {
        mRepository.getLaunchAdverts().subscribe(new BaseSubscribe<List<LaunchAdvertBean>>() {
            @Override
            protected void onSuccess(List<LaunchAdvertBean> data) {
                // 出入数据库
            }
        });
    }

    @Override
    public SystemConfigBean getAdvert() {
        return mSystemRepository.getBootstrappersInfoFromLocal();
    }
}

