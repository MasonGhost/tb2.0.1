package com.zhiyicx.thinksnsplus.modules.dynamic;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;

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
    public void sendDynamic(HashMap<String, Object> params) {
        // 发送动态
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_DYNAMIC);
        backgroundRequestTaskBean.setParams(params);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }
}
