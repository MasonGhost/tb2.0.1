package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelSubscripBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ChannelClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.http.Path;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public class BaseChannelRepository extends BaseDynamicRepository implements IBaseChannelRepository {
    protected ChannelClient mChannelClient;
    @Inject
    protected ChannelSubscripBeanGreenDaoImpl mChannelSubscripBeanGreenDao;
    @Inject
    protected ChannelInfoBeanGreenDaoImpl mChannelInfoBeanGreenDao;

    @Inject
    public BaseChannelRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mChannelClient = serviceManager.getChannelClient();

    }

    @Override
    public void handleSubscribChannel(ChannelSubscripBean channelSubscripBean) {
        // 发送订阅后台处理任务
        BackgroundRequestTaskBean backgroundRequestTaskBean = null;
        backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        ChannelInfoBean channelInfoBean = channelSubscripBean.getChannelInfoBean();
        if (channelSubscripBean.getChannelSubscriped()) {
            // 已经订阅，变为未订阅
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
            channelInfoBean.setFollow_count(channelInfoBean.getFollow_count() - 1);// 订阅数-1
        } else {
            // 未订阅，变为已订阅
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST);
            channelInfoBean.setFollow_count(channelInfoBean.getFollow_count() + 1);// 订阅数+1
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
    public Observable<BaseJson<Object>> handleSubscribChannelByFragment(final ChannelSubscripBean channelSubscripBean) {
        final boolean subscribState = channelSubscripBean.getChannelSubscriped();
        long channelId = channelSubscripBean.getId();
        Observable<BaseJson<Object>> observable = null;
        if (subscribState) {
            // 已经订阅，变为未订阅
            observable = mChannelClient.cancleSubscribChannel(channelId);
        } else {
            // 未订阅，变为已订阅
            observable = mChannelClient.subscribChannel(channelId);
        }
        return observable.doOnNext(new Action1<BaseJson<Object>>() {
            @Override
            public void call(BaseJson<Object> objectBaseJson) {
                if (objectBaseJson.isStatus() || objectBaseJson.getCode() == 0) {
                    // 服务器返回正常状态：操作数据库，数据源
                    ChannelInfoBean channelInfoBean = channelSubscripBean.getChannelInfoBean();
                    if (subscribState) {
                        channelInfoBean.setFollow_count(channelInfoBean.getFollow_count() - 1);// 订阅数-1
                    } else {
                        channelInfoBean.setFollow_count(channelInfoBean.getFollow_count() + 1);// 订阅数+1
                    }
                    // 更改数据源，切换订阅状态
                    channelSubscripBean.setChannelSubscriped(!channelSubscripBean.getChannelSubscriped());
                    // 更新数据库
                    mChannelSubscripBeanGreenDao.insertOrReplace(channelSubscripBean);
                } else {
                    // 返回错误状态，表明订阅或者取消订阅失败，不要改变当前状态
                }
            }
        });
    }

    @Override
    public Observable<BaseJson<List<ChannelSubscripBean>>> getChannelList(@Path("type") final String type, final long userId) {
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
                                    // 如果是获取我订阅的频道，设置订阅状态为1
                                    if (type == ApiConfig.CHANNEL_TYPE_MY_SUBSCRIB_CHANNEL) {
                                        channelInfoBean.setFollow_status(1);
                                    }
                                    channelSubscripBean.setChannelSubscriped(channelInfoBean.getFollow_status() == 0 ? false : true);// 设置订阅状态
                                    channelSubscripBean.setUserId(userId);// 设置请求的用户id
                                    channelSubscripBean.setUserIdAndIdforUnique("");// 添加唯一约束，防止数据重复
                                    channelSubscripBeanList.add(channelSubscripBean);
                                }
                            }
                            channelSubscripBeanBaseJson.setData(channelSubscripBeanList);
                            mChannelInfoBeanGreenDao.insertOrReplace(channelInfoBeanList);
                            mChannelSubscripBeanGreenDao.clearTable();
                            mChannelSubscripBeanGreenDao.insertOrReplace(channelSubscripBeanList);

                        } else {
                            channelSubscripBeanBaseJson.setData(null);
                        }
                        return channelSubscripBeanBaseJson;
                    }
                });
    }

    @Override
    public Observable<BaseJson<List<DynamicBean>>> getDynamicListFromChannel(long channel_id, long max_id) {
        return dealWithDynamicList(mChannelClient.getDynamicListFromChannel(channel_id, TSListFragment.DEFAULT_PAGE_SIZE, max_id), "", false);
    }


}
