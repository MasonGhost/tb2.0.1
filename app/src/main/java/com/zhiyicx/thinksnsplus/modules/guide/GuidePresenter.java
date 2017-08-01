package com.zhiyicx.thinksnsplus.modules.guide;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertLIstBeanGreendoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.RealAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.WalletRepository;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

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
    AllAdvertLIstBeanGreendoImpl mAllAdvertLIstBeanGreendo;
    @Inject
    RealAdvertListBeanGreenDaoImpl mRealAdvertListBeanGreenDao;

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
        mRepository.getLaunchAdverts()
                .flatMap(new Func1<List<AllAdverListBean>, Observable<List<AllAdverListBean>>>() {
                    @Override
                    public Observable<List<AllAdverListBean>> call(List<AllAdverListBean> allAdverListBeen) {
                        List<Observable<List<RealAdvertListBean>>> ids=new ArrayList<>();
                        for (AllAdverListBean adverListBean:allAdverListBeen){
                            ids.add(mRepository.getRealAdverts(adverListBean.getId().intValue()));
                        }
                        Observable.merge(ids).subscribe(realAdvertListBeen -> {
                            mRealAdvertListBeanGreenDao.saveMultiData(realAdvertListBeen);
                        });
                        return Observable.just(allAdverListBeen);
                    }
                })
                .subscribe(new BaseSubscribeForV2<List<AllAdverListBean>>() {
            @Override
            protected void onSuccess(List<AllAdverListBean> data) {
                // 出入数据库
                mAllAdvertLIstBeanGreendo.saveMultiData(data);
            }
        });
    }

    @Override
    public AllAdverListBean getBootAdvert() {
        return mAllAdvertLIstBeanGreendo.getBootAdvert();
    }

    @Override
    public SystemConfigBean getAdvert() {
        return mSystemRepository.getBootstrappersInfoFromLocal();
    }
}

