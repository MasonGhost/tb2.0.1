package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicContract;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */

public interface DynamicDetailContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicBean, Presenter> {
        /**
         * 设置是否喜欢该动态
         *
         * @param isLike
         */
        void setLike(boolean isLike);

        /**
         * 设置是否收藏该动态
         *
         * @param isCollect
         */
        void setCollect(boolean isCollect);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IDynamicReppsitory {

    }

    interface Presenter extends ITSListPresenter<DynamicBean> {

    }
}
