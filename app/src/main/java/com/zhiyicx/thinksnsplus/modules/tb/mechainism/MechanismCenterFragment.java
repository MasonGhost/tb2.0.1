package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;

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
        mPath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "tbm";

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

    private void updateMerchainInfo(MerchainInfo data) {
        mTvCnName.setText(data.getNickname());
        mTvIntroduce.setText(data.getIntroduce());
        mTvTime.setText(data.getCreated_at());
        mMerchainContentWebLoadView.setDetail(data);
        mTvBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMerchainInfo == null || mMerchainInfo.getPhoto() == 0) {
                    return;
                }
                downloadId2 = createDownloadTask().start();
            }
        });
    }

    private BaseDownloadTask createDownloadTask() {
        final String url;

        url = String.format(Locale.getDefault(), ApiConfig.APP_PATH_STORAGE_GET_FILE, mMerchainInfo.getPhoto() + "");

        return FileDownloader.getImpl().create(url + "?token=" + AppApplication.getmCurrentLoginAuth().getToken())
                .setPath(mPath + ".pdf", false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        System.out.println(" ------pending--------" + soFarBytes);

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
                        System.out.println(" ------progress--------" + soFarBytes);

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        System.out.println(" ------error--------");

                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        System.out.println(" ------connected--------");


                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                        System.out.println(" ------paused--------");

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        mProgressBar.setVisibility(View.GONE);
                        showSnackSuccessMessage("下载成功，文件位于 tbm 下");
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                        System.out.println(" ------warn--------");

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
        if (subscribe != null && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pause(downloadId2);

    }
}
