package com.zhiyicx.thinksnsplus.modules.settings;

import com.zhiyicx.appupdate.AppVersionBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UpdateInfoBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public interface SettingsContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IBaseView<Presenter> {
        /**
         * 设置缓存文件大小
         */
        void setCacheDirSize(String size);

        void getAppNewVersionSuccess(List<AppVersionBean> data);
    }

    interface Presenter extends IBasePresenter {
        /**
         * 获取缓存大小
         */
        void getDirCacheSize();

        /**
         * 清理缓存
         */
        void cleanCache();

        /**
         * 退出登录
         *
         * @return true 退出成功，false 退出失败
         */
        boolean loginOut();

        /**
         * 检查是否有更新
         */
        void checkUpdate();

        /**
         * 小助手
         *
         * @return
         */
        List<SystemConfigBean.ImHelperBean> getImHelper();
    }

}
