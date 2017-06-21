package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 */

public interface SendDynamicContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends IBaseView<SendDynamicContract.Presenter> {
        void sendDynamicComplete();

        /**
         * 获取动态上一个页面的数据，用来判断发送动态的某些逻辑
         *
         * @return
         */
        SendDynamicDataBean getDynamicSendData();

        boolean hasTollVerify();

        List<SendDynamicDataBeanV2.StorageTaskBean> packageDynamicStorageDataV2();

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IDynamicReppsitory {


    }

    interface Presenter extends IBasePresenter {
        void sendDynamic(DynamicBean dynamicBean);
        void sendDynamicV2(DynamicBean dynamicBean);
    }
}
