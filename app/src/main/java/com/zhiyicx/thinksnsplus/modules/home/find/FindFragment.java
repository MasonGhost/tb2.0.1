package com.zhiyicx.thinksnsplus.modules.home.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album.MusicListActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Describe 发现页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class FindFragment extends TSFragment {

    @BindView(R.id.find_info)
    CombinationButton mFindInfo;
    @BindView(R.id.find_chanel)
    CombinationButton mFindChanel;
    @BindView(R.id.find_active)
    CombinationButton mFindActive;
    @BindView(R.id.find_music)
    CombinationButton mFindMusic;
    @BindView(R.id.find_buy)
    CombinationButton mFindBuy;
    @BindView(R.id.find_person)
    CombinationButton mFindPerson;
    @BindView(R.id.find_nearby)
    CombinationButton mFindNearby;

    public FindFragment() {
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

    @OnClick({R.id.find_info, R.id.find_chanel, R.id.find_active, R.id.find_music, R.id.find_buy,
            R.id.find_person, R.id.find_nearby})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_info:
                startActivity(new Intent(getActivity(), InfoActivity.class));
                break;
            case R.id.find_chanel:
                break;
            case R.id.find_active:
                break;
            case R.id.find_music:
                startActivity(new Intent(getActivity(), MusicListActivity.class));
                break;
            case R.id.find_buy:
                break;
            case R.id.find_person:
                break;
            case R.id.find_nearby:
                break;
            default:
                break;
        }
    }
}
