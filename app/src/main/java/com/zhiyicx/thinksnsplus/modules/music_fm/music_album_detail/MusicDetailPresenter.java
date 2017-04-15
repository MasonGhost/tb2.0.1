package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;


import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.imsdk.core.autobahn.WampMessage;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicAlbumDetailsBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicDetailRepository;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_ABLUM_COLLECT;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 专辑详情
 */
@FragmentScoped
public class MusicDetailPresenter extends BasePresenter<MusicDetailContract.Repository,
        MusicDetailContract.View> implements MusicDetailContract.Presenter,OnShareCallbackListener {

    @Inject
    MusicDetailRepository mMusicDetailRepository;

    @Inject
    MusicAlbumDetailsBeanGreenDaoImpl mMusicAlbumDetailsBeanGreenDao;

    @Inject
    public SharePolicy mSharePolicy;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Inject
    public MusicDetailPresenter(MusicDetailContract.Repository repository, MusicDetailContract
            .View rootView) {
        super(repository, rootView);
    }

    /**
     * 将Presenter从传入fragment
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void getMusicAblum(String id) {
//        MusicAlbumDetailsBean cacheData = getCacheAblumDetail(Integer.valueOf(id));
//        if (cacheData != null) {
//            mRootView.setMusicAblum(cacheData);
//        }

        mMusicDetailRepository.getMusicAblum(id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<MusicAlbumDetailsBean>() {
                    @Override
                    protected void onSuccess(MusicAlbumDetailsBean data) {
                        mMusicAlbumDetailsBeanGreenDao.insertOrReplace(data);
                        mRootView.setMusicAblum(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    @Override
    public void getMusicDetails(String music_id) {
        mMusicDetailRepository.getMusicDetails(music_id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<MusicDetaisBean>() {
                    @Override
                    protected void onSuccess(MusicDetaisBean data) {

                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    @Override
    public void handleCollect(boolean isUnCollected, String special_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        int is_collect = mRootView.getCurrentAblum().getIs_collection();
        mRootView.getCurrentAblum().setIs_collection(is_collect == 0 ? 1 : 0);
        mRootView.getmMusicAlbumListBean().setIs_collection(is_collect);
        int countChange = isUnCollected ? 1 : -1;
        mRootView.getmMusicAlbumListBean().setCollect_count(mRootView.getCurrentAblum().getCollect_count() + countChange);
        mRootView.getCurrentAblum().setCollect_count(mRootView.getCurrentAblum().getCollect_count() + countChange);
        mMusicAlbumDetailsBeanGreenDao.insertOrReplace(mRootView.getCurrentAblum());
        EventBus.getDefault().post(mRootView.getmMusicAlbumListBean(),EVENT_ABLUM_COLLECT);
        mRootView.setCollect(isUnCollected);
        mMusicDetailRepository.handleCollect(isUnCollected, special_id);

    }

    @Override
    public void shareMusicAlbum() {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();

        shareContent.setTitle(mRootView.getCurrentAblum().getTitle());
        shareContent.setImage(ImageUtils.imagePathConvert(mRootView.getCurrentAblum()
                .getStorage() + "", 100));
        shareContent.setUrl("http://www.baidu.com");

        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());

        mMusicDetailRepository.shareAblum(mRootView.getCurrentAblum().getId() + "");
    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }

    @Override
    public MusicAlbumDetailsBean getCacheAblumDetail(int id) {
        return mMusicAlbumDetailsBeanGreenDao.getAblumByID(id);
    }
}
