package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailContract;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/3
 * @contact email:450127106@qq.com
 */

public interface DigListContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicDigListBean, DigListContract.Presenter> {
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository {

    }

    interface Presenter extends ITSListPresenter<DynamicDigListBean> {
    }
}
