package com.zhiyicx.thinksnsplus.modules.dynamic;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class SendDynamicPresenter extends BasePresenter<SendDynamicContract.Repository, SendDynamicContract.View>
        implements SendDynamicContract.Presenter {

    @Inject
    IUploadRepository mIUploadRepository;

    @Inject
    public SendDynamicPresenter(SendDynamicContract.Repository repository, SendDynamicContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void sendDynamic() {
      /*  BackgroundRequestTaskBean backgroundRequestTaskBean=new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType();*/
    }
}
