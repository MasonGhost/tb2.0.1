package com.zhiyicx.thinksnsplus.modules.information.infomain;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoMainContract {

    /**
     * 外层类型列表
     */
    interface InfoContainerView extends IBaseView<InfoContainerPresenter> {
        void setInfoType(InfoTypeBean infoType);
    }

    interface InfoContainerPresenter extends IBasePresenter {
        void getInfoType();
    }


    /**
     * 内层内容列表
     */
    interface InfoListView extends ITSListView<BaseListBean,InfoListPresenter> {
        String getInfoType();
    }

    interface InfoListPresenter extends ITSListPresenter<BaseListBean> {
        void getInfoList(String cate_id, long max_id,
                         long limit, long page);
    }


    interface Reppsitory {
        Observable<BaseJson<InfoTypeBean>> getInfoType();

        /**
         * @param cate_id 订阅分类 -1 推荐 -2 热门 其他对应资讯分类id
         * @param max_id  用来翻页的记录id(对应数据体里的id)
         * @param page    翻页页码（热门和推荐列表 加载所需传入）
         * @return
         */
        Observable<BaseJson<InfoListBean>> getInfoList(String cate_id, long max_id,
                                                             long page);
    }
}
