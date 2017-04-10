package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelSubscripBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ChannelClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.http.Path;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public class BaseChannelRepository implements IBaseChannelRepository {
    protected ChannelClient mChannelClient;
    protected Context mContext;
    protected ChannelSubscripBeanGreenDaoImpl mChannelSubscripBeanGreenDao;

    @Inject
    public BaseChannelRepository(ServiceManager serviceManager, Application context) {
        mChannelClient = serviceManager.getChannelClient();
        this.mContext = context;
        mChannelSubscripBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().channelSubscripBeanGreenDaoImpl();
    }

    @Override
    public Observable<BaseJson<Object>> cancleSubscribChannel(long channel_id) {
        return mChannelClient.cancleSubscribChannel(channel_id);
    }

    @Override
    public Observable<BaseJson<Object>> subscribChannel(long channel_id) {
        return mChannelClient.subscribChannel(channel_id);
    }

    @Override
    public void handleSubscribChannel(ChannelSubscripBean channelSubscripBean) {
        // 发送订阅后台处理任务
        BackgroundRequestTaskBean backgroundRequestTaskBean = null;
        backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        if (channelSubscripBean.getChannelSubscriped()) {
            // 已经订阅，变为未订阅
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);

        } else {
            // 未订阅，变为已订阅
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST);
        }
        // 设置请求路径
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_SUBSCRIB_CHANNEL_S, channelSubscripBean.getId() + ""));
        // 启动后台任务
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 更改数据源，切换订阅状态
        channelSubscripBean.setChannelSubscriped(!channelSubscripBean.getChannelSubscriped());
        // 更新数据库
        mChannelSubscripBeanGreenDao.insertOrReplace(channelSubscripBean);
    }

    @Override
    public Observable<BaseJson<List<ChannelSubscripBean>>> getChannelList(@Path("type") String type) {
        // 将获取到的ChannelInfoBean类型的频道列表，通过map转换成ChannelSubscripBean类型的数据
        return mChannelClient.getChannelList(type)
                .map(new Func1<BaseJson<List<ChannelInfoBean>>, BaseJson<List<ChannelSubscripBean>>>() {
                    @Override
                    public BaseJson<List<ChannelSubscripBean>> call(BaseJson<List<ChannelInfoBean>> listBaseJson) {
                        BaseJson<List<ChannelSubscripBean>> channelSubscripBeanBaseJson = new BaseJson<List<ChannelSubscripBean>>();
                        channelSubscripBeanBaseJson.setCode(listBaseJson.getCode());
                        channelSubscripBeanBaseJson.setMessage(listBaseJson.getMessage());
                        channelSubscripBeanBaseJson.setStatus(listBaseJson.isStatus());
                        if (listBaseJson.isStatus() || listBaseJson.getCode() == 0) {
                            List<ChannelInfoBean> channelInfoBeanList = listBaseJson.getData();
                            List<ChannelSubscripBean> channelSubscripBeanList = new ArrayList<ChannelSubscripBean>();
                            if (channelInfoBeanList != null) {
                                for (ChannelInfoBean channelInfoBean : channelInfoBeanList) {
                                    ChannelSubscripBean channelSubscripBean = new ChannelSubscripBean();
                                    channelSubscripBean.setId(channelInfoBean.getId());// 设置频道id
                                    channelSubscripBean.setChannelInfoBean(channelInfoBean);// 设置频道信息
                                    //channelSubscripBean.setChannelSubscriped();// 设置订阅状态
                                    //channelSubscripBean.setUserId();// 设置请求的用户id
                                    channelSubscripBeanList.add(channelSubscripBean);
                                }
                            }
                            channelSubscripBeanBaseJson.setData(channelSubscripBeanList);
                        } else {
                            channelSubscripBeanBaseJson.setData(null);
                        }
                        return channelSubscripBeanBaseJson;
                    }
                });
    }
}
