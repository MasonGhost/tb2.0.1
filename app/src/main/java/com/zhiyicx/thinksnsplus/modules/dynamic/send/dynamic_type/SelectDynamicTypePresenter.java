package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.text.TextUtils;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/19
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class SelectDynamicTypePresenter extends AppBasePresenter<SelectDynamicTypeContract.Repository, SelectDynamicTypeContract.View>
        implements SelectDynamicTypeContract.Presenter{

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public SelectDynamicTypePresenter(SelectDynamicTypeContract.Repository repository, SelectDynamicTypeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public boolean checkCertification() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getmCurrentLoginAuth().getUser_id());
        if (userInfoBean != null && userInfoBean.getVerified() != null) {
            if (TextUtils.isEmpty(userInfoBean.getVerified().getType())) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isNeedPayTip() {
        // 用用户ID加上key来取出值
        return SharePreferenceUtils.getBoolean(mContext,
                String.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id())
                        + SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_SEND_INFO,
                true);
    }

    @Override
    public void savePayTip(boolean isNeed) {
        SharePreferenceUtils.saveBoolean(mContext,
                String.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id())
                        + SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_SEND_INFO,
                false);
    }
}
