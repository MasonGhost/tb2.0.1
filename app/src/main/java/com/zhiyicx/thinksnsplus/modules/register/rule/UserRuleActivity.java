package com.zhiyicx.thinksnsplus.modules.register.rule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 17/10/27 14:04
 * @Email Jliuer@aliyun.com
 * @Description 用户协议
 */
public class UserRuleActivity extends TSActivity {

    @Override
    protected UserRuleFragment getFragment() {
        return UserRuleFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {

    }

    public static void startUserRuleActivity(Context context, String title, String rule) {
        Bundle bundle = new Bundle();
        bundle.putString(UserRuleFragment.TITLE, title);
        bundle.putString(UserRuleFragment.RULE, rule);
        Intent intent = new Intent(context, UserRuleActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
