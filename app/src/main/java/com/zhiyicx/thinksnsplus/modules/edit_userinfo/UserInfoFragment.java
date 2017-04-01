package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.dialog.LoadingDialog;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
public class UserInfoFragment extends TSFragment<UserInfoContract.Presenter> implements
        UserInfoContract.View, PhotoSelectorImpl.IPhotoBackListener {

    private static final int LOCATION_2LEVEL = 2;// 地区选择可选的级数为2，2级联动
    private static final int LOCATION_3LEVEL = 3;// 地区选择可选的级数为3

    /**
     * 定义这些常数，用来封装被修改的用户信息
     * 通过hashmap进行封装，而不是使用Usrinfobean，主要是以后可能配置用户信息，方便以后拓展
     */
    public static final String USER_NAME = "name";
    public static final String USER_INTRO = "intro";
    public static final String USER_SEX = "sex";
    public static final String USER_LOCATION = "location";
    public static final String USER_PROVINCE = "province";
    public static final String USER_CITY = "city";
    public static final String USER_AREA = "area";
    public static final String USER_STORAGE_TASK_ID = "storage_task_id";
    public static final String USER_LOCAL_IMG_PATH = "localImgPath";

    @BindView(R.id.iv_head_icon)
    ImageView mIvHeadIcon;
    @BindView(R.id.rl_change_head_container)
    RelativeLayout mRlChangeHeadContainer;
    @BindView(R.id.et_user_name)
    EditText mEtUserName;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.ll_sex_container)
    LinearLayout mLlSexContainer;
    @BindView(R.id.tv_city)
    TextView mTvCity;
    @BindView(R.id.ll_city_container)
    LinearLayout mLlCityContainer;
    @BindView(R.id.et_user_introduce)
    UserInfoInroduceInputView mEtUserIntroduce;
    @BindView(R.id.tv_edit_introduce)
    TextView mTvEditIntroduce;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;

    private TSnackbar mTSnackbarUserInfo;
    private TSnackbar mTSnackbarUploadIcon;


    private ArrayList<AreaBean> options1Items;
    private ArrayList<ArrayList<AreaBean>> options2Items;
    private ArrayList<ArrayList<ArrayList<AreaBean>>> options3Items;
    private int mCityOption1;//用来记录地区中滚轮的位置
    private int mCityOption2;
    private int mCityOption3;
    private OptionsPickerView mAreaPickerView;// 地域选择器
    private ActionPopupWindow mGenderPopupWindow;// 性别选择弹框
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private PhotoSelectorImpl mPhotoSelector;

    private UserInfoBean mUserInfoBean;// 用户未修改前的用户信息
    private int upLoadCount = 0;// 当前文件上传的次数，>0表示已经上传成功，但是还没有提交修改用户信息
    private boolean userNameChanged, sexChanged, cityChanged, introduceChanged;
    private boolean isFirstOpenCityPicker = true;// 是否是第一次打开城市选择器：默认是第一次打开
    private int upDateHeadIconStorageId = 0;// 上传成功返回的图片id
    private String path;// 上传成功的图片本地路径

    private int locationLevel = LOCATION_2LEVEL;

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

        initCityPickerView();
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
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        LogUtils.i("userName-->" + charSequence);
                        String oldUserName = mUserInfoBean.getName();
                        if (TextUtils.isEmpty(oldUserName)) {
                            userNameChanged = !TextUtils.isEmpty(charSequence);
                        } else {
                            userNameChanged = !oldUserName.equals(charSequence.toString());
                        }
                        canChangerUserInfo();
                    }
                });
        RxTextView.textChanges(mTvSex)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        String oldSex = mUserInfoBean.getSexString();
                        if (TextUtils.isEmpty(oldSex)) {
                            sexChanged = !TextUtils.isEmpty(charSequence);
                        } else {
                            sexChanged = !oldSex.equals(charSequence.toString());
                        }
                        canChangerUserInfo();
                    }
                });
        RxTextView.textChanges(mTvCity)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        String oldLocation = mUserInfoBean.getLocation();
                        if (TextUtils.isEmpty(oldLocation)) {
                            cityChanged = !TextUtils.isEmpty(charSequence);
                        } else {
                            cityChanged = !oldLocation.equals(charSequence.toString());
                        }
                        canChangerUserInfo();
                    }
                });
        RxTextView.textChanges(mEtUserIntroduce.getEtContent())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        String oldIntroduce = getIntro(mUserInfoBean);
                        if (TextUtils.isEmpty(oldIntroduce)) {
                            introduceChanged = !TextUtils.isEmpty(charSequence);
                        } else {
                            introduceChanged = !oldIntroduce.equals(charSequence.toString());
                        }
                        canChangerUserInfo();
                    }
                });
        RxView.focusChanges(mEtUserIntroduce.getEtContent())
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        // 没有焦点，并且简介为空的时候隐藏编辑框，显示编辑简介
                        if (!aBoolean && TextUtils.isEmpty(mEtUserIntroduce.getInputContent())) {
                            mTvEditIntroduce.setVisibility(View.VISIBLE);// 显示编辑简介
                            mEtUserIntroduce.setVisibility(View.GONE);// 隐藏编辑框
                        } else {
                            mTvEditIntroduce.setVisibility(View.GONE);// 隐藏编辑简介
                            mEtUserIntroduce.setVisibility(View.VISIBLE);// 显示编辑框
                        }
                    }
                });
    }

    @Override
    public void setPresenter(UserInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

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

    @OnClick({R.id.rl_change_head_container, R.id.ll_sex_container, R.id.ll_city_container, R.id.tv_edit_introduce})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_change_head_container:
                initPhotoPopupWindow();
                mPhotoPopupWindow.show();
                break;
            case R.id.ll_sex_container:
                initGenderPopupWindow();
                mGenderPopupWindow.show();
                break;
            case R.id.ll_city_container:
                initCityPickerFirstTime();
                mAreaPickerView.setSelectOptions(mCityOption1, mCityOption2, mCityOption3);
                // 尝试隐藏键盘
                DeviceUtils.hideSoftKeyboard(getContext(), mLlCityContainer);
                mAreaPickerView.show();
                break;
            case R.id.tv_edit_introduce:
                mEtUserIntroduce.getEtContent().requestFocus();
                break;
            default:
        }
    }

    @Override
    protected void setRightClick() {
        // 点击完成，修改用户信息
        mPresenter.changUserInfo(packageUserInfo());
    }

    @Override
    public void setAreaData(ArrayList<AreaBean> options1Items, ArrayList<ArrayList<AreaBean>>
            options2Items, ArrayList<ArrayList<ArrayList<AreaBean>>> options3Items) {
        this.options1Items = options1Items;
        this.options2Items = options2Items;
        this.options3Items = options3Items;
        if (locationLevel == LOCATION_2LEVEL) {
            mAreaPickerView.setPicker(options1Items, options2Items, true);
        } else if (locationLevel == LOCATION_3LEVEL) {
            mAreaPickerView.setPicker(options1Items, options2Items, options3Items, true);
        }

        mAreaPickerView.setCyclic(false);// 设置是否可以循环滚动
    }

    @Override
    public void setUpLoadHeadIconState(int upLoadState, int taskId) {
        // 上传成功，可以进行修改
        switch (upLoadState) {
            case -1:
                TSnackbar.getTSnackBar(mTSnackbarUploadIcon, mSnackRootView,
                        getString(R.string.update_head_failure), TSnackbar.LENGTH_SHORT)
                        .setPromptThemBackground(Prompt.ERROR)
                        .show();
                break;
            case 0:
                mTSnackbarUploadIcon = TSnackbar.make(mSnackRootView, R.string.update_head_ing, TSnackbar.LENGTH_INDEFINITE)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .addIconProgressLoading(0, true, false);
                mTSnackbarUploadIcon.show();
                break;
            case 1:
                upLoadCount++;
                upDateHeadIconStorageId = taskId;
                TSnackbar.getTSnackBar(mTSnackbarUploadIcon, mSnackRootView,
                        getString(R.string.update_head_success), TSnackbar.LENGTH_SHORT)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .show();
                break;
            default:
        }
        canChangerUserInfo();
    }

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
                mTSnackbarUserInfo = TSnackbar.make(mSnackRootView, R.string.edit_userinfo_ing, TSnackbar.LENGTH_INDEFINITE)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .addIconProgressLoading(0, true, false);
                mTSnackbarUserInfo.show();
                break;
            case 1:
                TSnackbar.getTSnackBar(mTSnackbarUserInfo, mSnackRootView,
                        getString(R.string.edit_userinfo_success), TSnackbar.LENGTH_SHORT)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .show();
                getActivity().finish();
                break;
            default:
        }
    }

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
        mTvCity.setText(mUserInfoBean.getLocation());
        // 设置简介
        String intro = getIntro(mUserInfoBean);
        // 如果没有简介
        if (TextUtils.isEmpty(intro)) {
            mTvEditIntroduce.setVisibility(View.VISIBLE);// 显示编辑简介
            mEtUserIntroduce.setVisibility(View.GONE);// 隐藏编辑框
        } else {
            mTvEditIntroduce.setVisibility(View.GONE);// 隐藏编辑简介
            mEtUserIntroduce.setVisibility(View.VISIBLE);// 显示编辑框
            mEtUserIntroduce.setText(intro);// 设置简介
        }

        // 设置头像
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(ImageUtils.imagePathConvert(mUserInfoBean.getAvatar(), ImageZipConfig.IMAGE_38_ZIP))
                .errorPic(R.drawable.shape_default_image_circle)
                .placeholder(R.drawable.shape_default_image_circle)
                .imagerView(mIvHeadIcon)
                .transformation(new GlideCircleTransform(getContext()))
                .build());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 处理pickerView和返回键的逻辑
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mAreaPickerView.isShowing()) {
                mAreaPickerView.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if(photoList.isEmpty()){
            return;
        }
        path = photoList.get(0).getImgUrl();
        // 开始上传
        mPresenter.changeUserHeadIcon(path);
        // 加载本地图片
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(path)
                .imagerView(mIvHeadIcon)
                .placeholder(R.drawable.shape_default_image_circle)
                .errorPic(R.drawable.shape_default_image_circle)
                .transformation(new GlideCircleTransform(getContext()))
                .build());
    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        ToastUtils.showToast(errorMsg);
    }

    /**
     * 初始化城市选择器
     */
    private void initCityPickerView() {
        if (mAreaPickerView != null) {
            return;
        }
        mAreaPickerView = new OptionsPickerView(getActivity());
        mAreaPickerView.setCancelable(true);// 触摸是否自动消失
        mAreaPickerView.setTitle("请选择城市");
        mAreaPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                if (options2Items.size() <= options1 || options2Items.get(options1).size() <=
                        options2) {
                    return;//避免pickview控件的bug
                }
                String areaText1 = options1Items.get(options1).getPickerViewText();
                String areaText2 = "", areaText3 = "";
                if (locationLevel == LOCATION_2LEVEL) {
                    areaText2 = options2Items.get(options1).get(options2).getPickerViewText();
                }
                if (locationLevel == LOCATION_3LEVEL) {
                    areaText2 = options2Items.get(options1).get(options2).getPickerViewText();
                    areaText3 = options3Items.get(options1).get(options2).get(options3)
                            .getPickerViewText();
                }
                areaText2 = areaText2.equals(getString(R.string.all)) ? "" : areaText2;//如果为全部则不显示
                areaText3 = areaText3.equals(getString(R.string.all)) ? "" : areaText3;//如果为全部则不显示
                setCity(areaText1 + areaText2 + areaText3);
                mCityOption1 = options1;
                mCityOption2 = options2;
                mCityOption3 = options3;
            }
        });
        mPresenter.getAreaData();
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
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        setGender(UserInfoBean.MALE);
                        mGenderPopupWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {
                        setGender(UserInfoBean.FEMALE);
                        mGenderPopupWindow.hide();
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItem3Clicked() {
                        setGender(UserInfoBean.SECRET);
                        mGenderPopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mGenderPopupWindow.hide();
                    }
                })
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
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        // 选择相册，单张
                        mPhotoSelector.getPhotoListFromSelector(1, null);
                        mPhotoPopupWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {
                        // 选择相机，拍照
                        mPhotoSelector.getPhotoFromCamera(null);
                        mPhotoPopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mPhotoPopupWindow.hide();
                    }
                }).build();
    }

    /**
     * 设置城市
     */
    private void setCity(String city) {
        mTvCity.setText(city);//更新位置
    }

    /**
     * 设置用户性别
     */
    private void setGender(String genderType) {
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
    private HashMap<String, String> packageUserInfo() {
        HashMap<String, String> fieldMap = new HashMap<>();
        // 图片上传的任务id，姓名。。。
        // 只上传改变的信息
        if (userNameChanged) {
            fieldMap.put(USER_NAME, mEtUserName.getText().toString());
        }
        if (sexChanged) {
            fieldMap.put(USER_SEX, mTvSex.getTag(R.id.view_data) + "");
        }
        if (cityChanged) {
            fieldMap.put(USER_LOCATION, mTvCity.getText().toString());
            fieldMap.put(USER_PROVINCE, options1Items.get(mCityOption1).getPickerViewText());// 省
            String city = options2Items.get(mCityOption1).get(mCityOption2).getPickerViewText();
            if (locationLevel == LOCATION_2LEVEL) {
                fieldMap.put(USER_CITY, city);// 市
            } else if (locationLevel == LOCATION_3LEVEL) {
                fieldMap.put(USER_CITY, city);// 市
                String area = options3Items.get(mCityOption1).get(mCityOption2).get(mCityOption3)
                        .getPickerViewText();
                fieldMap.put(USER_AREA, area);// 区
            }
        }
        if (introduceChanged) {
            fieldMap.put(USER_INTRO, mEtUserIntroduce.getInputContent());
        }
        if (upLoadCount > 0) {
            // avatar
            fieldMap.put(USER_STORAGE_TASK_ID, upDateHeadIconStorageId + "");
            fieldMap.put(USER_LOCAL_IMG_PATH, path);// 本地图片的路径，因为没有返回storage_id,用来更新图片
        }
        return fieldMap;
    }

    /**
     * 判断是否需要修改信息：如果头像，用户名，性别。。。其中任意一项发生变化，都可以提交修改
     */
    private void canChangerUserInfo() {
        if (userNameChanged || sexChanged || cityChanged || introduceChanged
                || upLoadCount > 0) {
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
            return "";
        }
        String intro = userInfoBean.getIntro();
        // 是缺省的内容,就设置为kong，但要注意这儿有个隐藏的bug，如果简介设置为缺省的内容，那就。。。
        if (getString(R.string.intro_default).equals(intro)) {
            intro = "";
        }
        return intro;
    }

    /**
     * 第一次点开城市选择器，需要判断我的地址在滑轮的哪儿
     */
    private void initCityPickerFirstTime() {
        if (!isFirstOpenCityPicker) {
            return;
        }
        isFirstOpenCityPicker = false;
        // 这样的情况基本不会发生
        if (mUserInfoBean == null || options1Items == null || options1Items.isEmpty()) {
            return;
        }
        // 初始化地区选择器
        String province = mUserInfoBean.getProvince();
        AreaBean provinceBean = new AreaBean();
        provinceBean.setAreaName(province);
        // 设置province初始位置
        mCityOption1 = options1Items.indexOf(provinceBean);
        // 如果没有找到位置，那就为0
        mCityOption1 = mCityOption1 == -1 ? 0 : mCityOption1;

        // 设置city初始位置
        String city = mUserInfoBean.getCity();
        if (TextUtils.isEmpty(city)) {
            city = getString(R.string.all);
        }
        AreaBean cityBean = new AreaBean();
        cityBean.setAreaName(city);
        mCityOption2 = options2Items.get(mCityOption1).indexOf(cityBean);
        // 如果没有找到位置，那就为0
        mCityOption2 = mCityOption2 == -1 ? 0 : mCityOption2;

        if (locationLevel == LOCATION_2LEVEL) {

        } else if (locationLevel == LOCATION_3LEVEL) {
            // 设置area初始位置
            String area = mUserInfoBean.getArea();
            if (TextUtils.isEmpty(area)) {
                area = getString(R.string.all);
            }
            AreaBean areaBean = new AreaBean();
            areaBean.setAreaName(area);
            mCityOption3 = options3Items.get(mCityOption1).get(mCityOption2).indexOf(areaBean);
            // 如果没有找到位置，那就为0
            mCityOption3 = mCityOption3 == -1 ? 0 : mCityOption3;
        }
    }

}
