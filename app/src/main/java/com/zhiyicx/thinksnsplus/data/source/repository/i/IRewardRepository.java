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

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/2
 * @Contact master.jungle68@gmail.com
 */
public interface IRewardRepository {

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


}
