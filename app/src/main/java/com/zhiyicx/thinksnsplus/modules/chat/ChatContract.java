package com.zhiyicx.thinksnsplus.modules.chat;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.imsdk.entity.Conversation;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public interface ChatContract {

    interface View extends IBaseView<Presenter> {

    }

    interface Repository {
        /**
         * 创建对话
         *
         * @param type 会话类型 `0` 私有会话 `1` 群组会话 `2`聊天室会话
         * @param name 会话名称
         * @param pwd  会话加入密码,type=`0`时该参数无效
         * @param uids 会话初始成员，数组集合或字符串列表``"1,2,3,4"` type=`0`时需要两个uid、type=`1`时需要至少一个、type=`2`时此参数将忽略;注意：如果不合法的uid或uid未注册到IM,将直接忽略
         * @return
         */
        Observable<BaseJson<Conversation>> createConveration(String type, String name, String pwd, String uids);

    }

    interface Presenter extends IBasePresenter {

    }
}
