package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public interface IBaseRankRepository {

    Observable<List<UserInfoBean>> getRankFollower(int size);
    Observable<List<UserInfoBean>> getRankRiches(int size);
    Observable<List<UserInfoBean>> getRankIncome(int size);
    Observable<List<UserInfoBean>> getRankCheckIn(int size);
    Observable<List<UserInfoBean>> getRankQuestionExpert(int size);
    Observable<List<UserInfoBean>> getRankQuestionLikes(int size);
    Observable<List<UserInfoBean>> getRankAnswer(String type, int size);
    Observable<List<UserInfoBean>> getRankDynamic(String type, int size);
    Observable<List<UserInfoBean>> getRankInfo(String type, int size);

}
