package com.zhiyicx.thinksnsplus.modules.tb.word;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WordResourceBean;
import com.zhiyicx.thinksnsplus.data.beans.WordResultBean;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsConstract;
import com.zhiyicx.thinksnsplus.modules.report.ReportContract;

public class WordContract {
    interface View extends ITSListView<InfoCommentListBean, WordContract.Presenter> {
        Long getNewsId();

        void wordSuccess();
    }

    interface Presenter extends ITSListPresenter<InfoCommentListBean> {

        void word(int reply_id, String content);

        void deleteComment(InfoCommentListBean data);
    }
}
