package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.yalantis.ucrop.UCrop;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static android.app.Activity.RESULT_OK;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
public class UserInfoFragment extends TSFragment<UserInfoContract.Presenter> implements UserInfoContract.View {

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

    private ArrayList<AreaBean> options1Items;
    private ArrayList<ArrayList<AreaBean>> options2Items;
    private int mCityOption1;//用来记录地区中滚轮的位置
    private int mCityOption2;
    private OptionsPickerView mAreaPickerView;// 地域选择器
    private ActionPopupWindow mGenderPopupWindow;// 性别选择弹框
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框

    private List<String> selectedPhotos = new ArrayList<>();// 被选择的图片

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initView(View rootView) {
        initCityPickerView();
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
                mAreaPickerView.setSelectOptions(mCityOption1, mCityOption2);
                mAreaPickerView.show();
                break;
        }
    }

    @Override
    public void setAreaData(ArrayList<AreaBean> options1Items, ArrayList<ArrayList<AreaBean>> options2Items) {
        this.options1Items = options1Items;
        this.options2Items = options2Items;
        mAreaPickerView.setPicker(options1Items, options2Items, true);
        mAreaPickerView.setCyclic(false);// 设置是否可以循环滚动
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
            public void onOptionsSelect(int options1, int option2, int options3) {
                if (options2Items.size() <= options1 || options2Items.get(options1).size() <= option2) {
                    return;//避免pickview控件的bug
                }
                String areaText = options1Items.get(options1).getPickerViewText();
                String city = options2Items.get(options1).get(option2).getPickerViewText();
                city = city.equals("全部") ? areaText : city;//如果为全部则不显示
                mTvCity.setText(city);//更新位置
                mCityOption1 = options1;
                mCityOption2 = option2;
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

                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {

                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItem3Clicked() {

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
                        PhotoPicker.builder()
                                .setPreviewEnabled(true)
                                .setGridColumnCount(3)
                                .setPhotoCount(1)
                                .setShowCamera(true)
                                .start(getActivity(), UserInfoFragment.this);
                        mPhotoPopupWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {
                        // 选择相机，拍照
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mPhotoPopupWindow.hide();
                    }
                }).build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if ((requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
                List<String> photos = null;
                if (data != null) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }
                startCropActivity(Uri.fromFile(new File(photos.get(0))));
                selectedPhotos.clear();
                if (photos != null) {
                    selectedPhotos.addAll(photos);
                }
            }

            if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }

            if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            }

        }
    }

    /**
     * 调用裁剪方法
     *
     * @param uri
     */
    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = "SampleCropImage.jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);//方形
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        uCrop.withOptions(options);
        uCrop.start(getActivity(), UserInfoFragment.this);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {

        } else {
            ToastUtils.showToast("Cannot retrieve cropped image");
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            ToastUtils.showToast(cropError.getMessage());
        } else {
            ToastUtils.showToast("Unexpected error");
        }
    }

}
