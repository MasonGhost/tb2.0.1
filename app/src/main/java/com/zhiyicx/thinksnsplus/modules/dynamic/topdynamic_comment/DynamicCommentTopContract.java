package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentToll;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface DynamicCommentTollContract {

    interface View extends IBaseView<Presenter> {
        void initWithdrawalsInstructionsPop();
        float getCommentMoney();
    }

    interface Presenter extends IBasePresenter {
        void setDynamicCommentToll(Long feed_id, int amout);
    }

    interface Repository {
        Observable<DynamicCommentToll> tollDynamicComment(Long feed_id, int amount);
    }
}
