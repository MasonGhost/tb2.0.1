package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search.ExpertSearchContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class ExpertSearchRepository extends BaseQARepository implements ExpertSearchContract.Repository{

    @Inject
    public ExpertSearchRepository(ServiceManager manager) {
        super(manager);
    }


    @Override
    public Observable<List<ExpertBean>> getExpertList(int size, String topic_ids) {
        return mQAClient.getExpertListByTopicIds(topic_ids, size);
    }
}
