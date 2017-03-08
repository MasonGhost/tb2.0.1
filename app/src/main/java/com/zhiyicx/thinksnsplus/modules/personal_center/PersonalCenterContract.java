package com.zhiyicx.thinksnsplus.modules.personal_center;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public interface PersonalCenterContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicBean, PersonalCenterContract.Presenter> {
        /**
         * 设置头部的用户相关信息
         */
        void setHeaderInfo(UserInfoBean userInfoBean);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository {
        /**
         * 获取当前个人中心的用户信息
         *
         * @return
         */
        Observable<BaseJson<UserInfoBean>> getCurrentUserInfo(Long userId);
    }

    interface Presenter extends ITSListPresenter<DynamicBean> {
        void setCurrentUserInfo(Long userId);
    }
}
