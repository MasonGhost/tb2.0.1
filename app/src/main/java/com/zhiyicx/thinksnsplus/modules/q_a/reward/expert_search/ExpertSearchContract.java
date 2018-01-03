package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public interface ExpertSearchContract {

    interface View extends ITSListView<ExpertBean, Presenter> {

    }

    interface Presenter extends ITSListPresenter<ExpertBean> {
        void requestNetData(Long maxId, int topic_id, boolean isLoadMore);

        void requestNetData(int size, String topic_ids, String keyword, boolean isLoadMore);

        void handleFollowUser(UserInfoBean userInfoBean);
    }
}
