package com.zhiyicx.thinksnsplus.modules.report;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public interface ReportContract {
    interface View extends IBaseView<Presenter> {
        /**
         * 举报提交成功
         *
         * @param data
         */
        void reportSuccess(ReportResultBean data);

        /**
         * 更新用户信息回调
         *
         * @param data
         */
        void getUserInfoResult(UserInfoBean data);
    }

    interface Presenter extends IBasePresenter {
        /**
         * 举报
         *
         * @param inputContent       举报的内容
         * @param reportResourceBean 举报相关的资源
         */
        void report(String inputContent, ReportResourceBean reportResourceBean);

        /**
         * 获取用户信息
         *
         * @param userId 需要获取的用户 id
         */
        void getUserInfoById(Long userId);
    }

}
