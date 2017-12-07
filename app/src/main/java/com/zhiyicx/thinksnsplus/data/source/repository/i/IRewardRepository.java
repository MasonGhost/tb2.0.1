package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_DYNAMIC_REWARDS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_DYNAMIC_REWARDS_USER_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REWARD_USER;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/2
 * @Contact master.jungle68@gmail.com
 */
public interface IRewardRepository {

    /*******************************************  用户打赏  *********************************************/

    /**
     * 打赏一个用户
     *
     * @param user_id target user
     * @param amount  reward amount 真实货币的分单位
     * @return
     */
    Observable<Object> rewardUser(long user_id, double amount);

    /*******************************************  咨询打赏  *********************************************/

    /**
     * 对一条资讯打赏
     *
     * @param news_id 咨询 id
     * @param amount  打赏金额
     * @return
     */
    Observable<Object> rewardInfo(long news_id, double amount);


    /**
     * 资讯打赏列表
     *
     * @param news_id    咨询 id
     * @param limit      列表返回数据条数
     * @param since      翻页标识 时间排序时为数据 id 金额排序时为打赏金额 amount
     * @param order      翻页标识 排序 正序-asc 倒序 desc
     * @param order_type 排序规则 date-按时间 amount-按金额
     * @return
     */
    Observable<List<RewardsListBean>> rewardInfoList(long news_id, Integer limit, Integer since, String order, String order_type);

    /**
     * 资讯打赏统计
     *
     * @param news_id 咨询 id
     * @return
     */
    Observable<RewardsCountBean> getRewardCount(long news_id);

    /*******************************************  动态打赏  *********************************************/

    /**
     * 对一条动态打赏
     *
     * @param feed_id 动态 id
     * @param amount  打赏金额
     * @return
     */
    Observable<Object> rewardDynamic(long feed_id, double amount);


    /**
     * 动态打赏列表
     *
     * @param feed_id    动态 id
     * @param limit      列表返回数据条数
     * @param since      翻页标识 时间排序时为数据 id 金额排序时为打赏金额 amount
     * @param order      翻页标识 排序 正序-asc 倒序 desc
     * @param order_type 排序规则 date-按时间 amount-按金额
     * @return
     */
    Observable<List<RewardsListBean>> rewardDynamicList(long feed_id, Integer limit, Integer since, String order, String order_type);

    /*******************************************  Q&A 打赏  *********************************************/

    /**
     * 对一条动态打赏
     *
     * @param answer 问题回答 id
     * @param amount 打赏金额
     * @return
     */
    Observable<Object> rewardQA(long answer, double amount);

    /**
     * 问答回答打赏列表
     *
     * @param answer_id    动态 id
     * @param limit      默认 20 ，获取列表条数，修正值 1 - 30
     * @param offset     默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param order_type 默认值 time, time - 按照打赏时间倒序，amount - 按照金额倒序
     * @return
     */
    Observable<List<RewardsListBean>> rewardQAList(long answer_id, Integer limit, Integer offset, String order_type);


    Observable<Object> rewardPost(long sourceId, double rewardMoney);
}
