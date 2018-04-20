package com.zhiyicx.thinksnsplus.modules.home.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.tbcandy.CandyBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.tb.exchange.ExchangeActivity;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailActivity;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.CountTimerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
public class FindFragment extends TSListFragment<FindContract.Presenter, CandyBean> implements FindContract.View {

    /*@Inject
    AuthRepository mAuthRepository;*/

    /*@Inject
    FindPresenter mFindPresenter;*/

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
        /*Observable.create(subscriber -> {
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
                });*/
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_find;
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
    public void onNetResponseSuccess(@NotNull List<CandyBean> data, boolean isLoadMore) {
        for(int i = 0; i < 8; i++){
            CandyBean candyBean = new CandyBean();
            candyBean.setName("BTP");
            candyBean.setEnd_sec(86430);
            candyBean.setCandy_num(10000);
            candyBean.setTotal_tbmark(6000);
            candyBean.setTbmark(1000);
            candyBean.setStatus(2);
            data.add(candyBean);
        }
        CandyBean candyBean = new CandyBean();
        candyBean.setName("EOSTOKEN");
        candyBean.setEnd_sec(64345);
        candyBean.setCandy_num(90000);
        candyBean.setTotal_tbmark(2000);
        candyBean.setTbmark(3000);
        candyBean.setStatus(2);
        data.add(candyBean);

        CandyBean candyBean2 = new CandyBean();
        candyBean2.setName("EOSTOKEN");
        candyBean2.setEnd_sec(60000);
        candyBean2.setCandy_num(90000);
        candyBean2.setTotal_tbmark(4000);
        candyBean2.setTbmark(0);
        candyBean2.setStatus(0);
        data.add(candyBean2);
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<CandyBean>(mActivity, R.layout.item_candy, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CandyBean candyBean, int position) {
                FilterImageView filterImageView = holder.getView(R.id.iv_headpic);
                LinearLayout mLlCandy = holder.getView(R.id.ll_candy);
                switch (position % 5){
                    case 0: {
                        mLlCandy.setBackgroundResource(R.mipmap.ico_rectangle_candy);
                        break;
                    }
                    case 1: {
                        mLlCandy.setBackgroundResource(R.mipmap.ico_rectangle2_candy);
                        break;
                    }
                    case 2: {
                        mLlCandy.setBackgroundResource(R.mipmap.ico_rectangle3_candy);
                        break;
                    }
                    case 3: {
                        mLlCandy.setBackgroundResource(R.mipmap.ico_rectangle2_candy);
                        break;
                    }
                    case 4: {
                        mLlCandy.setBackgroundResource(R.mipmap.ico_rectangle4_candy);
                        break;
                    }
                    default:
                        break;
                }
                //头像，记得去掉注释
                /*Glide.with(BaseApplication.getContext())
                        .load(ImageUtils.imagePathConvertV2(candyBean.getCandy_cate().getLogo(), filterImageView
                                        .getWidth(), filterImageView.getHeight(),
                                ImageZipConfig.IMAGE_80_ZIP))
                        .placeholder(R.drawable.shape_default_image)
                        .error(R.drawable.shape_default_image)
                        .into(filterImageView);*/
                holder.setText(R.id.tv_coin_name, candyBean.getName());
                holder.setText(R.id.tv_coin_all, getString(R.string.tbmark_joined_all,
                        String.valueOf(candyBean.getCandy_num()),
                        candyBean.getName(),
                        String.valueOf(candyBean.getTotal_tbmark())));
                holder.getView(R.id.ll_joined).setVisibility(candyBean.getTbmark() <= 0 ? View.GONE : View.VISIBLE);
                holder.setText(R.id.tv_join_count, (candyBean.getTbmark() / 1000) + "K");
                TextView mTvBtnExchange = holder.getView(R.id.bt_exchange);
                mTvBtnExchange.setEnabled(candyBean.getStatus() == 2 ? true : false);
                CountTimerView mCountTimerView = holder.getView(R.id.count_timer);
                mCountTimerView.setOnTickListener(new CountTimerView.OnTickListener() {
                    @Override
                    public void onTick(long l) {
                        if (l > 86400){
                            mCountTimerView.setVisibility(View.GONE);
                            int hour = (int) (l / (60 * 60));
                            int days = hour / 24;
                            holder.setText(R.id.tv_time_count, getString(R.string.tbmark_end_days,
                                    String.valueOf(days)));
                        } else {
                            mCountTimerView.setVisibility(View.VISIBLE);
                            holder.setText(R.id.tv_time_count, getString(R.string.tbmark_end_time));
                        }
                    }
                });
                mCountTimerView.setOnStopListener(new CountTimerView.OnStopListener() {
                    @Override
                    public void isStop() {
                        holder.setText(R.id.tv_time_count, getString(R.string.tbmark_end));
                        mTvBtnExchange.setEnabled(false);
                        mCountTimerView.setVisibility(View.GONE);
                    }
                });
                mCountTimerView.setTime(candyBean.getEnd_sec());
                mTvBtnExchange.setOnClickListener(view -> {
                    Intent intent = new Intent(mActivity, ExchangeActivity.class);
                    mActivity.startActivity(intent);
                });
            }
        };
        return adapter;
    }
}
