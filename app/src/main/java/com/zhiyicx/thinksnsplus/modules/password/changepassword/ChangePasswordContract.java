package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public interface ChangePasswordContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IBaseView<Presenter> {
        /**
         * 关闭
         */
        void finsh();
    }

    /**
     * Model 层定义接口,外部只需关心 model 返回的数据,无需关心内部细节,及是否使用缓存
     */
    interface Repository  {
        Observable<Object> changePasswordV2(String oldPassword, String newPassword);
    }

    interface Presenter extends IBasePresenter {

        void changePassword(String oldPassword, String newPassword, String sureNewPassword);

    }

}
