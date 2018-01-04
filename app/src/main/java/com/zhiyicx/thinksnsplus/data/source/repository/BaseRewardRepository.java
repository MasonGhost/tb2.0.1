package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.QAClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
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
    private UserInfoClient mUserInfoClient;
    private QAClient mQAClient;
    private CircleClient mCircleClient;

    @Inject
    public BaseRewardRepository(ServiceManager serviceManager) {
        mInfoMainClient = serviceManager.getInfoMainClient();
        mDynamicClient = serviceManager.getDynamicClient();
        mUserInfoClient = serviceManager.getUserInfoClient();
        mQAClient = serviceManager.getQAClient();
        mCircleClient=serviceManager.getCircleClient();
    }

    /*******************************************  用户打赏  *********************************************/


    @Override
    public Observable<Object> rewardUser(long user_id, double amount) {
        return mUserInfoClient.rewardUser(user_id, (long) amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*******************************************  咨询打赏  *********************************************/

    /**
     * @param news_id 咨询 id
     * @param amount  打赏金额
     * @return
     */
    @Override
    public Observable<Object> rewardInfo(long news_id, double amount) {
        return mInfoMainClient.rewardInfo(news_id, (long) amount)
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
        return mDynamicClient.rewardDynamic(feed_id, (long) amount)
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

    /*******************************************  问答回答打赏  *********************************************/

    /**
     * @param answer_id
     * @param amount    打赏金额
     * @return
     */
    @Override
    public Observable<Object> rewardQA(long answer_id, double amount) {
        return mQAClient.rewardQA(answer_id, (long) amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> rewardPost(long postId, double rewardMoney) {
        return mCircleClient.rewardPost(postId, (long) rewardMoney)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *
     * @param answer_id    动态 id
     * @param limit      默认 20 ，获取列表条数，修正值 1 - 30
     * @param offset     默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param order_type 默认值 time, time - 按照打赏时间倒序，amount - 按照金额倒序
     * @return
     */
    @Override
    public Observable<List<RewardsListBean>> rewardQAList(long answer_id, Integer limit, Integer offset, String order_type) {
        return mQAClient.rewardQAList(answer_id,limit,offset,order_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
