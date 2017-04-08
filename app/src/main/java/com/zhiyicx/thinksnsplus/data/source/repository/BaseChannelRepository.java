package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ChannelClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

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

    @Inject
    public BaseChannelRepository(ServiceManager serviceManager, Application context) {
        mChannelClient = serviceManager.getChannelClient();
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
    public Observable<BaseJson<List<ChannelSubscripBean>>> getChannelList(@Path("type") String type) {
        // 将获取到的ChannelInfoBean类型的频道列表，通过map转换成ChannelSubscripBean类型的数据
        return mChannelClient.getChannelList(type)
                .map(new Func1<BaseJson<List<ChannelInfoBean>>, BaseJson<List<ChannelSubscripBean>>>() {
                    @Override
                    public BaseJson<List<ChannelSubscripBean>> call(BaseJson<List<ChannelInfoBean>> listBaseJson) {
                        if (listBaseJson.isStatus() || listBaseJson.getCode() == 0) {
                            List<ChannelInfoBean> channelInfoBeanList = listBaseJson.getData();
                            if (channelInfoBeanList == null || channelInfoBeanList.isEmpty()) {

                            } else {
                                for (ChannelInfoBean channelInfoBean : channelInfoBeanList) {
                                    ChannelSubscripBean channelSubscripBean = new ChannelSubscripBean();
                                }
                            }
                        }
                        return null;
                    }
                });
    }
}
