package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

import rx.Observable;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/21
 * @Contact master.jungle68@gmail.com
 */
public interface DynamicContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicBean, Presenter> {
        /**
         * get dynamic type
         *
         * @return  dynamic type
         */
        String getDynamicType();
        /**
         * 获取列表数据
         * @return
         */
        List<DynamicBean> getDatas();
        /**
         * 获取列表数据
         * @return
         */
        void setDatas(List<DynamicBean> dynamicBeen);

        void refreshPosition(int position);

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IDynamicReppsitory {
        Observable<BaseJson<List<DynamicBean>>> getHistoryDynamicList(String type, long max_id, long limit, long page);
    }

    interface Presenter extends ITSListPresenter<DynamicBean> {
    }
}
