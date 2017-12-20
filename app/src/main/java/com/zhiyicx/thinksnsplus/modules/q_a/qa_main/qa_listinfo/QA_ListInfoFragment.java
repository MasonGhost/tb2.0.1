package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container.QA_InfoContainerFragment;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment.ITEM_SPACING;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_ListInfoFragment extends TSListFragment<QA_ListInfoConstact.Presenter, QAListInfoBean>
        implements QA_ListInfoConstact.View, SpanTextClickable.SpanTextClickListener {

    public static final String BUNDLE_QA_TYPE = "qa_type";

    private String mQAInfoType;

    public String[] QA_TYPES;

    private PayPopWindow mPayWatchPopWindow; // 围观答案

    @Inject
    QA_ListInfoFragmentPresenter mQA_listInfoFragmentPresenter;

    public static QA_ListInfoFragment newInstance(String params) {
        QA_ListInfoFragment fragment = new QA_ListInfoFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_QA_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getQAInfoType() {
        return mQAInfoType;
    }

    @Override
    public void showDeleteSuccess() {
        showSnackSuccessMessage(getString(R.string.qa_question_delete_success));
    }

    @Override
    protected void onEmptyViewClick() {
        mRefreshlayout.autoRefresh();
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QA_TYPES = getResources().getStringArray(R.array.qa_net_type);
        mQAInfoType = getArguments().getString(BUNDLE_QA_TYPE);
        DaggerQA_ListInfoComponent
                .builder().appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .qA_listInfoFragmentPresenterModule(new QA_listInfoFragmentPresenterModule(this))
                .build().inject(this);

    }

    @Override
    protected void initData() {
        super.initData();
        // 控制 + 号按钮的隐藏显示
//        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                QA_InfoContainerFragment infoContainerFragment = (QA_InfoContainerFragment) getParentFragment();
//                infoContainerFragment.addBtnAnimation(dy > 0);
//            }
//        });

    }

    /**
     * 是否进入页面进行懒加载
     *
     * @return
     */
    @Override
    protected boolean isLayzLoad() {
        return true;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        requestNetData(null, maxId, mQAInfoType, isLoadMore);
    }

    private void requestNetData(String subject, Long maxId, String type, boolean isLoadMore) {
        mPresenter.requestNetData(subject, maxId, type, isLoadMore);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        QAListInfoAdapter adapter = new QAListInfoAdapter(getActivity(), mListDatas) {
            @Override
            protected int getExcellentTag(boolean isExcellent) {
                boolean isNewOrExcellent = getQAInfoType().equals(QA_TYPES[1]) || getQAInfoType().equals(QA_TYPES[3]);
                return isNewOrExcellent ? 0 : (isExcellent ? R.mipmap.icon_choice : 0);
            }

            @Override
            protected boolean isTourist() {
                return mPresenter.handleTouristControl();
            }

            @Override
            protected int getRatio() {
                return mPresenter.getRatio();
            }
        };
        adapter.setSpanTextClickListener(this);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mPresenter.handleTouristControl() || position < 0) {
                    return;
                }
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                QAListInfoBean listInfoBean = mListDatas.get(position);
                bundle.putSerializable(BUNDLE_QUESTION_BEAN, listInfoBean);
                intent.putExtra(BUNDLE_QUESTION_BEAN, bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    protected Long getMaxId(@NotNull List<QAListInfoBean> data) {
        return (long) mListDatas.size();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void onSpanClick(long answer_id, int position) {
        initOnlookPopWindow(answer_id, position);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE)
    public void updateList(Bundle bundle) {
        if (bundle != null) {
            QAListInfoBean qaListInfoBean = (QAListInfoBean) bundle.
                    getSerializable(EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE);
            if (qaListInfoBean != null) {
                for (int i = 0; i < mListDatas.size(); i++) {
                    if (qaListInfoBean.getId().equals(mListDatas.get(i).getId())) {
                        mListDatas.remove(i);
                        refreshData();
                        showDeleteSuccess();
                        break;
                    }
                }
            }
        }
    }

    private void initOnlookPopWindow(long answer_id, int pisotion) {
        mPayWatchPopWindow = PayPopWindow.builder()
                .with(getActivity())
                .isWrap(true)
                .isFocus(true)
                .isOutsideTouch(true)
                .buildLinksColor1(R.color.themeColor)
                .buildLinksColor2(R.color.important_for_content)
                .contentView(R.layout.ppw_for_center)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .buildDescrStr(String.format(getString(R.string.qa_pay_for_watch_answer_hint) + getString(R
                                .string.buy_pay_member),
                        PayConfig.realCurrency2GameCurrency(mPresenter.getSystemConfig().getOnlookQuestion(), mPresenter.getRatio())
                        , mPresenter.getGoldName()))
                .buildLinksStr(getString(R.string.qa_pay_for_watch))
                .buildTitleStr(getString(R.string.qa_pay_for_watch))
                .buildItem1Str(getString(R.string.buy_pay_in_payment))
                .buildItem2Str(getString(R.string.buy_pay_out))
                .buildMoneyStr(String.format(getString(R.string.buy_pay_money), PayConfig.realCurrency2GameCurrency(mPresenter.getSystemConfig()
                        .getOnlookQuestion(), mPresenter.getRatio())))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    mPresenter.payForOnlook(answer_id, pisotion);
                    mPayWatchPopWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> mPayWatchPopWindow.hide())
                .buildCenterPopWindowLinkClickListener(new PayPopWindow
                        .CenterPopWindowLinkClickListener() {
                    @Override
                    public void onLongClick() {

                    }

                    @Override
                    public void onClicked() {

                    }
                })
                .build();
        mPayWatchPopWindow.show();
    }

}
