package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_DYNAMIC_TO_CHANNEL;
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
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;

    @Inject
    public SendDynamicPresenter(SendDynamicContract.Repository repository, SendDynamicContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void sendDynamic(DynamicBean dynamicBean) {
        if (dynamicBean.getFeed().getStorages() == null) { // 当没有图片的时候，给一个占位数组
            dynamicBean.getFeed().setStorages(new ArrayList<ImageBean>());
        }
        if (mRootView.hasTollVerify()) {
            mRootView.showSnackErrorMessage(mContext.getResources().getString(R.string.dynamic_send_toll_toll_verify));
            return;
        }
        if (mRootView.getTollMoney() != (int) mRootView.getTollMoney()) {
            mRootView.showSnackErrorMessage(mContext.getResources().getString(R.string.limit_monye));
            return;
        }
        SendDynamicDataBean sendDynamicDataBean = mRootView.getDynamicSendData();
        int dynamicBelong = sendDynamicDataBean.getDynamicBelong();
        dynamicBean.setComments(new ArrayList<DynamicCommentBean>());
        dynamicBean.setState(DynamicBean.SEND_ING);
        dynamicBean.setUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(dynamicBean.getUser_id()));
        // 发送动态
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_DYNAMIC);
        HashMap<String, Object> params = new HashMap<>();
        // feed_mark作为参数
        params.put("params", dynamicBean.getFeed_mark());
        params.put("sendDynamicDataBean", sendDynamicDataBean);
        switch (dynamicBelong) {
            case SendDynamicDataBean.MORMAL_DYNAMIC:
                // 将动态信息存入数据库
                mDynamicBeanGreenDao.insertOrReplace(dynamicBean);
                mDynamicDetailBeanGreenDao.insertOrReplace(dynamicBean.getFeed());
                mDynamicToolBeanGreenDao.insertOrReplace(dynamicBean.getTool());
                EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_LIST);
                break;
            case SendDynamicDataBean.CHANNEL_DYNAMIC:
                // 没有存入数据库，所以通过map传到后台
                params.put("dynamicbean", dynamicBean);
                // 发送到频道，不做处理
                EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_CHANNEL);
                break;
            default:
        }
        backgroundRequestTaskBean.setParams(params);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        mRootView.sendDynamicComplete();// 发送动态放到后台任务处理，关闭当前的动态发送页面

    }

    @Override
    public void sendDynamicV2(DynamicBean dynamicBean) {
        if (dynamicBean.getFeed().getStorages() == null) { // 当没有图片的时候，给一个占位数组
            dynamicBean.getFeed().setStorages(new ArrayList<ImageBean>());
        }
        if (mRootView.hasTollVerify()) {
            mRootView.showSnackErrorMessage(mContext.getResources().getString(R.string.dynamic_send_toll_toll_verify));
            return;
        }

        if (mRootView.getTollMoney() != (int) mRootView.getTollMoney()) {
            mRootView.showSnackErrorMessage(mContext.getResources().getString(R.string.limit_monye));
            return;
        }

        SendDynamicDataBean sendDynamicDataBean = mRootView.getDynamicSendData();
        int dynamicBelong = sendDynamicDataBean.getDynamicBelong();
        dynamicBean.setComments(new ArrayList<DynamicCommentBean>());
        dynamicBean.setState(DynamicBean.SEND_ING);
        dynamicBean.setUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(dynamicBean.getUser_id()));

        // 发送动态 V2 所需要的数据
        SendDynamicDataBeanV2 sendDynamicDataBeanV2 = SendDynamicDataBeanV2.DynamicDetailBean2SendDynamicDataBeanV2(dynamicBean);
        sendDynamicDataBeanV2.setStorage_task(mRootView.packageDynamicStorageDataV2());

        // 建立 task
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_DYNAMIC_V2);
        HashMap<String, Object> params = new HashMap<>();
        // feed_mark作为参数
        params.put("params", dynamicBean.getFeed_mark());
        params.put("sendDynamicDataBean", sendDynamicDataBeanV2);

        switch (dynamicBelong) {
            case SendDynamicDataBean.MORMAL_DYNAMIC:
                // 将动态信息存入数据库
                mDynamicBeanGreenDao.insertOrReplace(dynamicBean);
                mDynamicDetailBeanGreenDao.insertOrReplace(dynamicBean.getFeed());
                mDynamicToolBeanGreenDao.insertOrReplace(dynamicBean.getTool());
                EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_LIST);
                break;
            case SendDynamicDataBean.CHANNEL_DYNAMIC:
                // 没有存入数据库，所以通过map传到后台
                params.put("dynamicbean", dynamicBean);
                // 发送到频道，不做处理
                EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_CHANNEL);
                break;
            default:
        }

        backgroundRequestTaskBean.setParams(params);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        mRootView.sendDynamicComplete();// 发送动态放到后台任务处理，关闭当前的动态发送页面
    }
}
