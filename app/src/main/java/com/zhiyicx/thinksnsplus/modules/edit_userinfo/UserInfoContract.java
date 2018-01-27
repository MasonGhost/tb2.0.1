package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public interface UserInfoContract {
    interface View extends IBaseView<UserInfoContract.Presenter> {
        /**
         * 设置地域选择的数据
         */
        void setAreaData(ArrayList<AreaBean> options1Items, ArrayList<ArrayList<AreaBean>> options2Items, ArrayList<ArrayList<ArrayList<AreaBean>>>
                options3Items);

        /**
         * 设置头像上传的状态
         *
         * @param upLoadState -1 失败 0进行中 1 图片上传成功 2图片用户信息修改成功
         * @param message
         */
        void setUpLoadHeadIconState(int upLoadState, String message);

        /**
         * 设置信息修改提交状态
         *
         * @param changeUserInfoState -1 失败 1进行中 2 成功
         */
        void setChangeUserInfoState(int changeUserInfoState, String message);

        /**
         * 初始化界面数据
         */
        void initUserInfo(UserInfoBean mUserInfoBean);

        /**
         * 更新用户标签
         *
         * @param datas tags
         */
        void updateTags(List<UserTagBean> datas);
    }

    interface Presenter extends IBasePresenter {
        /**
         * 上传用户头像
         *
         * @param filePath
         */
        void changeUserHeadIcon(String filePath);

        /**
         * @param userInfos
         * @param isHeadIcon 仅仅修改头像
         */
        void changUserInfo(HashMap<String, Object> userInfos, boolean isHeadIcon);


        /**
         * 初始化用户界面数据
         */
        void initUserInfo();

        void getCurrentUserTags();
    }
}
