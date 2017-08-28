package com.zhiyicx.thinksnsplus.modules.rank.main.container;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class RankIndexActivity extends TSActivity{

    @Override
    protected Fragment getFragment() {
        return new RankIndexFragment().instance();
    }

    @Override
    protected void componentInject() {

    }
}
