package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IRewardRepository;

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
    private DynamicClient mDynamicClient;

    @Inject
    public BaseRewardRepository(ServiceManager serviceManager) {
        mInfoMainClient = serviceManager.getInfoMainClient();
        mDynamicClient = serviceManager.getDynamicClient();
    }

    /*******************************************  咨询打赏  *********************************************/

    /**
     * @param news_id 咨询 id
     * @param amount  打赏金额
     * @return
     */
    @Override
    public Observable<Object> rewardInfo(long news_id, double amount) {
        return mInfoMainClient.rewardInfo(news_id, (float) amount)
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
    public Observable<List<RewardsListBean>> rewardInfoList(long news_id, Integer limit, Integer since, String order, String order_type) {
        return mInfoMainClient.rewardInfoList(news_id, limit, since, order, order_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param news_id 咨询 id
     * @return
     */
    @Override
    public Observable<RewardsCountBean> getRewardCount(long news_id) {
        return mInfoMainClient.getRewardCount(news_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*******************************************  动态打赏  *********************************************/

    /**
     * @param feed_id 动态 id
     * @param amount  打赏金额
     * @return
     */
    @Override
    public Observable<Object> rewardDynamic(long feed_id, double amount) {
        return mDynamicClient.rewardDynamic(feed_id, (float) amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param feed_id    动态 id
     * @param limit      列表返回数据条数
     * @param since      翻页标识 时间排序时为数据 id 金额排序时为打赏金额 amount
     * @param order      翻页标识 排序 正序-asc 倒序 desc
     * @param order_type 排序规则 date-按时间 amount-按金额
     * @return
     */
    @Override
    public Observable<List<RewardsListBean>> rewardDynamicList(long feed_id, Integer limit, Integer since, String order, String order_type) {
        return mDynamicClient.rewardDynamicList(feed_id, limit, since, order, order_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
