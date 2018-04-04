package com.zhiyicx.thinksnsplus.modules.tb.word;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoCommentListBeanDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean.SEND_ING;

@FragmentScoped
public class WordPresenter extends AppBasePresenter<WordContract.View>
        implements WordContract.Presenter{

    @Inject
    InfoCommentListBeanDaoImpl mInfoCommentListBeanDao;

    @Inject
    BaseInfoRepository mBaseInfoRepository;

    @Inject
    public WordPresenter(WordContract.View rootView) {
        super( rootView);
    }

    @Override
    public void word(int reply_id, String content) {
        InfoCommentListBean createComment = new InfoCommentListBean();

        createComment.setInfo_id(mRootView.getNewsId().intValue());

        createComment.setState(SEND_ING);

        createComment.setComment_content(content);

        createComment.setReply_to_user_id(reply_id);

        createComment.setId(-1L);

        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        createComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());

        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setComment_mark(Long.parseLong(comment_mark));

        if (reply_id == 0) {// 评论资讯
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(0L);
            createComment.setToUserInfoBean(userInfoBean);
        } else {
            createComment.setToUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                    (long) reply_id));
        }
        createComment.setFromUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                AppApplication.getmCurrentLoginAuth().getUser_id()));
        mInfoCommentListBeanDao.insertOrReplace(createComment);
        /*if (mRootView.getListDatas().get(0).getComment_content() == null) {
            mRootView.getListDatas().remove(0);// 去掉占位图
        }*/
        mRootView.getListDatas().add(0, createComment);
        mRootView.refreshData();
        mBaseInfoRepository.sendComment(content, mRootView.getNewsId(), reply_id,
                createComment.getComment_mark());
        mRootView.wordSuccess();

    }

    @Override
    public void deleteComment(InfoCommentListBean data) {
        mInfoCommentListBeanDao.deleteSingleCache(data);
        mRootView.getListDatas().remove(data);
        if (mRootView.getListDatas().size() == 0) {// 占位
            InfoCommentListBean emptyData = new InfoCommentListBean();
            mRootView.getListDatas().add(emptyData);
        }
        mBaseInfoRepository.deleteComment(mRootView.getNewsId().intValue(), data.getId().intValue());
        mRootView.refreshData();
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mBaseInfoRepository.getMyInfoCommentListV2(mRootView.getNewsId() + "", maxId, 0L)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<InfoCommentListBean>>() {
                    @Override
                    protected void onSuccess(List<InfoCommentListBean> data) {
                        List<InfoCommentListBean> newList = dealComment(data, maxId);
                        mRootView.onNetResponseSuccess(newList, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        //handleInfoHasBeDeleted(code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoCommentListBean> data, boolean isLoadMore) {
        return false;
    }

    private List<InfoCommentListBean> dealComment(List<InfoCommentListBean> infoCommentBean, long max_id) {
        List<InfoCommentListBean> all = new ArrayList<>();
        if (max_id == 0) {
            List<InfoCommentListBean> localComment = mInfoCommentListBeanDao
                    .getMySendingComment(mRootView.getNewsId());
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
                all.addAll(localComment);
            }
        }
        if (infoCommentBean != null) {
            mInfoCommentListBeanDao.saveMultiData(infoCommentBean);
            all.addAll(infoCommentBean);
        }
        return all;
    }
}
