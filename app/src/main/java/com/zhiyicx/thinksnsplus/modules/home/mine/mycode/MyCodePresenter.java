package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
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
        implements MyCodeContract.Presenter{

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public MyCodePresenter(MyCodeContract.Repository repository, MyCodeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void createUserCodePic() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (userInfoBean != null){
            // 生成用户二维码，二维码的内容为uid=xx的格式
            String qrCodeContent = String.format(mContext.getString(R.string.my_qr_code_content), userInfoBean.getUser_id());
            Observable.just(qrCodeContent)
                    .subscribeOn(Schedulers.newThread())
                    .map(s -> QRCodeEncoder.syncEncodeQRCode(s, BGAQRCodeUtil.dp2px(mContext, 150), Color.parseColor("#000000")))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> mRootView.setMyCode(bitmap));
        }
    }
}
