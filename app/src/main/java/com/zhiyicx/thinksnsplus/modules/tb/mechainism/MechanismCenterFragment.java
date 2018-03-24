package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.yalantis.ucrop.util.FileUtils;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.tb.detail.MerchainMessagelistActivity;

import java.io.File;
import java.util.Locale;

import javax.inject.Inject;

import br.tiagohm.markdownview.MarkdownView;
import butterknife.BindView;
import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechanismCenterFragment extends TSFragment {
    @BindView(R.id.line_name)
    View mLineName;

    @BindView(R.id.line_intro)
    View mLineIntro;
    @BindView(R.id.line_content)
    View mLineContent;
    @BindView(R.id.line_web)
    View mLineWeb;
    @BindView(R.id.line_downlad)
    View mLineDownlad;
    @BindView(R.id.line_book)
    View mLineBook;

    @BindView(R.id.tv_cn_name)
    TextView mTvCnName;
    @BindView(R.id.ll_cn_name_container)
    LinearLayout mLlCnNameContainer;
    @BindView(R.id.tv_en_name)
    TextView mTvEnName;
    @BindView(R.id.ll_en_name_container)
    LinearLayout mLlEnNameContainer;
    @BindView(R.id.tv_introduce)
    TextView mTvIntroduce;
    @BindView(R.id.ll_introduce_container)
    LinearLayout mLlIntroduceContainer;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.ll_coin_container)
    LinearLayout mLlCoinContainer;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.ll_count_container)
    LinearLayout mLlCountContainer;
    @BindView(R.id.tv_currency)
    TextView mTvCurrency;
    @BindView(R.id.ll_currency_container)
    LinearLayout mLlCurrencyContainer;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.ll_price_container)
    LinearLayout mLlPriceContainer;
    @BindView(R.id.tv_online)
    TextView mTvOnline;
    @BindView(R.id.ll_online_container)
    LinearLayout mLlOnlineContainer;
    @BindView(R.id.tv_media)
    TextView mTvMedia;
    @BindView(R.id.ll_media_container)
    LinearLayout mLlMediaContainer;
    @BindView(R.id.tv_website)
    TextView mTvWebsite;
    @BindView(R.id.ll_website_container)
    LinearLayout mLlWebsiteContainer;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.ll_info_container)
    LinearLayout mLlInfoContainer;
    @BindView(R.id.tv_book)
    TextView mTvBook;
    @BindView(R.id.ll_book_container)
    LinearLayout mLlBookContainer;
    @BindView(R.id.mv_content)
    MarkdownView mContent;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.bt_top)
    TextView mBtFollow;


    private UserInfoBean mUserInfoBean;
    private int downloadId2;
    private MerchainInfo mMerchainInfo;


    @Inject
    UserInfoRepository mUserInfoRepository;
    private Subscription subscribe;
    private MerchainContentWebLoadView mMerchainContentWebLoadView;
    private String mPath;

    public static MechanismCenterFragment newInstance(Bundle bundle) {
        MechanismCenterFragment mechanismCenterFragment = new MechanismCenterFragment();
        mechanismCenterFragment.setArguments(bundle);
        return mechanismCenterFragment;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        FileDownloader.setup(mActivity);
        try {
            mPath = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath() + File.separator + "tbm" + File.separator;
        } catch (Exception e) {
            mPath = "tbm" + File.separator;
        }

        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        mUserInfoBean = getArguments().getParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA);
        mMerchainContentWebLoadView = new MerchainContentWebLoadView(mActivity, rootView);
    }

    @Override
    protected void initData() {
        subscribe = mUserInfoRepository.getMerchainUserInfo(mUserInfoBean.getUser_id().intValue())
                .subscribe(new BaseSubscribeForV2<MerchainInfo>() {
                    @Override
                    protected void onSuccess(MerchainInfo data) {
                        mMerchainInfo = data;
                        updateMerchainInfo(data);
                    }
                });

    }

    /**
     * 更新关注
     *
     * @param follower
     */
    public void updateFollowStat(boolean follower) {
        mBtFollow.setText(follower ? "进入公众号" : getString(R.string.follow));
    }

    private void updateMerchainInfo(MerchainInfo data) {
        if (!TextUtils.isEmpty(data.getNickname())) {
            mLineName.setVisibility(View.VISIBLE);
            mLlCnNameContainer.setVisibility(View.VISIBLE);
            mTvCnName.setText(data.getNickname());
        }
        if (!TextUtils.isEmpty(data.getIntroduce())) {
            mLineIntro.setVisibility(View.VISIBLE);
            mLlIntroduceContainer.setVisibility(View.VISIBLE);
            mTvIntroduce.setText(data.getIntroduce());
        }
///        目前不需要
//        if (!TextUtils.isEmpty(data.getCreated_at())) {
//            mLlCoinContainer.setVisibility(View.VISIBLE);
//            mTvTime.setText(data.getCreated_at());
//        }
//
//        if (!TextUtils.isEmpty(data.getOther_info())) {
//            mLineContent.setVisibility(View.VISIBLE);
//            mMerchainContentWebLoadView.setDetail(data);
//        }

        if (!TextUtils.isEmpty(data.getUrl())) {
            mLineWeb.setVisibility(View.VISIBLE);
            mLlWebsiteContainer.setVisibility(View.VISIBLE);
            mTvWebsite.setText(data.getUrl());
            mTvWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomWEBActivity.startToWEBActivity(getContext(), data.getUrl());
                }
            });
        }
        // 官网
        if (!TextUtils.isEmpty(data.getOther_info())) {
            mLineContent.setVisibility(View.VISIBLE);
            mLlInfoContainer.setVisibility(View.VISIBLE);
            mTvInfo.setText(data.getOther_info());
        }
        // 下载
        if (!TextUtils.isEmpty(data.getAndroid_download_url())) {
            mLineDownlad.setVisibility(View.VISIBLE);
            mLlCountContainer.setVisibility(View.VISIBLE);
            mLlCountContainer.setOnClickListener(v -> {
                CustomWEBActivity.startToWEBActivity(getContext(), data.getAndroid_download_url());
            });
        }

        if (TextUtils.isEmpty(data.getWhite_paper_name())) {
            mLlBookContainer.setVisibility(View.GONE);
        } else {
            mLineBook.setVisibility(View.VISIBLE);
            mLlBookContainer.setVisibility(View.VISIBLE);
            mTvBook.setText(data.getWhite_paper_name());
            mTvBook.setOnClickListener(v -> {
                CustomWEBActivity.startToWEBActivity(getContext(), String.format(Locale.getDefault(), ApiConfig.APP_PATH_STORAGE_GET_FILE,
                        mMerchainInfo.getWhite_paper() + ""));
//                if (mMerchainInfo == null || mMerchainInfo.getWhite_paper() == 0) {
//                    return;
//                }
//                try {
//                    Intent intent = FileUtils.openFile(getBookFilePath(data), mActivity.getApplicationContext());
//                    if (intent == null) {
//                        downloadId2 = createDownloadTask().start();
//                    } else {
//                        startActivity(intent);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            });
        }
        updateMerchainRemark(data);

        mBtFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getString(R.string.follow).equals(mBtFollow.getText())) {
                    // 关注
                    handleFollow();
                } else {
                    // 进入公众号
                    Intent intent = new Intent(getActivity(), MerchainMessagelistActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MerchainMessagelistActivity.BUNDLE_USER, mUserInfoBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 更新机构用户的备注
     *
     * @param data
     */
    private void updateMerchainRemark(MerchainInfo data) {
        if (getParentFragment() != null && getParentFragment() instanceof OnMerchainismInfoChangedListener) {
            ((OnMerchainismInfoChangedListener) getParentFragment()).onMerchainismInfoChanged(data);
        }

    }

    /**
     * 更新机构用户的备注
     */
    private void handleFollow() {
        if (getParentFragment() != null && getParentFragment() instanceof OnMerchainismInfoChangedListener) {
            ((OnMerchainismInfoChangedListener) getParentFragment()).handleFollow();
        }

    }

    @NonNull
    private String getBookFilePath(MerchainInfo merchainInfo) {
        return mPath + merchainInfo.getWhite_paper_name() + ".pdf";
    }

    private BaseDownloadTask createDownloadTask() {
        final String url;

        url = String.format(Locale.getDefault(), ApiConfig.APP_PATH_STORAGE_GET_FILE, mMerchainInfo.getWhite_paper() + "");

        return FileDownloader.getImpl().create(url + "?token=" + AppApplication.getmCurrentLoginAuth().getToken())
                .setPath(getBookFilePath(mMerchainInfo), false)
                .setCallbackProgressTimes(100)
                .setMinIntervalUpdateSpeed(200)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        mProgressBar.setVisibility(View.VISIBLE);
                        if (totalBytes == -1) {
                            // chunked transfer encoding data
                            mProgressBar.setIndeterminate(true);
                        } else {
                            mProgressBar.setMax(totalBytes);
                            mProgressBar.setProgress(soFarBytes);
                        }

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);


                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        mProgressBar.setVisibility(View.GONE);
                        showSnackSuccessMessage("下载成功，文件位于 tbm 下");
                        try {
                            startActivity(FileUtils.openFile(getBookFilePath(mMerchainInfo), mActivity.getApplicationContext
                                    ()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);

                    }
                });
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tb_mechainsm;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        mMerchainContentWebLoadView.destroyedWeb();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pause(downloadId2);

    }


    @Override
    public void onPause() {
        mMerchainContentWebLoadView.getContentWebView().onPause();
        mMerchainContentWebLoadView.getContentWebView().pauseTimers();
        super.onPause();

    }

    @Override
    public void onResume() {
        mMerchainContentWebLoadView.getContentWebView().onResume();
        mMerchainContentWebLoadView.getContentWebView().resumeTimers();
        super.onResume();

    }

    public interface OnMerchainismInfoChangedListener {
        void onMerchainismInfoChanged(MerchainInfo merchainInfo);

        void handleFollow();
    }

}
