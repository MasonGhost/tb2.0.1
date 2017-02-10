package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.EditConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.EditConfigBeanDaoImpl;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import java.io.File;
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
public class UserInfoFragment extends TSFragment<UserInfoContract.Presenter> implements UserInfoContract.View, PhotoSelectorImpl.IPhotoBackListener {

    private static final int GENDER_MALE = 0;// 性别男
    private static final int GENDER_FEMALE = 1;// 性别女
    private static final int GENDER_SECRET = 2;// 性别保密
    private static final int LOCATION_2LEVEL = 2;// 地区选择可选的级数为2，2级联动
    private static final int LOCATION_3LEVEL = 3;// 地区选择可选的级数为3

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
    @BindView(R.id.view_container)
    LinearLayout mViewContainer;

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

    private List<String> selectedPhotos = new ArrayList<>();// 被选择的图片
    private EditConfigBeanDaoImpl mEditConfigBeanDao;
    private int locationLevel = 2;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_user_info;
    }

    private void initConfig() {
        mEditConfigBeanDao = new EditConfigBeanDaoImpl(getContext());
        EditConfigBean editConfigBean = new EditConfigBean(1021L, "school", "学校", "TextView");
        mEditConfigBeanDao.saveSingleData(editConfigBean);
    }

    private void initUI() {
        List<EditConfigBean> editConfigBeanList = mEditConfigBeanDao.getMultiDataFromCache();
        for (EditConfigBean editConfigBean : editConfigBeanList) {
            String itemName = editConfigBean.getItemName();
            String itemType = editConfigBean.getItemType();
            String itemField = editConfigBean.getItemField();
            if (itemType.equals("TextView")) {
                CombinationButton combinationButton = new CombinationButton(getContext(), null);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                combinationButton.setLayoutParams(layoutParams);
                combinationButton.setLeftText(itemName);
                combinationButton.setTag(editConfigBean);
                mViewContainer.removeAllViews();
                mViewContainer.addView(combinationButton);
            }

        }
    }

    private HashMap<String, String> getNetParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < mViewContainer.getChildCount(); i++) {
            View view = mViewContainer.getChildAt(i);
            EditConfigBean editConfigBean = (EditConfigBean) view.getTag();
            hashMap.put(editConfigBean.getItemField(), "");// 通过自定义view接口获取要传递的值
        }
        return hashMap;
    }

    @Override
    protected void initView(View rootView) {
        mUserInfoBean = new UserInfoBean();
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl.SHAPE_RCTANGLE))
                .build().photoSelectorImpl();
        initCityPickerView();
      /*  initConfig();
        initUI();*/
        ////////////////////////监听所有的用户信息变化///////////////////////////////
        RxTextView.textChanges(mEtUserName)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        String oldUserName = mUserInfoBean.getName();
                        if (TextUtils.isEmpty(oldUserName)) {
                            userNameChanged = !TextUtils.isEmpty(charSequence);
                        } else {
                            userNameChanged = !mUserInfoBean.getName().equals(charSequence);
                        }
                        canChangerUserInfo();
                    }
                });
        RxTextView.textChanges(mTvSex)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        String oldSex = mUserInfoBean.getSex();
                        if (TextUtils.isEmpty(oldSex)) {
                            sexChanged = !TextUtils.isEmpty(charSequence);
                        } else {
                            sexChanged = !oldSex.equals(charSequence);
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
                            cityChanged = !oldLocation.equals(charSequence);
                        }
                        canChangerUserInfo();
                    }
                });
        RxTextView.textChanges(mEtUserIntroduce.getEtContent())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        String oldIntroduce = mUserInfoBean.getIntro();
                        if (TextUtils.isEmpty(oldIntroduce)) {
                            introduceChanged = !TextUtils.isEmpty(charSequence);
                        } else {
                            introduceChanged = !oldIntroduce.equals(charSequence);
                        }
                        canChangerUserInfo();
                    }
                });

    }

    @Override
    protected void initData() {

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

    @OnClick({R.id.rl_change_head_container, R.id.ll_sex_container, R.id.ll_city_container})
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
                mAreaPickerView.setSelectOptions(mCityOption1, mCityOption2, mCityOption3);
                mAreaPickerView.show();
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
    public void setAreaData(ArrayList<AreaBean> options1Items, ArrayList<ArrayList<AreaBean>> options2Items, ArrayList<ArrayList<ArrayList<AreaBean>>> options3Items) {
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
    public void setUpLoadHeadIconState(boolean upLoadState) {
        // 上传成功，可以进行修改
        if (upLoadState) {
            upLoadCount++;
            ToastUtils.showToast("头像上传成功");
        } else {
            ToastUtils.showToast("头像上传失败");
        }
        canChangerUserInfo();
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
        String filePath = photoList.get(0).getImgUrl();
        File file = new File(filePath);
        // 开始上传
        mPresenter.changeUserHeadIcon(FileUtils.getFileMD5ToString(file), file.getName(), filePath);
        // 加载本地图片
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(photoList.get(0).getImgUrl())
                .imagerView(mIvHeadIcon)
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
                /*if (options2Items.size() <= options1 || options2Items.get(options1).size() <= options2) {
                    return;//避免pickview控件的bug
                }*/
                String areaText1 = options1Items.get(options1).getPickerViewText();
                String areaText2 = "", areaText3 = "";
                if (locationLevel == LOCATION_2LEVEL) {
                    areaText2 = options2Items.get(options1).get(options2).getPickerViewText();
                }
                if (locationLevel == LOCATION_3LEVEL) {
                    areaText2 = options2Items.get(options1).get(options2).getPickerViewText();
                    areaText3 = options3Items.get(options1).get(options2).get(options3).getPickerViewText();
                }
                areaText2 = areaText2.equals("全部") ? "" : areaText2;//如果为全部则不显示
                areaText3 = areaText3.equals("全部") ? "" : areaText3;//如果为全部则不显示
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
                        setGender(GENDER_MALE);
                        mGenderPopupWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {
                        setGender(GENDER_FEMALE);
                        mGenderPopupWindow.hide();
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItem3Clicked() {
                        setGender(GENDER_SECRET);
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
                        mPhotoSelector.getPhotoListFromSelector(5,null);
                        mPhotoPopupWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {
                        // 选择相机，拍照
                        mPhotoSelector.getPhotoFromCamera();
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
    private void setGender(int genderType) {
        switch (genderType) {
            case GENDER_MALE:
                mTvSex.setText(R.string.male);
                //mTvSex.setTag();
                break;
            case GENDER_FEMALE:
                mTvSex.setText(R.string.female);
                //mTvSex.setTag();
                break;
            case GENDER_SECRET:
                mTvSex.setText(R.string.keep_secret);
                // mTvSex.setTag();
                break;
            default: // 没有该性别
        }

    }

    /**
     * 封装编辑用户信息的提交信息
     */
    private HashMap<String, String> packageUserInfo() {
        HashMap<String, String> fieldMap = new HashMap<>();
        // 图片上传的任务id，姓名。。。
        // 只上传改变的信息
        if (userNameChanged) {

        }
        if (sexChanged) {
            fieldMap.put("sex", mTvSex.getText().toString());
        }
        if (cityChanged) {
            
        }
        if (introduceChanged) {

        }
        if (upLoadCount > 0) {

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

}
