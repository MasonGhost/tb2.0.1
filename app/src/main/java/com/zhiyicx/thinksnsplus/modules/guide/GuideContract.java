package com.zhiyicx.thinksnsplus.modules.guide;

import android.content.Context;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */

public interface GuideContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IBaseView<Presenter> {
        /**
         * 跳转
         *
         * @param tClass
         */
        void startActivity(Class tClass);
        void initAdvert();
    }


    interface Presenter extends IBasePresenter {

        void checkLogin();

        SystemConfigBean getAdvert();

        void getLaunchAdverts();

        List<RealAdvertListBean> getBootAdvert();

        void initConfig();

    }

}
