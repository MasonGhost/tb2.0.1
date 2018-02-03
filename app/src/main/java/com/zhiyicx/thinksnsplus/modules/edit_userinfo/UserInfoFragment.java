package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import com.wcy.overscroll.OverScrollLayout;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.LocationRecommentActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search.LocationSearchFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;


/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
public class UserInfoFragment extends TSFragment<UserInfoContract.Presenter> implements
        UserInfoContract.View, PhotoSelectorImpl.IPhotoBackListener {

    private static final int REQUST_CODE_AREA = 8100;
    /**
     * 定义这些常数，用来封装被修改的用户信息
     * 通过hashmap进行封装，而不是使用Usrinfobean，主要是以后可能配置用户信息，方便以后拓展
     */
    public static final String USER_NAME = "name";
    public static final String USER_INTRO = "bio";
    public static final String USER_SEX = "sex";
    public static final String USER_LOCATION = "location";

    public static final String USER_STORAGE_TASK_ID = "storage_task_id";
    public static final String USER_LOCAL_IMG_PATH = "localImgPath";

    @BindView(R.id.iv_head_icon)
    UserAvatarView mIvHeadIcon;
    @BindView(R.id.et_user_name)
    EditText mEtUserName;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.tv_city)
    TextView mTvCity;
    @BindView(R.id.ll_city_container)
    LinearLayout mLlCityContainer;
    @BindView(R.id.et_user_introduce)
    UserInfoInroduceInputView mEtUserIntroduce;
    @BindView(R.id.overscroll)
    OverScrollLayout mDvViewGroup;
    @BindView(R.id.fl_tags)
    TagFlowLayout mFlTags;
    @BindView(R.id.tv_tag_hint)
    TextView mTvTagHint;

    private TSnackbar mTSnackbarUserInfo;
    private TSnackbar mTSnackbarUploadIcon;

    private ActionPopupWindow mGenderPopupWindow;// 性别选择弹框
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private PhotoSelectorImpl mPhotoSelector;

    private UserInfoBean mUserInfoBean;// 用户未修改前的用户信息
    private boolean userNameChanged, sexChanged, cityChanged, introduceChanged;

    private UserInfoTagsAdapter mUserInfoTagsAdapter;
    private List<UserTagBean> mUserTagBeens = new ArrayList<>();

    private String mCurrentShowLocation;


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_user_info;
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
    protected void initView(View rootView) {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .SHAPE_SQUARE))
                .build().photoSelectorImpl();

