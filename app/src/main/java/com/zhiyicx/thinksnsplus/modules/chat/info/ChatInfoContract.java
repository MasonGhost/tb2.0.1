package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public interface ChatInfoContract {

    interface View extends IBaseView<Presenter>{

    }

    interface Presenter extends IBasePresenter{

    }

    interface Repository extends IBaseFriendsRepository{

    }
}
