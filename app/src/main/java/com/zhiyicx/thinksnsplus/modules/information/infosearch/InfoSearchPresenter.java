package com.zhiyicx.thinksnsplus.modules.information.infosearch;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListDataBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoSearchPresenter extends AppBasePresenter<SearchContract
        .View> implements SearchContract.Presenter {

    @Inject
    InfoListDataBeanGreenDaoImpl mInfoListDataBeanGreenDao;

    @Inject
    BaseInfoRepository mBaseInfoRepository;

    @Inject
    public InfoSearchPresenter(SearchContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription subscription = mBaseInfoRepository.getInfoListV2("", mRootView.getKeyWords(), maxId, mRootView.getPage(), 0)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<InfoListDataBean>>() {
                    @Override
                    protected void onSuccess(List<InfoListDataBean> data) {
                        // 搜索暂时不存，因为置顶也会搜出来 但是他不是置顶 如果后期要存 取出来再update吧
//                        if (!data.isEmpty()){
//                            mInfoListDataBeanGreenDao.saveMultiData(data);
//                        }
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoListDataBean> data, boolean isLoadMore) {
        return false;
    }

}
