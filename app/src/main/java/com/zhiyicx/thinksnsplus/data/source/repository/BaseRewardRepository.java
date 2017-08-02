package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IRewardRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */
public class BaseRewardRepository implements IRewardRepository {

    private InfoMainClient mInfoMainClient;

    @Inject
    public BaseRewardRepository(ServiceManager serviceManager) {
        mInfoMainClient=serviceManager.getInfoMainClient();
    }
/*******************************************  咨询打赏  *********************************************/

    /**
     * @param news_id 咨询 id
     * @param amount  打赏金额
     * @return
     */
    @Override
    public Observable<Object> rewardsInfo(long news_id, float amount) {
        return mInfoMainClient.rewardsInfo(news_id,amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param news_id    咨询 id
     * @param limit      列表返回数据条数
     * @param since      翻页标识 时间排序时为数据 id 金额排序时为打赏金额 amount
     * @param order      翻页标识 排序 正序-asc 倒序 desc
     * @param order_type 排序规则 date-按时间 amount-按金额
     * @return
     */
    @Override
    public Observable<List<RewardsListBean>> rewardsInfoList(long news_id, Integer limit, Integer since, String order, String order_type) {
        return mInfoMainClient. rewardsInfoList(news_id,limit,since,order,order_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *
     * @param news_id 咨询 id
     * @return
     */
    @Override
    public Observable<RewardsCountBean> getRewardsCount(long news_id) {
        return mInfoMainClient.getRewardsCount(news_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
