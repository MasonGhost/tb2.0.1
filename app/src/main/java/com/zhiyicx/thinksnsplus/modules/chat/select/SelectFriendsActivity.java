package com.zhiyicx.thinksnsplus.modules.chat.select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;

import static com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsFragment.BUNDLE_GROUP_EDIT_DATA;
import static com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsFragment.BUNDLE_GROUP_IS_DELETE;

/**
 * @author Catherine
 * @describe 选择好友创建会话页面
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class SelectFriendsActivity extends TSActivity<SelectFriendsPresenter, SelectFriendsFragment> {

    @Override
    protected SelectFriendsFragment getFragment() {
        return SelectFriendsFragment.instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerSelectFriendsComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .selectFriendsPresenterModule(new SelectFriendsPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void startSelectFriendActivity(Context context, ChatGroupBean groupBean, boolean isDelete) {
        Intent intent = new Intent(context, SelectFriendsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_GROUP_EDIT_DATA, groupBean);
        bundle.putBoolean(BUNDLE_GROUP_IS_DELETE, isDelete);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
