package com.zhiyicx.thinksnsplus.modules.tb.info;

import com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/22
 * @contact master.jungle68@gmail.com
 */
public class TBInfoContainerFragment extends InfoContainerFragment {
    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }
}
