package com.zhiyicx.thinksnsplus.modules.circle.create;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCheckedTextView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.modules.circle.create.location.CircleLocationActivity;
import com.zhiyicx.thinksnsplus.modules.circle.create.location.CircleLocationFragment;
import com.zhiyicx.thinksnsplus.modules.circle.create.types.CircleTyepsActivity;
import com.zhiyicx.thinksnsplus.modules.circle.create.types.CircleTypesFragment;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoTagsAdapter;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func4;

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

    public static final int REQUST_CODE_UPDATE = 1996;

    public static final String CIRCLEINFO = "circleinfo";
    public static final String CANUPDATE = "canupdate";
    public static final String PERMISSION_OWNER = "permission_owner";
    public static final String PERMISSION_MANAGER = "permission_manager";

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
    @BindView(R.id.tv_user_agreement)
    TextView mTvUseAgreeMent;
    @BindView(R.id.ll_type_container)
    LinearLayout mLlTypeContainer;
    @BindView(R.id.fl_tags)
    TagFlowLayout mFlTags;
    @BindView(R.id.tv_tag_hint)
    TextView mTvTagHint;
    @BindView(R.id.ll_tag_container)
    LinearLayout mLlTagContainer;
    @BindView(R.id.et_circle_amount)
    EditText mEtCircleAmount;
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
    @BindView(R.id.ll_free)
    LinearLayout mLlFree;
    @BindView(R.id.cb_free)
    CheckBox mCbFree;
    @BindView(R.id.tv_notice)
    UserInfoInroduceInputView mTvNotice;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;

    // 图片选择弹框
    private ActionPopupWindow mPhotoPopupWindow;
    private PhotoSelectorImpl mPhotoSelector;

    private String mCurrentShowLocation;

    private UserInfoTagsAdapter mUserInfoTagsAdapter;
    private List<UserTagBean> mUserTagBeens = new ArrayList<>();
    private CircleTypeBean mCircleTypeBean;

    private CreateCircleBean mCreateCircleBean;
    private String mHeadImage = "";
    private PoiItem mPoiItem;

    private CircleInfo mCircleInfo;
    private boolean canUpdate;
    boolean isOwner;
    boolean isManager;

    /**
     * 圈子必须输入的字段数监听
     */
    private int emptyFlag;

    /**
     * 有没有头像
     */
    private boolean hasHeadImage;

    public static CreateCircleFragment newInstance(Bundle bundle) {
        CreateCircleFragment createCircleFragment = new CreateCircleFragment();
        createCircleFragment.setArguments(bundle);
        return createCircleFragment;
    }

    @Override
    protected String setRightTitle() {
        return getString(mCircleInfo == null ? R.string.create : R.string.save);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(mCircleInfo == null ? R.string.create_circle : R.string.edit_circle);
    }

    @Override
    protected void initView(View rootView) {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .SHAPE_SQUARE))
                .build().photoSelectorImpl();
        initListener();
        // 适配手机无法显示输入焦点
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AndroidBug5497Workaround.assistActivity(getActivity());
        }
        if (getArguments() != null) {
            canUpdate = getArguments().getBoolean(CANUPDATE, false);
            isOwner = getArguments().getBoolean(PERMISSION_OWNER, false);
            isManager = getArguments().getBoolean(PERMISSION_MANAGER, false);
            mCircleInfo = getArguments().getParcelable(CIRCLEINFO);
            mTvUseAgreeMent.setVisibility(canUpdate ? View.VISIBLE : View.GONE);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        mTvUseAgreeMent.setText(String.format(Locale.getDefault(), getString(R.string.edit_circle_rule), getString(R.string.app_name)));
        mEtCircleName.setFilters(new InputFilter[]{RegexUtils.getEmojiFilter()});
        mFlTags.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (canUpdate && !isManager) {
                    jumpToEditUserTag();
                }
            }
            return true;
        });
        if (mCircleInfo == null) {
            return;
        }
        restoreData();
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        packageDataAndHandle();
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    public void setCircleInfo(CircleInfo data) {
        if (mCircleInfo == null) {
            mActivity.finish();
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CIRCLEINFO, data);
        intent.putExtras(bundle);
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUST_CODE_AREA && data != null && data.getExtras() != null) {
            mPoiItem = data.getExtras().getParcelable(CircleLocationFragment.BUNDLE_DATA);
            if (mPoiItem != null) {
                mTvLocation.setText(mPoiItem.getTitle());
                return;
            }
            mTvLocation.setText(R.string.create_circle_nolocation);
        } else if (requestCode == REQUST_CODE_CATEGORY && data != null && data.getExtras() != null) {
            mCircleTypeBean = data.getExtras().getParcelable(CircleTypesFragment.BUNDLE_CIRCLE_CATEGORY);
            mTvCircleType.setText(mCircleTypeBean.getName());
        } else if (requestCode == TagFrom.CREATE_CIRCLE.id) {
            ArrayList<UserTagBean> choosedTags = data.getExtras().getParcelableArrayList(EditUserTagFragment.BUNDLE_CHOOSED_TAGS);
            mUserTagBeens.clear();
            mUserTagBeens.addAll(choosedTags);
            mUserInfoTagsAdapter.notifyDataChanged();
            createCirclepreHandle(emptyFlag != 0);
            mTvTagHint.setVisibility(choosedTags.isEmpty() ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        Glide.with(getActivity())
                .load(photoList.get(0).getImgUrl())
                .into(mIvHeadIcon);
        mHeadImage = photoList.get(0).getImgUrl();
        hasHeadImage = true;
        createCirclepreHandle(emptyFlag != 0);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_create_circle;
    }

    private void initListener() {

        Observable.combineLatest(
                RxTextView.textChanges(mEtCircleName),
                RxTextView.textChanges(mTvCircleType),
                RxTextView.textChanges(mEtCircleIntroduce.getEtContent()), (charSequence, charSequence2, charSequence3) -> {
                    emptyFlag = charSequence.length() * charSequence2.length() * charSequence3.length();
                    return emptyFlag != 0;
                }).subscribe(this::createCirclepreHandle);

        RxTextView.textChanges(mEtCircleAmount)
                .filter(charSequence -> mCbToll.isChecked())
                .subscribe(charSequence -> createCirclepreHandle(emptyFlag != 0 && !charSequence.toString().isEmpty()));


        mCbToll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mCbFree.setChecked(false);
                createCirclepreHandle(emptyFlag != 0 && mEtCircleAmount.getText().toString().length() > 0);
            }
        });

        mCbFree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mCbToll.setChecked(false);
                createCirclepreHandle(emptyFlag != 0);
            }
        });

        mWcBlock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                createCirclepreHandle(emptyFlag != 0);
                mLlCharge.setVisibility(View.GONE);
                mLlFree.setVisibility(View.GONE);
            } else {
                createCirclepreHandle(emptyFlag != 0 && (mCbFree.isChecked() || mCbToll.isChecked()));
                mLlFree.setVisibility(View.VISIBLE);
                mLlCharge.setVisibility(View.VISIBLE);
            }
        });


        mUserInfoTagsAdapter = new UserInfoTagsAdapter(mUserTagBeens, getContext());
        mFlTags.setAdapter(mUserInfoTagsAdapter);
    }

    private void createCirclepreHandle(Boolean aBoolean) {
        mToolbarRight.setEnabled(hasHeadImage && !mUserTagBeens.isEmpty() && aBoolean);
    }

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

    private void packageDataAndHandle() {
        mCreateCircleBean = new CreateCircleBean();
        mCreateCircleBean.setName(mEtCircleName.getText().toString());
        if (mPoiItem != null) {
            mCreateCircleBean.setLatitude(mPoiItem.getLatLonPoint().getLatitude() + "");
            mCreateCircleBean.setLongitude(mPoiItem.getLatLonPoint().getLongitude() + "");
            mCreateCircleBean.setGeo_hash(mPoiItem.getAdCode());
            mCreateCircleBean.setLocation(mTvLocation.getText().toString());
        }
        mCreateCircleBean.setAllow_feed(mWcSynchro.isChecked() ? 1 : 0);
        mCreateCircleBean.setMode(mCbToll.isChecked() ? CircleInfo.CirclePayMode.PAID.value : (mWcBlock.isChecked() ?
                CircleInfo.CirclePayMode.PRIVATE.value : CircleInfo.CirclePayMode.PUBLIC.value));
        mCreateCircleBean.setNotice(mTvNotice.getInputContent());
        mCreateCircleBean.setMoney(!mCbToll.isChecked() || mEtCircleAmount.getText().toString().isEmpty() ? "0" : mEtCircleAmount.getText().toString());
        mCreateCircleBean.setSummary(mEtCircleIntroduce.getInputContent());
        List<CreateCircleBean.TagId> tags = new ArrayList<>();
        for (UserTagBean tagBean : mUserTagBeens) {
            CreateCircleBean.TagId tagId = new CreateCircleBean.TagId(tagBean.getId());
            tags.add(tagId);
        }
        mCreateCircleBean.setTags(tags);
        mCreateCircleBean.setCategoryId(mCircleTypeBean.getId());
        mCreateCircleBean.setFilePath(mHeadImage);
        if (mCircleInfo == null) {
            mPresenter.createCircle(mCreateCircleBean);
        } else {
            mCreateCircleBean.setCircleId(mCircleInfo.getId());
            mCircleInfo.setTags(mUserTagBeens);
            mCircleInfo.setCategory(mCircleTypeBean);
            mPresenter.updateCircle(mCreateCircleBean);
        }
    }

    private void jumpToEditUserTag() {
        EditUserTagFragment.startToEditTagActivity(getActivity(), TagFrom.CREATE_CIRCLE, (ArrayList<UserTagBean>) mUserTagBeens);
    }

    private void restoreData() {
        hasHeadImage = true;
        mToolbarRight.setText(setRightTitle());
        mToolbarCenter.setText(setCenterTitle());
        Glide.with(getActivity())
                .load(mCircleInfo.getAvatar())
                .into(mIvHeadIcon);
        mHeadImage = null;
        mPoiItem = new PoiItem("", new LatLonPoint(Double.parseDouble(mCircleInfo.getLatitude()),
                Double.parseDouble(mCircleInfo.getLongitude())), "", "");
        mPoiItem.setAdCode(mCircleInfo.getGeo_hash());
        mCircleTypeBean = mCircleInfo.getCategory();

        mUserTagBeens.addAll(mCircleInfo.getTags());
        mUserInfoTagsAdapter.notifyDataChanged();
        mTvTagHint.setVisibility(mUserTagBeens.isEmpty() ? View.VISIBLE : View.GONE);

        mEtCircleName.setText(mCircleInfo.getName());
        mTvCircleType.setText(mCircleInfo.getCategory().getName());
        mTvLocation.setText(mCircleInfo.getLocation());
        mTvNotice.setText(mCircleInfo.getNotice());
        mEtCircleIntroduce.setText(mCircleInfo.getSummary());
        mWcSynchro.setChecked(mCircleInfo.getAllow_feed() == 1);

        boolean isPaidCircle = CircleInfo.CirclePayMode.PAID.value.equals(mCircleInfo.getMode());

        mWcBlock.setChecked(CircleInfo.CirclePayMode.PRIVATE.value.equals(mCircleInfo.getMode()) || isPaidCircle);
        mCbToll.setChecked(isPaidCircle);
        mCbFree.setChecked(CircleInfo.CirclePayMode.PUBLIC.value.equals(mCircleInfo.getMode()));
        mEtCircleAmount.setText(mCircleInfo.getMoney() > 0 ? mCircleInfo.getMoney() + "" : "");

        mToolbarRight.setVisibility(!canUpdate ? View.GONE : View.VISIBLE);
        mRlChangeHeadContainer.setEnabled(canUpdate && !isManager);
        mEtCircleName.setEnabled(canUpdate && !isManager);
        mLlTypeContainer.setEnabled(canUpdate && !isManager);
        mLlTagContainer.setEnabled(canUpdate && !isManager);
        mLlLocationContainer.setEnabled(canUpdate && !isManager);
        mWcSynchro.setEnabled(canUpdate && !isManager);
        mWcBlock.setEnabled(canUpdate && !isManager && !isPaidCircle);
        mCbFree.setEnabled(canUpdate && !isManager && !isPaidCircle);
        mCbToll.setEnabled(canUpdate && !isManager && !isPaidCircle);
        mEtCircleAmount.setEnabled(canUpdate && !isManager && !isPaidCircle);

        mTvNotice.setEnabled(canUpdate);
        mEtCircleIntroduce.setEnabled(canUpdate);

    }

    @OnClick({R.id.rl_change_head_container, R.id.ll_type_container, R.id.ll_tag_container, R.id.ll_location_container,
            R.id.ll_synchro, R.id.ll_block})
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
                jumpToEditUserTag();
                break;
            case R.id.ll_location_container:
                Intent intent = new Intent(getActivity(), CircleLocationActivity.class);
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
