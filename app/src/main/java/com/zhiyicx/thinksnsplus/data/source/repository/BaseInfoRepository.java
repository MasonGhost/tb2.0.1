package com.zhiyicx.thinksnsplus.data.source.repository;

import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/7
 * @contact email:648129313@qq.com
 */

public class BaseInfoRepository implements IBaseInfoRepository{

    protected InfoMainClient mInfoMainClient;

    @Inject
    public BaseInfoRepository(ServiceManager manager) {
        mInfoMainClient = manager.getInfoMainClient();
    }

    @Override
    public Observable<List<InfoListDataBean>> getInfoListV2(String cate_id, String key, long max_id, long page, int isRecommend) {
        if (!TextUtils.isEmpty(key)){
            return mInfoMainClient.getInfoListV2(cate_id, max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page, key, 0);
        } else {
            return mInfoMainClient.getInfoTopList(cate_id)
                    .flatMap(new Func1<List<InfoListDataBean>, Observable<List<InfoListDataBean>>>() {
                        @Override
                        public Observable<List<InfoListDataBean>> call(List<InfoListDataBean> infoListDataBeenTopList) {
                            if (infoListDataBeenTopList != null){
                                for (InfoListDataBean infoListDataBean : infoListDataBeenTopList){
                                    infoListDataBean.setIsTop(true);
                                }
                            }
                            return mInfoMainClient.getInfoListV2(cate_id, max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page, "", isRecommend)
                                    .map(infoListDataBeenList -> {
                                        if (infoListDataBeenTopList != null){
                                            infoListDataBeenList.addAll(0, infoListDataBeenTopList);
                                        }
                                        return infoListDataBeenList;
                                    });
                        }
                    });
        }
    }
}
