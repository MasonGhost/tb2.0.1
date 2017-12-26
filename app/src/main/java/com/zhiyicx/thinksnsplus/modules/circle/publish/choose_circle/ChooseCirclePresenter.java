package com.zhiyicx.thinksnsplus.modules.circle.publish.choose_circle;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.local.CircleInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.circle.mine.joined.BaseCircleListContract;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/12/15/18:09
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ChooseCirclePresenter extends AppBasePresenter<
        ChooseCircleContract.View>
        implements ChooseCircleContract.Presenter {

    @Inject
    CircleInfoGreenDaoImpl mCircleInfoGreenDao;
    @Inject
    BaseCircleRepository mBaseCircleRepository;

    @Inject
    public ChooseCirclePresenter(ChooseCircleContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getMyJoinedCircleList() {
        Subscription subscribe = mBaseCircleRepository.getMyJoinedCircle(Integer.MAX_VALUE
                , 0, mRootView.getMineCircleType().value)
                .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {
                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        mRootView.onNetResponseSuccess(data);
                        mCircleInfoGreenDao.saveMultiData(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
        addSubscrebe(subscribe);
    }
}
