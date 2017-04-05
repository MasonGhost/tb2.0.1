package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;


import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicDetailRepository;

import javax.inject.Inject;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAILS_FORMAT;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 专辑详情
 */
@FragmentScoped
public class MusicDetailPresenter extends BasePresenter<MusicDetailContract.Repository,
        MusicDetailContract.View> implements MusicDetailContract.Presenter {

    @Inject
    MusicDetailRepository mMusicDetailRepository;

    @Inject
    public SharePolicy mSharePolicy;

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
        mMusicDetailRepository.getMusicAblum(id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<MusicAlbumDetailsBean>() {
                    @Override
                    protected void onSuccess(MusicAlbumDetailsBean data) {
                        mRootView.setMusicAblum(data);
                    }

                    @Override
                    protected void onFailure(String message) {

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
                    protected void onFailure(String message) {

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
        int countChange = isUnCollected ? 1 : -1;
        mRootView.getCurrentAblum().setCollect_count(mRootView.getCurrentAblum().getCollect_count() + countChange);
        mRootView.setCollect(isUnCollected);
        mMusicDetailRepository.handleCollect(isUnCollected, special_id);

    }

    @Override
    public void shareMusicAlbum() {
        ShareContent shareContent = new ShareContent();

        shareContent.setTitle(mRootView.getCurrentAblum().getTitle());
        shareContent.setImage(ImageUtils.imagePathConvert(mRootView.getCurrentAblum()
                .getStorage() + "", 100));
        shareContent.setUrl("www.baidu.com");

        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());

        mMusicDetailRepository.shareAblum(mRootView.getCurrentAblum().getId()+"");
    }
}
