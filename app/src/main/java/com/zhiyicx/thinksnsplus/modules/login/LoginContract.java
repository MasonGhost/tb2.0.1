package com.zhiyicx.thinksnsplus.modules.login;

import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.baseproject.base.IBaseTouristPresenter;

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

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository {
        Observable<BaseJson<AuthBean>> login(Context context, String phone, String password);

        Observable<AuthBean> loginV2(final String account, final String password);
    }

    interface Presenter extends IBaseTouristPresenter {
        void login(String phone, String password);

    }
}
