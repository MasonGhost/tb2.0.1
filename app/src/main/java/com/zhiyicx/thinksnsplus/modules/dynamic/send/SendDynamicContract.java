package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

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

        void packageDynamicStorageDataV2(SendDynamicDataBeanV2 sendDynamicDataBeanV2);
        void packageGroupDynamicStorageData(GroupSendDynamicDataBean sendDynamicDataBeanV2);

        double getTollMoney();

        boolean wordsNumLimit();

        void initInstructionsPop(String title,String des);
    }

    interface Presenter extends IBaseTouristPresenter {
        void sendGroupDynamic(GroupDynamicListBean dynamicBean);
        void sendDynamicV2(DynamicDetailBeanV2 dynamicBean);
    }
}
