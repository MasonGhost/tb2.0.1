package com.zhiyicx.thinksnsplus.modules.tb.info;

import android.os.Bundle;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/22
 * @contact master.jungle68@gmail.com
 */
public class TBHomeInfoListFragment extends InfoListFragment {


    public static InfoListFragment newInstance(String params) {
        TBHomeInfoListFragment fragment = new TBHomeInfoListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_INFO_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }
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

    @Override
    protected int setEmptView() {return R.mipmap.def_home_information_prompt;
    }
}
