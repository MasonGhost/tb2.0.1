package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelist.MessageConversationContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/29
 * @contact email:648129313@qq.com
 */

public class MessageConversationRepository extends BaseMessageRepository implements MessageConversationContract.Repository{

    @Inject
    public MessageConversationRepository(ServiceManager manager) {
        super(manager);
    }
}
