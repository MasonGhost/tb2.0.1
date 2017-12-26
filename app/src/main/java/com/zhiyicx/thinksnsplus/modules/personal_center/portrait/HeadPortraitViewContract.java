package com.zhiyicx.thinksnsplus.modules.personal_center.portrait;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.HashMap;

/**
 * @author Catherine
 * @describe 需求还没定，可能有更换头像之类的吧 开心就好
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */

public interface HeadPortraitViewContract {

    interface View extends IBaseView<Presenter> {
        /**
         * 设置头像上传的状态
         *
         * @param upLoadState -1 失败 0进行中 1 图片上传成功 2图片用户信息修改成功
         * @param taskId      返回的图片任务id
         */
        void setUpLoadHeadIconState(int upLoadState, int taskId);
    }

    interface Presenter extends IBasePresenter {
        UserInfoBean getCurrentUser(long user_id);

        /**
         * 上传用户头像
         */
        void changeUserHeadIcon(String filePath);

        /**
         * 头像上传成功后返回的图片id
         *
         * @param id 头像id
         */
        void updateUserInfo(HashMap<String, String> userInfo, int id);
    }

}
