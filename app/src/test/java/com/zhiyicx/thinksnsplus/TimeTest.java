package com.zhiyicx.thinksnsplus;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.junit.Test;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

import static com.zhiyicx.imsdk.db.base.BaseDao.TIME_DEFAULT_ADD;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/17
 * @Contact master.jungle68@gmail.com
 */

public class TimeTest {

    /**
     * utc 时间测试
     */
    @Test
    public void finalTest() {
        System.out.println(System.currentTimeMillis());
        String time = "2017-03-17 8:28:56";
        System.out.println("TimeUtils.converTime(time, TimeZone.getDefault()) = " + TimeUtils.utc2LocalStr(time));
        System.out.println("TimeUtils.getTimeFriendlyForDetail() = " + TimeUtils.getTimeFriendlyForDetail(TimeUtils.utc2LocalStr(time)));

        java.util.Calendar ca2 = java.util.Calendar.getInstance();
        System.out.println("ca2 = " + ca2.getTimeInMillis());
    }

    /**
     * 获取当前 0 时区时间测试
     */
    @Test
    public void getCurrenZeroTime() {
        System.out.println("cal str = " + TimeUtils.getCurrenZeroTimeStr());
        System.out.println("cal  long= " + TimeUtils.getCurrenZeroTime());

    }

    @Test
    public void rxMerge() {
        UserInfoBean userInfoBean1 = new UserInfoBean();
        userInfoBean1.setUser_id(10L);
        UserInfoBean userInfoBean2 = new UserInfoBean();
        userInfoBean2.setName("李四");
        Observable<UserInfoBean> userInfoBeanObservable1 = Observable.just(userInfoBean1);
        Observable<UserInfoBean> userInfoBeanObservable2 = Observable.just(userInfoBean2);
        Observable.merge(userInfoBeanObservable1, userInfoBeanObservable2)
                .subscribe(new Action1<UserInfoBean>() {
                    @Override
                    public void call(UserInfoBean userInfoBean) {
                        System.out.println("userInfoBean = " + userInfoBean.getUser_id());
                    }
                });
        Observable.zip(userInfoBeanObservable1, userInfoBeanObservable2, new Func2<UserInfoBean, UserInfoBean, UserInfoBean>() {
            @Override
            public UserInfoBean call(UserInfoBean userInfoBean, UserInfoBean userInfoBean2) {
                userInfoBean.setName(userInfoBean2.getName());
                return userInfoBean;
            }
        }).subscribe(new Action1<UserInfoBean>() {
            @Override
            public void call(UserInfoBean userInfoBean) {
                System.out.println("userInfoBean = " + userInfoBean.getUser_id() + userInfoBean.getName());
            }
        });


    }

    @Test
    public void rxCombineLast() {
        UserInfoBean userInfoBean1 = new UserInfoBean();
        userInfoBean1.setName("张三");
        UserInfoBean userInfoBean2 = new UserInfoBean();
        userInfoBean2.setName("李四");
        Observable<UserInfoBean> userInfoBeanObservable1 = Observable.just(userInfoBean1);
        Observable<UserInfoBean> userInfoBeanObservable2 = Observable.just(userInfoBean2);

        Observable.combineLatest(userInfoBeanObservable1, userInfoBeanObservable2, new Func2<UserInfoBean, UserInfoBean, UserInfoBean>() {
            @Override
            public UserInfoBean call(UserInfoBean userInfoBean, UserInfoBean userInfoBean2) {
                System.out.println("userInfoBean = " + userInfoBean.getName());
                System.out.println("userInfoBean2 = " + userInfoBean2.getName());
                return userInfoBean;

            }
        }).subscribe(new Action1<UserInfoBean>() {
            @Override
            public void call(UserInfoBean userInfoBean) {
                System.out.println(userInfoBean.getName());
            }
        });

    }

    @Test
    public void rxZip() {
        UserInfoBean userInfoBean1 = new UserInfoBean();
        userInfoBean1.setUser_id(10L);
        UserInfoBean userInfoBean2 = new UserInfoBean();
        userInfoBean2.setName("李四");
        Observable<UserInfoBean> userInfoBeanObservable1 = Observable.just(userInfoBean1);
        Observable<UserInfoBean> userInfoBeanObservable2 = Observable.just(userInfoBean2);
        Observable.zip(userInfoBeanObservable1, userInfoBeanObservable2, new Func2<UserInfoBean, UserInfoBean, UserInfoBean>() {
            @Override
            public UserInfoBean call(UserInfoBean userInfoBean, UserInfoBean userInfoBean2) {
                userInfoBean.setName(userInfoBean2.getName());
                return userInfoBean;
            }
        }).subscribe(new Action1<UserInfoBean>() {
            @Override
            public void call(UserInfoBean userInfoBean) {
                System.out.println("userInfoBean = " + userInfoBean.getUser_id() + userInfoBean.getName());
            }
        });


    }


    @Test
    public void testdjgkdgj() {

        long a = 325237164109463553L;
        long b = (a >> 23) + TIME_DEFAULT_ADD;
        System.out.println("b = " + b);

    }
}
