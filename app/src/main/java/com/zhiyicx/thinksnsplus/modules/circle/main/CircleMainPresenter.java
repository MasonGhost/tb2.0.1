package com.zhiyicx.thinksnsplus.modules.circle.main;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.BaseCircleItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/11/14/11:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainPresenter extends AppBasePresenter<CircleMainContract.Repository, CircleMainContract.View>
        implements CircleMainContract.Presenter {


    @Inject
    public CircleMainPresenter(CircleMainContract.Repository repository, CircleMainContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        Observable.zip(mRepository.getCircleCount(),
                mRepository.getMyJoinedCircle(CircleMainFragment.DATALIMIT, 0),
                mRepository.getRecommendCircle(CircleMainFragment.DATALIMIT, 0),
                (integerBaseJsonV2, myJoinedCircle, recommendCircle) -> {

                    mRootView.updateCircleCount(integerBaseJsonV2.getData());
                    CircleInfo moreJoined = new CircleInfo();
                    moreJoined.setName("我加入的");
                    moreJoined.setId(BaseCircleItem.MYJOINEDCIRCLE);
                    CircleInfo changeCircle = new CircleInfo();
                    changeCircle.setName("热门推荐");
                    changeCircle.setId(BaseCircleItem.RECOMMENDCIRCLE);
                    myJoinedCircle.add(0, moreJoined);
                    recommendCircle.add(0, changeCircle);
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

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void getRecommendCircle() {
        mRepository.getRecommendCircle(CircleMainFragment.DATALIMIT, 0)
                .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {
                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        mRootView.refreshData();
                    }
                });
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        return false;
    }


}
