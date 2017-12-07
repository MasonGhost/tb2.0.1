package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Describe 我加入的圈子
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class MyJoinedCircleActivity extends TSActivity<BaseCircleListPresenter, MyJoinedCircleFragment> {
    @Override
    protected MyJoinedCircleFragment getFragment() {
        return  MyJoinedCircleFragment.newInstance(true);
    }

    @Override
    protected void componentInject() {

    }
}
