package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

import retrofit2.http.Path;
import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface DynamicCommentTollContract {

    interface View extends IBaseView<Presenter> {

    }

    interface Presenter extends IBasePresenter {
        void tollDynamicComment(Long feed_id);
    }

    interface Repository {
        Observable<BaseJson<Integer>> tollDynamicComment(Long feed_id);
    }
}
