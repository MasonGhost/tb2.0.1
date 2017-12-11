package com.zhiyicx.thinksnsplus.modules.report;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public interface ReportContract {
    interface View extends IBaseView<Presenter> {
    }

    interface Presenter extends IBasePresenter {
    }

    interface Repository {
    }
}
