package com.zhiyicx.thinksnsplus.modules.home.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicPresenter;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailCommentEmptyItem;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListPresenter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe 发现页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class FindFragment extends TSListFragment<FindContract.Presenter, RechargeSuccessBean> implements FindContract.View{

    @Inject
    AuthRepository mAuthRepository;

    @Inject
    FindPresenter mFindPresenter;

    public FindFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static FindFragment newInstance() {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        Observable.create(subscriber -> {
            DaggerFindComponent
                    .builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .findPresenterModule(new FindPresenterModule(FindFragment.this))
                    .build()
                    .inject(FindFragment.this);
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        initData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }

    @Override
    protected int setSystemStatusBarCorlorResource() {
        return R.color.themeColor;
    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            super.initData();
        }
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.find);
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    /*@OnClick({R.id.find_info, R.id.find_chanel, R.id.find_active, R.id.find_music, R.id.find_buy,
            R.id.find_person, R.id.find_nearby, R.id.find_qa, R.id.find_rank})
    public void onClick(View view) {
        switch (view.getId()) {
                *//*
                  资讯
                 *//*
            case R.id.find_info:
                if (TouristConfig.INFO_LIST_CAN_LOOK || !mAuthRepository.isTourist()) {
                    startActivity(new Intent(getActivity(), InfoActivity.class));

                } else {
                    showLoginPop();
                }
                break;
                *//*
                  圈子
                 *//*
            case R.id.find_chanel:
                if (TouristConfig.CHENNEL_LIST_CAN_LOOK || !mAuthRepository.isTourist()) {
                    startActivity(new Intent(getActivity(), CircleMainActivity.class));
                } else {
                    showLoginPop();
                }
                break;
                *//*
                活动
                 *//*
            case R.id.find_active:
                break;
                *//*
                 音乐
                 *//*
            case R.id.find_music:
//                if (TouristConfig.MUSIC_LIST_CAN_LOOK || !mAuthRepository.isTourist()) {
//                    startActivity(new Intent(getActivity(), MusicListActivity.class));
//                } else {
//                    showLoginPop();
//                }
                String mUrl = "http://soft.imtt.qq.com/browser/tes/feedback.html";
                CustomWEBActivity.startToWEBActivity(getContext(), mUrl);

                break;
                *//*
                 极铺
                 *//*
            case R.id.find_buy:
                if (TouristConfig.JIPU_SHOP_CAN_LOOK || !mAuthRepository.isTourist()) {
                    CustomWEBActivity.startToWEBActivity(getContext(), ApiConfig.URL_JIPU_SHOP);
                } else {
                    showLoginPop();
                }
                break;
                *//*
                 找人
                 *//*
            case R.id.find_person:

                Intent itFollow = new Intent(getActivity(), FindSomeOneContainerActivity.class);
                Bundle bundleFollow = new Bundle();
                itFollow.putExtras(bundleFollow);
                startActivity(itFollow);
                break;
            case R.id.find_nearby:
                break;
                *//*
                 问答
                 *//*
            case R.id.find_qa:
                if (TouristConfig.MORE_QUESTION_LIST_CAN_LOOK || !mAuthRepository.isTourist()) {
                    Intent intent = new Intent(getActivity(), QA_Activity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    showLoginPop();
                }
                break;
                *//*
                 排行榜
                 *//*
            case R.id.find_rank:
                startActivity(new Intent(getActivity(), RankIndexActivity.class));
                break;
            default:
                break;
        }
    }*/

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        CandyItem candyItem = new CandyItem(getContext());
        adapter.addItemViewDelegate(candyItem);
        adapter.addItemViewDelegate(new InfoDetailCommentEmptyItem(R.mipmap.ico_bg_color));
        return adapter;
    }

}
