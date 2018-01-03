package com.zhiyicx.thinksnsplus.modules.circle.publish.choose_circle;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/12/15/17:53
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface ChooseCircleContract {

    interface View extends IBaseView<Presenter> {
        CircleClient.MineCircleType getMineCircleType();

        void onNetResponseSuccess(List<CircleInfo> data);
    }

    interface Presenter extends IBasePresenter {
        void getMyJoinedCircleList();
    }
}
