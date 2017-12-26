package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IUploadRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
public class SendDynamicPresenter extends AppBasePresenter< SendDynamicContract.View>
        implements SendDynamicContract.Presenter {

    @Inject
    IUploadRepository mIUploadRepository;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;

    @Inject
    public SendDynamicPresenter( SendDynamicContract.View rootView) {
        super( rootView);
    }

    @Override
    public void sendGroupDynamic(GroupDynamicListBean dynamicBean) {
        if (dynamicBean.getImages() == null) { // 当没有图片的时候，给一个占位数组
            dynamicBean.setImages(new ArrayList<>());
        }

//        if (mRootView.getDynamicSendData().getDynamicBelong() == SendDynamicDataBean.GROUP_DYNAMIC
//                && (dynamicBean.getTitle() == null || dynamicBean.getTitle().isEmpty())) {
//            mRootView.initInstructionsPop(mContext.getString(R.string.instructions),
//                    mContext.getString(R.string.group_dynamic_send_must_has_title));
//            return;
//        }

        GroupSendDynamicDataBean groupSendDynamicDataBean = new GroupSendDynamicDataBean();
        groupSendDynamicDataBean.setGroup_post_mark(dynamicBean.getFeed_mark());
        groupSendDynamicDataBean.setGroup_id(dynamicBean.getGroup_id());
        groupSendDynamicDataBean.setTitle(dynamicBean.getTitle());
        groupSendDynamicDataBean.setContent(dynamicBean.getContent());
        mRootView.packageGroupDynamicStorageData(groupSendDynamicDataBean);

        dynamicBean.setNew_comments(new ArrayList<>());
        dynamicBean.setUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(dynamicBean.getUser_id()));
        // 发送动态
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_GROUP_DYNAMIC);
        HashMap<String, Object> params = new HashMap<>();

        params.put("sendDynamicDataBean", groupSendDynamicDataBean);
        // 没有存入数据库，所以通过map传到后台
        params.put("dynamicbean", dynamicBean);
        // 发送到频道，不做处理
        EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_CHANNEL);
        backgroundRequestTaskBean.setParams(params);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        mRootView.sendDynamicComplete();// 发送动态放到后台任务处理，关闭当前的动态发送页面

    }

    @Override
    public void sendDynamicV2(DynamicDetailBeanV2 dynamicBean) {
        if (dynamicBean.getImages() == null) { // 当没有图片的时候，给一个占位数组
            dynamicBean.setImages(new ArrayList<>());
        }
        // 发送动态 V2 所需要的数据
        dynamicBean.setAmount((long) PayConfig.gameCurrency2RealCurrency(dynamicBean.getAmount(), getRatio()));
        SendDynamicDataBeanV2 sendDynamicDataBeanV2 = SendDynamicDataBeanV2.DynamicDetailBean2SendDynamicDataBeanV2(dynamicBean);
        mRootView.packageDynamicStorageDataV2(sendDynamicDataBeanV2);

        if (mRootView.hasTollVerify()) {// 当设置图片收费时，最少配置一张图
            mRootView.initInstructionsPop(mContext.getString(R.string.instructions),
                    mContext.getString(R.string.dynamic_send_toll_toll_verify));
            return;
        }
        int contentLenght = ConvertUtils.stringLenghtDealForEmoji(sendDynamicDataBeanV2.getFeed_content());
        int wordLimit = getSystemConfigBean().getFeed().getLimit();
        wordLimit = wordLimit > 0 ? wordLimit : 50;
        if (mRootView.wordsNumLimit() && contentLenght <= wordLimit) {
            mRootView.initInstructionsPop(mContext.getString(R.string.instructions), String.format(mContext.getString(R.string.dynamic_send_toll_notes), wordLimit));
            return;
        }
        if ((mRootView.wordsNumLimit() && mRootView.getTollMoney() <= 0d) || mRootView.getTollMoney() != (long) mRootView.getTollMoney()) {// 文字收费金额整数限制
            mRootView.initInstructionsPop(mContext.getString(R.string.instructions), String.format(Locale.getDefault(),
                    mContext.getResources().getString(R.string.limit_monye_death), getGoldName()));
            return;
        }

        SendDynamicDataBean sendDynamicDataBean = mRootView.getDynamicSendData();
        int dynamicBelong = sendDynamicDataBean.getDynamicBelong();

        dynamicBean.setUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(dynamicBean.getUser_id()));
        dynamicBean.handleData();
        // 建立 task
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_DYNAMIC_V2);
        HashMap<String, Object> params = new HashMap<>();
        // feed_mark作为参数
        params.put("params", dynamicBean.getFeed_mark());
        params.put("sendDynamicDataBean", sendDynamicDataBeanV2);

        switch (dynamicBelong) {
            case SendDynamicDataBean.NORMAL_DYNAMIC:
                // 将动态信息存入数据库
                mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicBean);
                EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_LIST);
                break;
            case SendDynamicDataBean.GROUP_DYNAMIC:
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
