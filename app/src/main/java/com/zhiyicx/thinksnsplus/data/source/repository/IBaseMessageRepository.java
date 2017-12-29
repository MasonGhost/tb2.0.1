package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/29
 * @contact email:648129313@qq.com
 */

public interface IBaseMessageRepository {
    /**
     * 获取对话列表信息
     *
     * @param user_id 用户 id
     */
    Observable<List<MessageItemBeanV2>> getConversationList(int user_id);

    Observable<List<MessageItemBeanV2>> completeEmConversation(List<MessageItemBeanV2> list);
}
