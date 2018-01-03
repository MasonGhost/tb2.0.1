package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IVertifyCodeRepository;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */

public interface FindPasswordContract {
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
         * 设置发送按钮的提示信息
         */
        void setVertifyCodeBtText(String text);

        /**
         * 验证码加载动画
         * @param isEnable
         */
        void setVertifyCodeLoading(boolean isEnable);
        /**
         * 关闭
         */
        void finsh();
        /**
         * 设置确认按钮状态
         *
         * @param isEnable
         */
        void setSureBtEnabled(boolean isEnable);
    }

    interface Presenter extends IBasePresenter {

        void findPassword(String phone, String vertifyCode, String newPassword);

        void findPasswordByEmail(String email, String vertifyCode, String newPassword);

        void getVertifyCode(String phone);

        void getVerifyCodeByEmail(String email);
    }

}
