package com.zhiyicx.thinksnsplus.modules.gallery;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Author Jliuer
 * @Date 2017/06/29/9:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface GalleryConstract {

    interface View extends IBaseView<Presenter>{
        ImageBean getCurrentImageBean();
        void reLoadImage();
    }

    interface Presenter extends IBaseTouristPresenter{
        void checkNote(int note);

        void payNote(Long feed_id,int imagePosition,int note);
    }

}
