package com.zhiyicx.thinksnsplus.modules.login;

import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.AccountBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.SystemConfigBean;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public interface LoginContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends IBaseView<Presenter> {
        /**
         * 正在登录
         */
        void setLogining();

        /**
         * 设置登陆状态 true 登陆成功  false 登录失败
         */
        void setLoginState(boolean loginState);

        /**
         * 登录异常提示
         */
        void showErrorTips(String error);

        AccountBean getAccountBean();

        /**
         * 三方注册
         * @param provider
         * @param access_token
         */
        void registerByThrid(String provider, String access_token);

    }


    interface Presenter extends IBaseTouristPresenter {
        void login(String phone, String password);

        List<AccountBean> getAllAccountList();

        /**
         * 三方登录或者注册
         *
         * @param provider
         * @param access_token
         */
        void checkBindOrLogin(String provider, String access_token);
    }
}
