package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserTagBeanGreenDaoImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */

public class AddInfoPresenter extends AppBasePresenter<AddInfoContract.Repository, AddInfoContract.View>
        implements AddInfoContract.Presenter {

    @Inject
    InfoTypeBeanGreenDaoImpl mInfoTypeBeanGreenDao;

    @Inject
    public AddInfoPresenter(AddInfoContract.Repository repository, AddInfoContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public List<InfoTypeCatesBean> getInfoTypeBean() {
        return mInfoTypeBeanGreenDao.getAllCatesLists();
    }

}
