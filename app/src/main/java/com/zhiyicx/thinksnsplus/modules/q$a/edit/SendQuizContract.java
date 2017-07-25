package com.zhiyicx.thinksnsplus.modules.q$a.edit;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseQuizRepository;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public interface SendQuizContract {

    interface View extends IBaseView<Presenter>{
        List<ImageBean> getImageList();
    }

    interface Presenter extends IBasePresenter{
        void postImage();
    }

    interface Repository extends IBaseQuizRepository{

    }
}
