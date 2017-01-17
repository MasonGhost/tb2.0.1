package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.StorageTaskBean;
import com.zhiyicx.thinksnsplus.modules.login.LoginContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;

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
        void setAreaData(ArrayList<AreaBean> options1Items, ArrayList<ArrayList<AreaBean>> options2Items);

    }

    interface Repository {
        /**
         * 从本地文件获取全国所有的省市
         */
        Observable<ArrayList<AreaBean>> getAreaList();

        /**
         * 修改用户头像
         */
        Observable<BaseJson<StorageTaskBean>> changeUserHeadIcon(String hash, String fileName, Map<String, String> filePathList);
    }

    interface Presenter extends IBasePresenter {
        void getAreaData();

        void changeUserHeadIcon(String hash, String fileName, Map<String, String> filePathList);
    }
}