//        initCityPickerView();
        // 这个是和其他反的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AndroidBug5497Workaround.assistActivity(getActivity());
        }

        RxView.globalLayouts(mDvViewGroup)
                .flatMap(aVoid -> {
                    if (mDvViewGroup == null) {
                        return Observable.just(false);
                    }
                    Rect rect = new Rect();
                    //获取root在窗体的可视区域
                    mDvViewGroup.getWindowVisibleDisplayFrame(rect);
                    //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                    int rootInvisibleHeight = mDvViewGroup.getRootView().getHeight() - rect.bottom;
                    int dispayHeight = UIUtils.getWindowHeight(getContext());
///                        LogUtils.i("rootInvisibleHeight-->" + rootInvisibleHeight + "  dispayHeight-->" + dispayHeight);
                    return Observable.just(rootInvisibleHeight > (dispayHeight * (1f / 3)));
                })
                // 监听键盘弹起隐藏状态时，会多次调用globalLayouts方法，为了避免多个数据导致状态判断出错，只取200ms内最后一次数据
                .debounce(getResources().getInteger(android.R.integer.config_mediumAnimTime), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (mEtUserIntroduce == null) {
                        return;
                    }
                    //若不可视区域高度大于1/3屏幕高度，则键盘显示
                    LogUtils.i(TAG + "---RxView   " + aBoolean);
                    if (aBoolean) {
                    } else {
                        //键盘隐藏,清除焦点
                        if (mEtUserIntroduce != null && mEtUserIntroduce.getEtContent().hasFocus()) {
                            mEtUserIntroduce.getEtContent().clearFocus();
                        }
                        if (mEtUserName != null && mEtUserName.hasFocus()) {
                            mEtUserName.clearFocus();
                        }
                    }
                });
        mUserInfoTagsAdapter = new UserInfoTagsAdapter(mUserTagBeens, getContext());
        mFlTags.setAdapter(mUserInfoTagsAdapter);
        mFlTags.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                jumpToEditUserTag();
            }
            return true;
        });
    }


    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void initData() {
        mPresenter.initUserInfo();
        ////////////////////////监听所有的用户信息变化///////////////////////////////
        RxTextView.textChanges(mEtUserName)
                .subscribe(charSequence -> {
                    String oldUserName = mUserInfoBean.getName();
                    if (TextUtils.isEmpty(oldUserName)) {
                        userNameChanged = !TextUtils.isEmpty(charSequence);
                    } else {
                        userNameChanged = !oldUserName.equals(charSequence.toString());
                    }
                    canChangerUserInfo();
                });
        RxTextView.textChanges(mTvSex)
                .subscribe(charSequence -> {
                    String oldSex = mUserInfoBean.getSexString();
                    if (TextUtils.isEmpty(oldSex)) {
                        sexChanged = !TextUtils.isEmpty(charSequence);
                    } else {
                        sexChanged = !oldSex.equals(charSequence.toString());
                    }
                    canChangerUserInfo();
                });
        RxTextView.textChanges(mTvCity)
                .subscribe(charSequence -> {
                    String oldLocation = mUserInfoBean.getLocation();
                    if (TextUtils.isEmpty(oldLocation)) {
                        cityChanged = !TextUtils.isEmpty(charSequence);
                    } else {
                        cityChanged = !oldLocation.equals(mCurrentShowLocation);
                    }
                    canChangerUserInfo();
                });
        RxTextView.textChanges(mEtUserIntroduce.getEtContent())
                .subscribe(charSequence -> {
                    String oldIntroduce = getIntro(mUserInfoBean);
                    if (TextUtils.isEmpty(oldIntroduce)) {
                        introduceChanged = !TextUtils.isEmpty(charSequence);
                    } else {
                        introduceChanged = !oldIntroduce.equals(charSequence.toString());
                    }
                    canChangerUserInfo();
                });
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.user_info);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.complete);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @OnClick({R.id.rl_change_head_container, R.id.ll_sex_container, R.id.ll_city_container, R.id.ll_tag_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_change_head_container:
                initPhotoPopupWindow();
                mPhotoPopupWindow.show();
                break;
            case R.id.ll_sex_container:
                // 尝试隐藏键盘
                DeviceUtils.hideSoftKeyboard(getContext(), mLlCityContainer);
                initGenderPopupWindow();
                mGenderPopupWindow.show();
                break;
            case R.id.ll_city_container:
                // 尝试隐藏键盘
                DeviceUtils.hideSoftKeyboard(getContext(), mLlCityContainer);

                Intent intent = new Intent(getActivity(), LocationRecommentActivity.class);
///               Bundle bundle = new Bundle();
//                bundle.putString(LocationSearchFragment.BUNDLE_LOCATION_STRING, mTvCity.getText().toString().trim());
//                intent.putExtras(bundle);
                startActivityForResult(intent, REQUST_CODE_AREA);

                break;
            case R.id.ll_tag_container:
                jumpToEditUserTag();
                break;
            default:
        }
    }

    /**
     * 跳转标签管理页面
     */
    private void jumpToEditUserTag() {
        EditUserTagFragment.startToEditTagActivity(getActivity(), TagFrom.USER_EDIT, null);
    }

    @Override
    protected void setRightClick() {
        if (TextUtils.isEmpty(mEtUserIntroduce.getInputContent())) {
            showSnackErrorMessage(getString(R.string.please_input_intro));
            return;
        }
        // 点击完成，修改用户信息
        mPresenter.changUserInfo(packageUserInfo(), false);
    }

    /**
     * @param options1Items
     * @param options2Items
     * @param options3Items
     */
    @Override
    public void setAreaData(ArrayList<AreaBean> options1Items, ArrayList<ArrayList<AreaBean>>
            options2Items, ArrayList<ArrayList<ArrayList<AreaBean>>> options3Items) {
    }

    /**
     * @param upLoadState -1 失败 0进行中 1 图片上传成功 2图片用户信息修改成功
     * @param message
     */
    @Override
    public void setUpLoadHeadIconState(int upLoadState, String message) {
        // 上传成功，可以进行修改
        switch (upLoadState) {
            case -1:
                TSnackbar.getTSnackBar(mTSnackbarUploadIcon, mSnackRootView,
                        TextUtils.isEmpty(message) ? getString(R.string.update_head_failure) : message, TSnackbar.LENGTH_SHORT)
                        .setPromptThemBackground(Prompt.ERROR)
                        .show();
                break;
            case 0:
                mTSnackbarUploadIcon = TSnackbar.make(mSnackRootView, TextUtils.isEmpty(message) ? getString(R.string.update_head_ing) : message,
                        TSnackbar
                                .LENGTH_INDEFINITE)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .addIconProgressLoading(0, true, false);
                mTSnackbarUploadIcon.show();
                break;
            case 1:
            case 2:
                TSnackbar.getTSnackBar(mTSnackbarUploadIcon, mSnackRootView,
                        TextUtils.isEmpty(message) ? getString(R.string.update_head_success) : message, TSnackbar.LENGTH_SHORT)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .show();
                break;
            default:
        }
    }

    /**
     * @param changeUserInfoState -1 失败 1进行中 2 成功
     * @param message
     */
    @Override
    public void setChangeUserInfoState(int changeUserInfoState, String message) {
        switch (changeUserInfoState) {
            case -1:
                message = TextUtils.isEmpty(message) ? getString(R.string.edit_userinfo_failure) : message;
                TSnackbar.getTSnackBar(mTSnackbarUserInfo, mSnackRootView,
                        message, TSnackbar.LENGTH_SHORT)
                        .setPromptThemBackground(Prompt.ERROR)
                        .show();
                break;
            case 0:
                mTSnackbarUserInfo = TSnackbar.make(mSnackRootView, TextUtils.isEmpty(message) ? getString(R.string.edit_userinfo_ing) : message,
                        TSnackbar
                                .LENGTH_INDEFINITE)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .addIconProgressLoading(0, true, false);
                mTSnackbarUserInfo.show();
                break;
            case 1:
                TSnackbar.getTSnackBar(mTSnackbarUserInfo, mSnackRootView,
                        TextUtils.isEmpty(message) ? getString(R.string.edit_userinfo_success) : message, TSnackbar.LENGTH_SHORT)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .show();
                // 为了让用户看到提示成功的消息，添加定时器：1.5s后关闭页面
                Observable.timer(1500, TimeUnit.MILLISECONDS)
                        .compose(this.bindToLifecycle())
                        .subscribe(aLong -> {
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        });
                break;
            default:
        }
    }

    /**
     * @param mUserInfoBean
     */
    @Override
    public void initUserInfo(UserInfoBean mUserInfoBean) {
        this.mUserInfoBean = mUserInfoBean;
        // 初始化界面数据
        // 设置用户名
        mEtUserName.setText(mUserInfoBean.getName());
        // 设置性别
        mTvSex.setText(mUserInfoBean.getSexString());
        mTvSex.setTag(R.id.view_data, mUserInfoBean.getSex());// 设置性别编号


        // 设置地址
        setCity(mUserInfoBean.getLocation());
        // 设置简介
        String intro = getIntro(mUserInfoBean);
        mEtUserIntroduce.setText(intro);// 设置简介

        ImageUtils.loadCircleUserHeadPic(mUserInfoBean, mIvHeadIcon);
    }

    /**
     * @param datas tags
     */
    @Override
    public void updateTags(List<UserTagBean> datas) {
        if (datas == null) {
            return;
        }
        mUserTagBeens.clear();
        mUserTagBeens.addAll(datas);
        mTvTagHint.setVisibility(datas.size() == 0 ? View.VISIBLE : View.GONE);
        mUserInfoTagsAdapter.notifyDataChanged();
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

        }

    }

    /**
     * @param photoList
     */
    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (photoList.isEmpty()) {
            return;
        }
        String path = photoList.get(0).getImgUrl();
        // 开始上传
        mPresenter.changeUserHeadIcon(path);
        // 加载本地图片
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(path)
                .imagerView(mIvHeadIcon.getIvAvatar())
                .placeholder(R.drawable.shape_default_image_circle)
                .errorPic(R.drawable.shape_default_image_circle)
                .transformation(new GlideCircleTransform(getContext()))
                .build());
    }

    /**
     * @param errorMsg
     */
    @Override
    public void getPhotoFailure(String errorMsg) {
        ToastUtils.showToast(errorMsg);
    }


    /**
     * 初始化性别选择弹框
     */
    private void initGenderPopupWindow() {
        if (mGenderPopupWindow != null) {
            return;
        }
        mGenderPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.male))
                .item2Str(getString(R.string.female))
                .item3Str(getString(R.string.keep_secret))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item1ClickListener(() -> {
                    setGender(UserInfoBean.MALE);
                    mGenderPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    setGender(UserInfoBean.FEMALE);
                    mGenderPopupWindow.hide();
                })
                .item3ClickListener(() -> {
                    setGender(UserInfoBean.SECRET);
                    mGenderPopupWindow.hide();
                })
                .bottomClickListener(() -> mGenderPopupWindow.hide())
                .build();
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
                for (int i = 0; i < data.length; i++) {
                    city += data[i] + " ";
                }
            }
        } catch (Exception e) {

        }
        try {
            String[] locatons = city.split(" ");
            if (locatons.length > 2) {
                city = locatons[locatons.length - 2] + " " + locatons[locatons.length - 1];
            }
        } catch (Exception ignored) {
        }
        mTvCity.setText(city);//更新位置
    }

    /**
     * 设置用户性别
     */
    private void setGender(int genderType) {
        switch (genderType) {
            case UserInfoBean.MALE:
                mTvSex.setText(R.string.male);
                break;
            case UserInfoBean.FEMALE:
                mTvSex.setText(R.string.female);
                break;
            case UserInfoBean.SECRET:
                mTvSex.setText(R.string.keep_secret);
                break;
            default: // 没有该性别
        }
        mTvSex.setTag(R.id.view_data, genderType);// 设置性别编号
    }

    /**
     * 封装编辑用户信息的提交信息
     */
    private HashMap<String, Object> packageUserInfo() {
        HashMap<String, Object> fieldMap = new HashMap<>();
        // 只上传改变的信息
        if (userNameChanged) {
            fieldMap.put(USER_NAME, mEtUserName.getText().toString());
        }
        if (sexChanged) {
            fieldMap.put(USER_SEX, mTvSex.getTag(R.id.view_data));
        }
        if (cityChanged) {
            fieldMap.put(USER_LOCATION, mCurrentShowLocation);
        }
        if (introduceChanged) {
            fieldMap.put(USER_INTRO, mEtUserIntroduce.getInputContent());
        }

        return fieldMap;
    }

    /**
     * 判断是否需要修改信息：如果用户名，性别。。。其中任意一项发生变化，都可以提交修改
     */
    private void canChangerUserInfo() {
        if (userNameChanged || sexChanged || cityChanged || introduceChanged) {
            mToolbarRight.setEnabled(true);
        } else {
            mToolbarRight.setEnabled(false);
        }
    }

    /**
     * 处理用户简介缺省文字，如果是缺省文字，就应该默认为“”
     */
    private String getIntro(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return getString(R.string.intro_default);
        }
        String intro = userInfoBean.getIntro();
        // 是缺省的内容,就设置为kong，但要注意这儿有个隐藏的bug，如果简介设置为缺省的内容，那就。。。
        if (getString(R.string.intro_default).equals(intro)) {
            intro = "";
        }
        return intro;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getCurrentUserTags();

    }


}
