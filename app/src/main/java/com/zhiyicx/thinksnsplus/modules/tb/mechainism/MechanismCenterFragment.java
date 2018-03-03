package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.utils.MarkDownRule;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.tiagohm.markdownview.MarkdownView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

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



    private UserInfoBean mUserInfoBean;


    @Inject
    UserInfoRepository mUserInfoRepository;
    private Subscription subscribe;
    private MerchainContentWebLoadView mMerchainContentWebLoadView;

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
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        mUserInfoBean = getArguments().getParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA);
         mMerchainContentWebLoadView=new MerchainContentWebLoadView(mActivity,rootView);
    }

    @Override
    protected void initData() {
        subscribe = mUserInfoRepository.getMerchainUserInfo(mUserInfoBean.getUser_id().intValue())
                .subscribe(new BaseSubscribeForV2<MerchainInfo>() {
                    @Override
                    protected void onSuccess(MerchainInfo data) {
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
}
