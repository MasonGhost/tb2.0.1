package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import javax.inject.Inject;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MyCodePresenter extends AppBasePresenter<MyCodeContract.Repository, MyCodeContract.View>
        implements MyCodeContract.Presenter, OnShareCallbackListener {

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    public MyCodePresenter(MyCodeContract.Repository repository, MyCodeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void createUserCodePic(Bitmap logo) {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (userInfoBean != null) {
            mRootView.getEmptyView().setErrorType(EmptyView.STATE_NETWORK_LOADING);
            // 生成用户二维码，二维码的内容为uid=xx的格式
            String qrCodeContent = String.format(mContext.getString(R.string.my_qr_code_content), userInfoBean.getUser_id());
            Observable.just(qrCodeContent)
                    .subscribeOn(Schedulers.newThread())
                    .map(s -> QRCodeEncoder.syncEncodeQRCode(s, BGAQRCodeUtil.dp2px(mContext, 150), Color.parseColor("#000000"), logo))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> {
                        mRootView.setMyCode(bitmap);
                        mRootView.getEmptyView().setErrorType(EmptyView.STATE_HIDE_LAYOUT);
                    });
        }
    }

    @Override
    public void getUserInfo() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (userInfoBean != null) {
            mRootView.setUserInfo(userInfoBean);
        }
    }

    @Override
    public void shareMyQrCode(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (userInfoBean != null) {
            ShareContent shareContent = new ShareContent();
            shareContent.setTitle(userInfoBean.getName());
            shareContent.setContent(TextUtils.isEmpty(userInfoBean.getIntro()) ? mContext.getString(R.string.intro_default) : userInfoBean.getIntro());
            if (null != bitmap) {
                shareContent.setBitmap(bitmap);
            } else {
                shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
            }
            shareContent.setUrl(String.format(ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_SHARE_USERINFO, userInfoBean.getUser_id()
                    == null ? "" : userInfoBean.getUser_id()));
            mSharePolicy.setShareContent(shareContent);
            mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
        }
    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }
}
