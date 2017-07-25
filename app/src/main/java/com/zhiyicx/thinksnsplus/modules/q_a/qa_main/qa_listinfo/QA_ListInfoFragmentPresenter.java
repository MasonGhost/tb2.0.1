package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.QA_LIstInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/25/13:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_ListInfoFragmentPresenter extends AppBasePresenter<QA_ListInfoConstact.Repository,QA_ListInfoConstact.View> implements QA_ListInfoConstact.Presenter {

    @Inject
    public QA_ListInfoFragmentPresenter(QA_ListInfoConstact.Repository repository, QA_ListInfoConstact.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean istourist() {
        return false;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public boolean handleTouristControl() {
        return false;
    }

    @Override
    public List<QA_LIstInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QA_LIstInfoBean> data, boolean isLoadMore) {
        return false;
    }
}
