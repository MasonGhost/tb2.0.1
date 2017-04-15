package com.zhiyicx.thinksnsplus.modules.home;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */

public interface HomeContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IBaseView<Presenter> {
        /**
         * 是否显示消息提示
         *
         * @param isVisiable
         */
        void setMessageTipVisable(boolean isVisiable);
        /**
         * 是否显示我的消息提示
         *
         * @param isVisiable
         */
        void setMineTipVisable(boolean isVisiable);

        /**
         * 选择 item
         * @param positon
         */
        void checkBottomItem(int positon);

    }

    /**
     * Model 层定义接口,外部只需关心 model 返回的数据,无需关心内部细节,及是否使用缓存
     */
    interface Repository {
    }

    interface Presenter extends IBasePresenter {
        void initIM();
    }
}
