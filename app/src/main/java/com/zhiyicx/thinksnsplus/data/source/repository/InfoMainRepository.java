package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoMainRepository extends BaseInfoRepository implements InfoMainContract.Repository {

    @Inject
    public InfoMainRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<InfoTypeBean> getInfoType() {
        return mInfoMainClient.getInfoType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJson<InfoListBean>> getInfoList(final String cate_id,
                                                          long max_id,
                                                          long page) {
        // 如果传入的cate_id是collections，表示需要获取收藏列表
        // 如果传入的cate_id表示的是咨询列表类型
        switch (cate_id) {
            case ApiConfig.INFO_TYPE_COLLECTIONS:
                return mInfoMainClient.getInfoCollectList(max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page)
                        .map(listBaseJson -> {
                            // 重新封装网络数据
                            List<InfoListDataBean> listBeanList = listBaseJson.getData();
                            BaseJson<InfoListBean> infoListBeanBaseJson = new BaseJson<>();
                            InfoListBean infoListBean = new InfoListBean();
                            infoListBean.setList(listBeanList);
                            infoListBean.setInfo_type(Long.parseLong(cate_id));
                            infoListBeanBaseJson.setData(infoListBean);
                            infoListBeanBaseJson.setMessage(listBaseJson.getMessage());
                            infoListBeanBaseJson.setStatus(listBaseJson.isStatus());
                            infoListBeanBaseJson.setCode(listBaseJson.getCode());
                            return infoListBeanBaseJson;
                        });
            default:
                return mInfoMainClient.getInfoList(cate_id, max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page);
        }
    }
}
