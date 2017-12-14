package com.zhiyicx.thinksnsplus.modules.circle.manager.earning;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/12/12/11:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleEarningContract {
    interface View extends IBaseView<Presenter> {
    }

    interface Presenter extends IBaseTouristPresenter {
    }

    interface Repository extends IBaseCircleRepository {

        /**
         * 圈子收入记录
         *
         * @param circleId 圈子id
         * @param start 秒级时间戳，起始筛选时间
         * @param end 秒级时间戳，结束筛选时间
         * @param after 默认 0 ，翻页标识。
         * @param limit 默认 15 ，数据返回条数 默认为15
         * @param type  默认 all, all-全部 join-成员加入 pinned-帖子置顶
         * @return
         */
        Observable<List<CircleEarningListBean>> getCircleEarningList(Long circleId, Long start, Long end, Long after, Long limit, String type);
    }
}
