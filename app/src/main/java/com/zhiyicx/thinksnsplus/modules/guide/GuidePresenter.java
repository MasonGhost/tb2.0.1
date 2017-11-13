package com.zhiyicx.thinksnsplus.modules.guide;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.config.AdvertConfig;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.RealAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.WalletRepository;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */

public class GuidePresenter extends BasePresenter<GuideContract.Repository, GuideContract.View>
        implements GuideContract.Presenter {

    @Inject
    AuthRepository mIAuthRepository;
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    WalletRepository mWalletRepository;
    @Inject
    AllAdvertListBeanGreenDaoImpl mAllAdvertLIstBeanGreendo;
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
        Subscription subscribe = mRepository.getLaunchAdverts()
                .observeOn(Schedulers.io())
                .flatMap(allAdverListBeen -> {
                    List<Object> ids = new ArrayList<>();
                    for (AllAdverListBean adverListBean : allAdverListBeen) {
                        ids.add(adverListBean.getId());
                    }
                    return mRepository.getAllRealAdverts(ids)
                            .flatMap(realAdvertListBeen -> {
                                for (RealAdvertListBean boot : realAdvertListBeen) {
                                    if (boot.getType().equals(AdvertConfig.APP_IMAGE_TYPE_ADVERT)) {
                                        String url=boot.getAdvertFormat().getImage().getImage();
                                        LogUtils.d("getLaunchAdverts:::"+url);
                                        Glide.with(mContext)
                                                .load(url)
                                                .downloadOnly(DeviceUtils
                                                                .getScreenWidth(mContext),
                                                        DeviceUtils.getScreenHeight(mContext));
                                    }
                                }
                                mRealAdvertListBeanGreenDao.saveMultiData(realAdvertListBeen);
                                if (realAdvertListBeen.isEmpty()) {
                                    mRealAdvertListBeanGreenDao.clearTable();
                                }
                                return Observable.just(allAdverListBeen);
                            });
//                        Observable.merge(adverts).subscribe(realAdvertListBeen -> {
//                            mRealAdvertListBeanGreenDao.saveMultiData(realAdvertListBeen);
//                        });
//                        return Observable.just(allAdverListBeen);
                })
                .subscribe(new BaseSubscribeForV2<List<AllAdverListBean>>() {
                    @Override
                    protected void onSuccess(List<AllAdverListBean> data) {
                        // 出入数据库
                        mAllAdvertLIstBeanGreendo.saveMultiData(data);
                        if (data.isEmpty()) {
                            mAllAdvertLIstBeanGreendo.clearTable();
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public List<RealAdvertListBean> getBootAdvert() {
        AllAdverListBean boot = mAllAdvertLIstBeanGreendo.getBootAdvert();
        if (boot != null && boot.getMRealAdvertListBeen() != null && !boot.getMRealAdvertListBeen().isEmpty()) {
            return boot.getMRealAdvertListBeen();
        }
        return null;
    }

    @Override
    public SystemConfigBean getAdvert() {
        return mSystemRepository.getBootstrappersInfoFromLocal();
    }
}

