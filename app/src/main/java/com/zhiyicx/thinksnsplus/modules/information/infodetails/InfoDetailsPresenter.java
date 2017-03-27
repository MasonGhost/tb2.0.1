package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoCommentListBeanDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean.SEND_ING;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class InfoDetailsPresenter extends BasePresenter<InfoDetailsConstract.Repository,
        InfoDetailsConstract.View> implements InfoDetailsConstract.Presenter {

    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    InfoCommentListBeanDaoImpl mInfoCommentListBeanDao;

    @Inject
    public InfoDetailsPresenter(InfoDetailsConstract.Repository repository, InfoDetailsConstract
            .View rootView) {
        super(repository, rootView);
    }

    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        mRepository.getInfoCommentList(mRootView.getNewsId() + "", maxId, 0L)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<List<InfoCommentListBean>>() {
                    @Override
                    protected void onSuccess(List<InfoCommentListBean> data) {
                        mInfoCommentListBeanDao.saveMultiData(data);

                        List<InfoCommentListBean> localComment = mInfoCommentListBeanDao
                                .getMySendingComment();
                        ToastUtils.showToast(localComment.isEmpty()+"");
                        if (!localComment.isEmpty()) {
                            for (int i = 0; i < localComment.size(); i++) {
                                localComment.get(i).setFromUserInfoBean(mUserInfoBeanGreenDao
                                        .getSingleDataFromCache(localComment.get(i).getUser_id()));
                                if (localComment.get(i).getReply_to_user_id() != 0) {
                                    localComment.get(i).setToUserInfoBean(mUserInfoBeanGreenDao
                                            .getSingleDataFromCache(localComment.get(i)
                                                    .getReply_to_user_id()));
                                }
                            }
                            localComment.addAll(data);
                            data.clear();
                            data.addAll(localComment);
                        }

                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
    }

    @Override
    public void shareInfo() {
        ShareContent shareContent = new ShareContent();
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void handleCollect(boolean isCollected, String news_id) {
        mRepository.handleCollect(isCollected, news_id);
    }

    @Override
    public void sendComment(int reply_id, String content) {
        InfoCommentListBean createComment = new InfoCommentListBean();

        createComment.setState(SEND_ING);

        createComment.setComment_content(content);

        createComment.setReply_to_user_id(reply_id);

        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        createComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());

        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setComment_mark(Long.parseLong(comment_mark));

        if (reply_id == 0) {// 回复资讯
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id((long) reply_id);
            createComment.setToUserInfoBean(userInfoBean);
        } else {
            createComment.setToUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                    (long) reply_id));
        }
        createComment.setFromUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache((long)
                AppApplication.getmCurrentLoginAuth().getUser_id()));
        mInfoCommentListBeanDao.insertOrReplace(createComment);
        mRootView.getListDatas().add(1, createComment);
        mRootView.refreshData();
        mRepository.sendComment(content, mRootView.getNewsId(), reply_id,
                createComment.getComment_mark());
    }

    @Override
    public List<InfoCommentListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return new ArrayList<>();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoCommentListBean> data) {
        return false;
    }
}
