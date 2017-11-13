package com.zhiyicx.thinksnsplus.modules.register;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IVertifyCodeRepository;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public interface RegisterContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IBaseView<Presenter> {
        /**
         * 设置验证码按钮状态
         *
         * @param isEnable
         */
        void setVertifyCodeBtEnabled(boolean isEnable);

        /**
         * 设置验证码加载
         *
         * @param isEnable
         */
        void setVertifyCodeLoadin(boolean isEnable);

        /**
         * 设置发送按钮的提示信息
         */
        void setVertifyCodeBtText(String text);

        /**
         * 设置验证码按钮状态
         *
         * @param isEnable
         */
        void setRegisterBtEnabled(boolean isEnable);

        /**
         * 跳转主页
         */
        void goHome();
    }

    /**
     * Model 层定义接口,外部只需关心 model 返回的数据,无需关心内部细节,及是否使用缓存
     */
    interface Repository extends IVertifyCodeRepository {

        /**
         * 注册
         *
         * @param phone       注册的手机号码
         * @param name        用户名
         * @param vertifyCode 手机验证码
         * @param password    用户密码
         * @return
         */
        Observable<AuthBean> registerByPhone(String phone, String name, String vertifyCode, String password);

        /**
         * @param email       注册的邮箱
         * @param name        用户名
         * @param vertifyCode 邮箱验证码
         * @param password    用户密码
         * @return
         */
        Observable<AuthBean> registerByEmail(String email, String name, String vertifyCode, String password);

    }

    interface Presenter extends IBaseTouristPresenter {
        void getVertifyCode(String phone);

        void getVerifyCodeByEmail(String email);

        void register(String name, String phone, String vertifyCode, String password);

        void registerByEmail(String name, String email, String verifyCode, String password);

        void closeTimer();
    }

}
