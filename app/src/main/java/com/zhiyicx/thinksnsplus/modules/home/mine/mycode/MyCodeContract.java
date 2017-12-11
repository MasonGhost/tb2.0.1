package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public interface MyCodeContract {

    interface View extends IBaseView<Presenter> {
        /**
         * 设置生成的二维码图片
         *
         * @param codePic 生成的图片结果
         */
        void setMyCode(Bitmap codePic);

        /**
         * 设置用户信息
         *
         * @param userInfo 用户信息
         */
        void setUserInfo(UserInfoBean userInfo);

        EmptyView getEmptyView();
    }

    interface Presenter extends IBaseTouristPresenter {
        /**
         * 生成用户二维码图片
         *
         * @param logo 二维码的logo
         */
        void createUserCodePic(Bitmap logo);

        /**
         * 获取用户信息
         */
        void getUserInfo();

        /**
         * 分享二维码
         */
        void shareMyQrCode(Bitmap bitmap);
    }

    interface Repository {

    }
}
