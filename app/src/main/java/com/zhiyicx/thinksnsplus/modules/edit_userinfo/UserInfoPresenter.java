package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.imsdk.utils.common.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public class UserInfoPresenter extends BasePresenter<UserInfoContract.Repository, UserInfoContract.View> implements UserInfoContract.Presenter {

    @Inject
    public UserInfoPresenter(UserInfoContract.Repository repository, UserInfoContract.View rootView) {
        super(repository, rootView);
    }

    /**
     * 将Presenter从传入fragment
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void getAreaData() {
        mRepository.getAreaList()
                .subscribe(new Action1<ArrayList<AreaBean>>() {
                    @Override
                    public void call(ArrayList<AreaBean> areaBeen) {
                        ArrayList<ArrayList<AreaBean>> areaBeenChildList = new ArrayList<ArrayList<AreaBean>>();
                        for (AreaBean areaBean : areaBeen) {
                            areaBeenChildList.add(areaBean.getChild());
                        }
                        mRootView.setAreaData(areaBeen, areaBeenChildList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        com.zhiyicx.common.utils.log.LogUtils.e(throwable, mContext.getString(R.string.data_load_error));
                    }
                });
    }
}
