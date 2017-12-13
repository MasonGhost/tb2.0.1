package com.zhiyicx.thinksnsplus.modules.home.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PermissionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.circle.list.ChannelListActivity;
import com.zhiyicx.thinksnsplus.modules.circle.main.CircleMainActivity;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicListActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.QA_Activity;
import com.zhiyicx.thinksnsplus.modules.rank.main.container.RankIndexActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportFragment;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * @Describe 发现页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class FindFragment extends TSFragment {


    private ActionPopupWindow mActionPopupWindow;

    @Inject
    AuthRepository mAuthRepository;

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
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
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

    private void initPermissionPopUpWindow() {
        if (mActionPopupWindow != null) {
            return;
        }
        String model = android.os.Build.MODEL;
        final boolean isOppoR9s = model.equalsIgnoreCase("oppo r9s");
        mActionPopupWindow = PermissionPopupWindow.builder()
                .permissionName(getString(com.zhiyicx.baseproject.R.string.windows_permission))
                .with(getActivity())
                .bottomStr(getString(com.zhiyicx.baseproject.R.string.cancel))

                .item1Str(getString(isOppoR9s ? com.zhiyicx.baseproject.R.string
                        .oppo_setting_windows_permission_hint :
                        com.zhiyicx.baseproject.R.string.setting_windows_permission_hint))

                .item2Str(getString(com.zhiyicx.baseproject.R.string.setting_permission))
                .item2ClickListener(() -> {
                    mActionPopupWindow.hide();
                    if (isOppoR9s) {
                        DeviceUtils.startAppByPackageName(getActivity(), "com.coloros.safecenter");
                    } else {
                        DeviceUtils.openAppDetail(getActivity());
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mActionPopupWindow.hide();
                    }
                })
                .isFocus(true)
                .isOutsideTouch(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .build();
    }
}
