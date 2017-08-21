package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.dig_list.AnswerDigListContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */

public class AnswerDigListRepository extends BaseQARepository implements AnswerDigListContract.Repository {

    @Inject
    protected UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public AnswerDigListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<List<AnswerDigListBean>> getAnswerDigListV2(Long answer_id, Long max_id) {
        return mQAClient.getAnswerDigList(answer_id, max_id, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<AnswerDigListBean>, Observable<List<AnswerDigListBean>>>() {
                    @Override
                    public Observable<List<AnswerDigListBean>> call(List<AnswerDigListBean> answerDigListBeen) {

                        if (!answerDigListBeen.isEmpty()) {
                            List<Object> userids = new ArrayList<>();
                            for (int i = 0; i < answerDigListBeen.size(); i++) {
                                AnswerDigListBean answerDigListBean = answerDigListBeen.get(i);
                                userids.add(answerDigListBean.getUser_id());
                                userids.add(answerDigListBean.getTarget_user());
                            }
                            // 通过用户id列表请求用户信息和用户关注状态
                            return mUserInfoRepository.getUserInfo(userids)
                                    .map(listBaseJson -> {
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : listBaseJson) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        for (AnswerDigListBean answerDigListBean : answerDigListBeen) {
                                            answerDigListBean.setDiggUserInfo(userInfoBeanSparseArray.get(answerDigListBean.getUser_id().intValue()));
                                            answerDigListBean.setTargetUserInfo(userInfoBeanSparseArray.get(answerDigListBean.getTarget_user().intValue()));
                                        }
                                        mUserInfoBeanGreenDao.insertOrReplace(listBaseJson);
                                        return answerDigListBeen;
                                    });
                        } else {
                            return Observable.just(answerDigListBeen);
                        }
                    }
                });
    }
}
