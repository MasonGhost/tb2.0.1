package com.zhiyicx.thinksnsplus.modules.chat.location;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/10
 * @contact email:648129313@qq.com
 */

public class SendLocationPresenter extends AppBasePresenter<SendLocationContract.View>
        implements SendLocationContract.Presenter{

    @Inject
    public SendLocationPresenter(SendLocationContract.View rootView) {
        super(rootView);
    }
}
