package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */

public class AddInfoPresenter extends AppBasePresenter<AddInfoContract.View>
        implements AddInfoContract.Presenter {

    @Inject
    BaseInfoRepository mBaseInfoRepository;

    @Inject
    InfoTypeBeanGreenDaoImpl mInfoTypeBeanGreenDao;

    @Inject
    public AddInfoPresenter(AddInfoContract.View rootView) {
        super(rootView);
    }

    @Override
    public List<InfoTypeCatesBean> getInfoTypeBean() {
        return mInfoTypeBeanGreenDao.getAllCatesLists();
    }

    @Override
    public void getInfoType() {
        Subscription subscribe = mBaseInfoRepository.getInfoType()
                .subscribe(data -> {
                    for (InfoTypeCatesBean myCates : data.getMy_cates()) {
                        myCates.setIsMyCate(true);
                    }
                    mInfoTypeBeanGreenDao.updateSingleData(data);
                    mRootView.setInfoType(data.getMy_cates());
                }, throwable -> {

                });
        addSubscrebe(subscribe);
    }
}
