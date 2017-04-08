package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_DYNAMIC_TO_LIST;

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
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    DynamicDetailBeanGreenDaoImpl mDynamicDetailBeanGreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public SendDynamicPresenter(SendDynamicContract.Repository repository, SendDynamicContract.View rootView) {
        super(repository, rootView);
        mDynamicBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent()
                .dynamicBeanGreenDao();
        mDynamicDetailBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent()
                .dynamicDetailBeanGreenDao();
    }

    @Override
    public void sendDynamic(DynamicBean dynamicBean) {
        if (dynamicBean.getFeed().getStorages() == null) { // 当没有图片的时候，给一个占位数组
            dynamicBean.getFeed().setStorages(new ArrayList<ImageBean>());
        }
        dynamicBean.setComments(new ArrayList<DynamicCommentBean>());
        dynamicBean.setState(DynamicBean.SEND_ING);
        dynamicBean.setUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(dynamicBean.getUser_id()));
        // 将动态信息存入数据库
        mDynamicBeanGreenDao.insertOrReplace(dynamicBean);
        mDynamicDetailBeanGreenDao.insertOrReplace(dynamicBean.getFeed());
        // 发送动态
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_DYNAMIC);
        HashMap<String, Object> params = new HashMap<>();
        // feed_mark作为参数
        params.put("params", dynamicBean.getFeed_mark());
        backgroundRequestTaskBean.setParams(params);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_LIST);
        mRootView.sendDynamicComplete();// 发送动态放到后台任务处理，关闭当前的动态发送页面

    }
}
