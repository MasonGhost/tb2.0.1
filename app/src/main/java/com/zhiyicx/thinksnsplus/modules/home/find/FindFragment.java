package com.zhiyicx.thinksnsplus.modules.home.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.circle.main.CircleMainActivity;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicListActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.QA_Activity;
import com.zhiyicx.thinksnsplus.modules.rank.main.container.RankIndexActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;

import javax.inject.Inject;

import butterknife.OnClick;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @Describe 发现页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class FindFragment extends TSFragment {

    @Inject
    AuthRepository mAuthRepository;

    public FindFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Observable.create(subscriber -> {
            AppApplication.AppComponentHolder.getAppComponent().inject(FindFragment.this);
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                }, Throwable::printStackTrace);
    }

    public static FindFragment newInstance() {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

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
    protected void initData() {
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
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

    @OnClick({R.id.find_info, R.id.find_chanel, R.id.find_active, R.id.find_music, R.id.find_buy,
            R.id.find_person, R.id.find_nearby, R.id.find_qa, R.id.find_rank})
    public void onClick(View view) {
        switch (view.getId()) {
                /*
                  资讯
                 */
            case R.id.find_info:
                if (TouristConfig.INFO_LIST_CAN_LOOK || !mAuthRepository.isTourist()) {
                    startActivity(new Intent(getActivity(), InfoActivity.class));

                } else {
                    showLoginPop();
                }
                break;
                /*
                  圈子
                 */
            case R.id.find_chanel:
                if (TouristConfig.CHENNEL_LIST_CAN_LOOK || !mAuthRepository.isTourist()) {
//                    startActivity(new Intent(getActivity(), ChannelListActivity.class));
                    startActivity(new Intent(getActivity(), CircleMainActivity.class));

                } else {
                    showLoginPop();
                }
                break;
                /*
                活动
                 */
            case R.id.find_active:
                break;
                /*
                 音乐
                 */
            case R.id.find_music:
                if (TouristConfig.MUSIC_LIST_CAN_LOOK || !mAuthRepository.isTourist()) {
                    startActivity(new Intent(getActivity(), MusicListActivity.class));
                } else {
                    showLoginPop();
                }
                break;
                /*
                 极铺
                 */
            case R.id.find_buy:
                if (TouristConfig.JIPU_SHOP_CAN_LOOK || !mAuthRepository.isTourist()) {
                    CustomWEBActivity.startToWEBActivity(getContext(), ApiConfig.URL_JIPU_SHOP);
                } else {
                    showLoginPop();
                }
                break;
                /*
                 找人
                 */
            case R.id.find_person:

                Intent itFollow = new Intent(getActivity(), FindSomeOneContainerActivity.class);
                Bundle bundleFollow = new Bundle();
                itFollow.putExtras(bundleFollow);
                startActivity(itFollow);
                break;
            case R.id.find_nearby:
                break;
                /*
                 问答
                 */
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
                /*
                 排行榜
                 */
            case R.id.find_rank:
                startActivity(new Intent(getActivity(), RankIndexActivity.class));
                break;
            default:
                break;
        }
    }

}
