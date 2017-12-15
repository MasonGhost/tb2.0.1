package com.zhiyicx.thinksnsplus.modules.circle.main;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleJoinedBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.VerifiedBean;
import com.zhiyicx.thinksnsplus.data.source.local.CircleInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.repository.CertificationDetailRepository;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleFragment;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.BaseCircleItem;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * @author Jliuer
 * @Date 2017/11/14/11:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainPresenter extends AppBasePresenter<CircleMainContract.Repository, CircleMainContract.View>
        implements CircleMainContract.Presenter {

    @Inject
    CircleInfoGreenDaoImpl mCircleInfoGreenDao;
    @Inject
    UserCertificationInfoGreenDaoImpl mUserCertificationInfoDao;
    @Inject
    CertificationDetailRepository mCertificationDetailRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;


    @Inject
    public CircleMainPresenter(CircleMainContract.Repository repository, CircleMainContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        Subscription subscription = Observable.zip(mRepository.getCircleCount(),
                mRepository.getMyJoinedCircle(CircleMainFragment.DATALIMIT, 0, CircleClient.MineCircleType.JOIN.value),
                mRepository.getRecommendCircle(CircleMainFragment.DATALIMIT, 0),
                (integerBaseJsonV2, myJoinedCircle, recommendCircle) -> {

                    mRootView.updateCircleCount(integerBaseJsonV2.getData());
                    CircleInfo moreJoined = new CircleInfo();
                    moreJoined.setName(mContext.getString(R.string.joined_group));
                    moreJoined.setSummary(mContext.getString(R.string.more_group));
                    moreJoined.setId(BaseCircleItem.MYJOINEDCIRCLE);
                    CircleInfo changeCircle = new CircleInfo();
                    changeCircle.setName(mContext.getString(R.string.recommend_group));
                    changeCircle.setSummary(mContext.getString(R.string.exchange_group));
                    changeCircle.setId(BaseCircleItem.RECOMMENDCIRCLE);
                    myJoinedCircle.add(0, moreJoined);
                    myJoinedCircle.add(changeCircle);
                    mRootView.setJoinedCircles(new ArrayList<>(myJoinedCircle));
                    myJoinedCircle.addAll(recommendCircle);
                    return myJoinedCircle;
                })
                .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {
                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });

        addSubscrebe(subscription);

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void getRecommendCircle() {
        Subscription subscription = mRepository.getRecommendCircle(CircleMainFragment.DATALIMIT, 0)
                .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {
                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        List<CircleInfo> subs = new ArrayList<>(mRootView.getJoinedCircles());
                        subs.addAll(data);
                        mRootView.getListDatas().clear();
                        mRootView.getListDatas().addAll(subs);
                        mRootView.refreshData();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void dealCircleJoinOrExit(int position, CircleInfo circleInfo) {
        if (circleInfo.getAudit() != 1) {
            mRootView.showSnackErrorMessage(mContext.getString(R.string.reviewing_circle));
            return;
        }
        boolean isJoined = circleInfo.getJoined() != null;

        mRepository.dealCircleJoinOrExit(circleInfo)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.showSnackSuccessMessage(data.getMessage().get(0));
                        if (isJoined) {
                            circleInfo.setJoined(null);
                            circleInfo.setUsers_count(circleInfo.getUsers_count() - 1);
                        } else {
                            if (CreateCircleFragment.MODE_PAID.equals(circleInfo.getMode())) {

                                return;
                            }
                            circleInfo.setJoined(new CircleJoinedBean());
                            circleInfo.setUsers_count(circleInfo.getUsers_count() + 1);
                        }
                        mCircleInfoGreenDao.updateSingleData(circleInfo);
                        mRootView.refreshData();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
    }

    /**
     * 检查认证状态信息
     */
    @Override
    public void checkCertification() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        UserCertificationInfo userCertificationInfo = mUserCertificationInfoDao.getInfoByUserId();
        if (getSystemConfigBean() != null && getSystemConfigBean().getCircleGroup() != null && userCertificationInfo != null &&
                userCertificationInfo.getStatus() == UserCertificationInfo.CertifyStatusEnum.PASS.value) {
            mRootView.setUserCertificationInfo(userCertificationInfo);
            return;
        }

        Subscription subscribe = Observable.zip(mSystemRepository.getBootstrappersInfo(), mCertificationDetailRepository.getCertificationInfo(),
                (systemConfigBean, userCertificationInfo1) -> {
                    Map data = new HashMap();
                    data.put("systemConfigBean", systemConfigBean);
                    data.put("userCertificationInfo", userCertificationInfo1);
                    return data;
                })
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("信息加载中..."))
                .doAfterTerminate(() -> mRootView.dismissSnackBar())
                .subscribe(new BaseSubscribeForV2<Map>() {
                    @Override
                    protected void onSuccess(Map zipData) {
                        UserCertificationInfo data = (UserCertificationInfo) zipData.get("userCertificationInfo");
                        SystemConfigBean systemConfigBean = (SystemConfigBean) zipData.get("systemConfigBean");
                        mSystemRepository.saveComponentStatus(systemConfigBean, mContext);
                        mUserCertificationInfoDao.saveSingleData(data);
                        if (userInfoBean != null) {
                            if (userInfoBean.getVerified() != null) {
                                userInfoBean.getVerified().setStatus((int) data.getStatus());
                            } else {
                                VerifiedBean verifiedBean = new VerifiedBean();
                                verifiedBean.setStatus((int) data.getStatus());
                                userInfoBean.setVerified(verifiedBean);
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EventBusTagConfig.EVENT_UPDATE_CERTIFICATION_SUCCESS, data);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_CERTIFICATION_SUCCESS);
                        mUserInfoBeanGreenDao.updateSingleData(userInfoBean);
                        mRootView.setUserCertificationInfo(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackSuccessMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscribe);
    }
}
