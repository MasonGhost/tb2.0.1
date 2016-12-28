package com.zhiyicx.thinksnsplus.modules.register;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.User;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public interface RegisterContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends IBaseView {

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository {
        Observable<BaseJson<User>> getVertifyCode(String phone);
    }

    interface Presenter {
        void getVertifyCode(String phone);

        void login();
    }

}
