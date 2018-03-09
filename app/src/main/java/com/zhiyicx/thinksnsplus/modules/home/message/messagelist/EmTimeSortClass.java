package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.utils.BaseTimeStringSortClass;

/**
 * @Author Jliuer
 * @Date 2018/03/09/9:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EmTimeSortClass extends BaseTimeStringSortClass<MessageItemBeanV2> {

    @Override
    protected String getData1Time(MessageItemBeanV2 data) {
        return data.getConversation().getLastMessage().getMsgTime() + "";
    }

    @Override
    protected String getData2Time(MessageItemBeanV2 data) {
        return data.getConversation().getLastMessage().getMsgTime() + "";
    }
}
