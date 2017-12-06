package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.circle.mine.DaggerMyGroupComponent;
import com.zhiyicx.thinksnsplus.modules.circle.mine.MyGroupFragment;
import com.zhiyicx.thinksnsplus.modules.circle.mine.MyGroupPresenter;
import com.zhiyicx.thinksnsplus.modules.circle.mine.MyGroupPresenterModule;

/**
 * @Describe 我加入的圈子
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class MyJoinedCircleActivity extends TSActivity<MyJoinedCirclePresenter, MyJoinedCircleFragment> {
    @Override
    protected MyJoinedCircleFragment getFragment() {
        return  MyJoinedCircleFragment.newInstance(true);
    }

    @Override
    protected void componentInject() {

    }
}
