package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagContract;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/28
 * @Contact master.jungle68@gmail.com
 */

public class EditUserTagRepository implements EditUserTagContract.Repository {
    @Inject
    Application mContext;

    @Inject
    public EditUserTagRepository() {
    }

}
