package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicCommentContract {

    interface View extends ITSListView<MusicCommentListBean,Presenter>{

    }

    interface Presenter extends ITSListPresenter<MusicCommentListBean>{

    }

    interface Repository{

    }
}
