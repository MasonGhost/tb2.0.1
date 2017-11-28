package com.zhiyicx.thinksnsplus.modules.circle.create;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.modules.circle.create.types.CircleTyepsActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.LocationRecommentActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search.LocationSearchFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Jliuer
 * @Date 2017/11/21/16:57
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CreateCircleFragment extends TSFragment<CreateCircleContract.Presenter>
        implements CreateCircleContract.View, PhotoSelectorImpl.IPhotoBackListener {

    private static final int REQUST_CODE_AREA = 8000;
    private static final int REQUST_CODE_CATEGORY = 5000;

    @BindView(R.id.iv_head_icon)
    ImageView mIvHeadIcon;
    @BindView(R.id.ll_head_icon_container)
    LinearLayout mLlHeadIconContainer;
    @BindView(R.id.rl_change_head_container)
    RelativeLayout mRlChangeHeadContainer;
    @BindView(R.id.et_circle_name)
    DeleteEditText mEtCircleName;
    @BindView(R.id.tv_circle_type)
    TextView mTvCircleType;
    @BindView(R.id.ll_type_container)
    LinearLayout mLlTypeContainer;
    @BindView(R.id.fl_tags)
    TagFlowLayout mFlTags;
    @BindView(R.id.tv_tag_hint)
    TextView mTvTagHint;
    @BindView(R.id.ll_tag_container)
    LinearLayout mLlTagContainer;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.ll_location_container)
    LinearLayout mLlLocationContainer;
    @BindView(R.id.et_circle_introduce)
    UserInfoInroduceInputView mEtCircleIntroduce;
    @BindView(R.id.wc_synchro)
    SwitchCompat mWcSynchro;
    @BindView(R.id.ll_synchro)
    LinearLayout mLlSynchro;
    @BindView(R.id.wc_block)
    SwitchCompat mWcBlock;
    @BindView(R.id.ll_block)
    LinearLayout mLlBlock;
    @BindView(R.id.cb_toll)
    CheckBox mCbToll;
    @BindView(R.id.ll_charge)
    LinearLayout mLlCharge;
    @BindView(R.id.cb_free)
    CheckBox mCbFree;
    @BindView(R.id.tv_notice)
    UserInfoInroduceInputView mTvNotice;
    @BindView(R.id.ll_notice)
    LinearLayout mLlNotice;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;

    // 图片选择弹框
    private ActionPopupWindow mPhotoPopupWindow;
    private PhotoSelectorImpl mPhotoSelector;

    private String mCurrentShowLocation;

    @Override
    protected String setRightTitle() {
        return getString(R.string.create);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.create_circle);
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .SHAPE_SQUARE))
                .build().photoSelectorImpl();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUST_CODE_AREA && data != null && data.getExtras() != null) {
            LocationBean locationBean = data.getExtras().getParcelable(LocationSearchFragment.BUNDLE_DATA);
            if (locationBean != null) {
                String loacaiton = LocationBean.getlocation(locationBean);
                setCity(loacaiton);
            }
        } else if (requestCode == REQUST_CODE_CATEGORY && data != null && data.getExtras() != null) {
            
        }

    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {

    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_create_circle;
    }

    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {
        if (mPhotoPopupWindow != null) {
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.choose_from_photo))
                .item2Str(getString(R.string.choose_from_camera))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 选择相册，单张
                    mPhotoSelector.getPhotoListFromSelector(1, null);
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(null);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
    }

    /**
     * 设置城市
     */
    private void setCity(String city) {
        mCurrentShowLocation = city;
        try {
            String[] data = city.split("，");
            city = "";
            if (data.length > 2) {
                city = data[1] + " " + data[2];
            } else {
                StringBuilder cityBuilder = new StringBuilder(city);
                for (String aData : data) {
                    cityBuilder.append(aData).append(" ");
                }
                city = cityBuilder.toString();
            }
        } catch (Exception ignored) {

        }
        try {
            String[] locatons = city.split(" ");
            if (locatons.length > 2) {
                city = locatons[locatons.length - 2] + " " + locatons[locatons.length - 1];
            }
        } catch (Exception ignored) {
        }
        mTvLocation.setText(city);//更新位置
    }

    @OnClick({R.id.rl_change_head_container, R.id.ll_type_container, R.id.ll_tag_container, R.id.ll_location_container, R.id.ll_synchro, R.id.ll_block})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_change_head_container:
                initPhotoPopupWindow();
                mPhotoPopupWindow.show();
                break;
            case R.id.ll_type_container:
                Intent typeIntent = new Intent(getActivity(), CircleTyepsActivity.class);
                startActivityForResult(typeIntent, REQUST_CODE_CATEGORY);
                break;
            case R.id.ll_tag_container:
                EditUserTagFragment.startToEditTagActivity(getActivity(), TagFrom.CREATE_CIRCLE, null);
                break;
            case R.id.ll_location_container:
                Intent intent = new Intent(getActivity(), LocationRecommentActivity.class);
                startActivityForResult(intent, REQUST_CODE_AREA);
                break;
            case R.id.ll_synchro:
                break;
            case R.id.ll_block:
                break;
            default:
        }
    }
}
