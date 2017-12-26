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

    interface Presenter extends IBaseTouristPresenter {
        void getVertifyCode(String phone);

        void getVerifyCodeByEmail(String email);

        void register(String name, String phone, String vertifyCode, String password);

        void registerByEmail(String name, String email, String verifyCode, String password);

        void closeTimer();
    }

}
