package com.zhiyicx.thinksnsplus.modules.tb.privacy;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2018/03/01/15:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PrivacyPresenter extends AppBasePresenter<PrivacyContract.View> implements PrivacyContract.Presenter {
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public PrivacyPresenter(PrivacyContract.View rootView) {
        super(rootView);
    }


    @Override
    public void changeRankStatus(boolean rank) {
        addSubscrebe(mUserInfoRepository.changeRankStatus()
                .subscribe(s -> mRootView.onChangeRankStatus(!rank)));
    }

    @Override
    public void getRankStatus() {
        addSubscrebe(mUserInfoRepository.getRankStatus()
                .subscribe(s -> {
                    try {
                        mRootView.onGetRankStatus(new JSONObject(s).optInt("status"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }));
    }
}
