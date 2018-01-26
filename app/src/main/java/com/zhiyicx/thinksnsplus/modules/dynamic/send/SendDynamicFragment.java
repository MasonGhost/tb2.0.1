package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewActivity;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.TOLLBUNDLE;
import static com.zhiyicx.baseproject.impl.photoselector.Toll.DOWNLOAD_TOLL_TYPE;
import static com.zhiyicx.baseproject.impl.photoselector.Toll.LOOK_TOLL;
import static com.zhiyicx.baseproject.impl.photoselector.Toll.LOOK_TOLL_TYPE;


/**
 * @author LiuChao
 * @describe
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 * @See
 */
public class SendDynamicFragment extends TSFragment<SendDynamicContract.Presenter> implements
        SendDynamicContract.View, PhotoSelectorImpl.IPhotoBackListener {

    /**
     * recyclerView的每行item个数
     */
    private static final int ITEM_COLUM = 4;

    /**
     * 一共可选的图片数量
     */
    private static final int MAX_PHOTOS = 9;

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.et_dynamic_title)
    UserInfoInroduceInputView mEtDynamicTitle;
    @BindView(R.id.et_dynamic_content)
    UserInfoInroduceInputView mEtDynamicContent;
    @BindView(R.id.tv_toll)
    CombinationButton mTvToll;
    @BindView(R.id.send_dynamic_ll_toll)
    LinearLayout mLLToll;
    @BindView(R.id.ll_send_dynamic)
    LinearLayout ll_send_dynamic;
    @BindView(R.id.tv_choose_tip)
    TextView mTvChooseTip;
    @BindView(R.id.tv_word_limit)
    TextView mTvWordsLimit;
    @BindView(R.id.rb_one)
    RadioButton mRbOne;
    @BindView(R.id.rb_two)
    RadioButton mRbTwo;
    @BindView(R.id.rb_three)
    RadioButton mRbThree;
    @BindView(R.id.rb_days_group)
    RadioGroup mRbDaysGroup;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.sl_send_dynamic)
    ScrollView sl_send_dynamic;
    @BindView(R.id.v_horizontal_line)
    View mTitleUnderLine;

    @BindView(R.id.tv_custom_money)
    TextView mCustomMoney;

    /**
     * // 已经选择的图片
     */
    private List<ImageBean> selectedPhotos;

    private CommonAdapter<ImageBean> mCommonAdapter;

    private List<ImageBean> cachePhotos;

    /**
     * 图片选择弹框
     */
    private ActionPopupWindow mPhotoPopupWindow;

    private ActionPopupWindow mCanclePopupWindow;
    private PhotoSelectorImpl mPhotoSelector;

    /**
     * 状态值用来判断发送状态
     */
    private boolean hasContent;

    /**
     * 需要发送的动态类型
     */
    private int dynamicType;

    /**
     * 是否开启收费
     */
    private boolean isToll;

    private boolean isFromGroup;

    /**
     * 是否有图片设置了收费
     */
    private boolean hasTollPic;

    /**
     * 文字收费选择
     */
    private ArrayList<Float> mSelectMoney;

    /**
     * 文字收费金额
     */
    private double mTollMoney;

    /**
     * 各类提示信息弹窗
     */
    private ActionPopupWindow mInstructionsPopupWindow;

    public static SendDynamicFragment initFragment(Bundle bundle) {
        SendDynamicFragment sendDynamicFragment = new SendDynamicFragment();
        sendDynamicFragment.setArguments(bundle);
        return sendDynamicFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_send_dynamic;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.send_dynamic);
    }

    @Override
    protected String setLeftTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.publish);
    }

    @Override
    protected void setLeftClick() {
        handleBack();
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    /**
     * 处理取消发布动态
     */
    private void handleBack() {
        boolean noPic = selectedPhotos == null || !isToll && selectedPhotos.size() <= 1;
        if (hasContent || !noPic) {
            DeviceUtils.hideSoftKeyboard(getContext(), mEtDynamicContent);
            initCanclePopupWindow();
            mCanclePopupWindow.show();
        } else {
            super.setLeftClick();
        }
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        // 设置右边发布文字监听
        initSendDynamicBtnState();
        // 设置左边取消文字的颜色为主题色
        setLeftTextColor();
        initDynamicType();
        setSendDynamicState();
        initWordsToll();
        // 配置收费按钮隐藏显示
        initTollState();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AndroidBug5497Workaround.assistActivity(getActivity());
        }
    }

    private void initTollState() {
        boolean canPay = mPresenter.getSystemConfigBean().getFeed().hasPaycontrol();
        mTvToll.setVisibility(canPay ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initData() {
        mSelectMoney = new ArrayList<>();
        mSelectMoney.add(1f);
        mSelectMoney.add(5f);
        mSelectMoney.add(10f);

        mSystemConfigBean = mPresenter.getSystemConfigBean();

        if (mSystemConfigBean != null) {
            int[] amount = new int[]{};
            if (mSystemConfigBean.getFeed() != null) {
                amount = mSystemConfigBean.getFeed().getItems();
                int wordLimit = mSystemConfigBean.getFeed().getLimit();
                mTvWordsLimit.setText(String.format(getString(R.string.dynamic_send_toll_notes), wordLimit > 0 ? wordLimit : 50));
            }
            if (amount != null && amount.length > 2) {
                mSelectMoney.add(0, (float) PayConfig.realCurrency2GameCurrency(amount[0], mSystemConfigBean.getWallet_ratio()));
                mSelectMoney.add(1, (float) PayConfig.realCurrency2GameCurrency(amount[1], mSystemConfigBean.getWallet_ratio()));
                mSelectMoney.add(2, (float) PayConfig.realCurrency2GameCurrency(amount[2], mSystemConfigBean.getWallet_ratio()));
            }
        }
        initSelectMoney(mSelectMoney);
        mCustomMoney.setText(mPresenter.getGoldName());
    }

    @Override
    public double getTollMoney() {
        return mTollMoney;
    }

    @Override
    public boolean hasTollVerify() {
        return isToll && !hasTollPic;
    }

    private void initSelectMoney(List<Float> mSelectDays) {
        mRbOne.setText(String.format(getString(R.string.dynamic_send_toll_select_money),
                mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.dynamic_send_toll_select_money),
                mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.dynamic_send_toll_select_money),
                mSelectDays.get(2)));
    }

    private void initWordsToll() {
        mTvChooseTip.setText(R.string.dynamic_send_toll_words_count);
        RxTextView.textChanges(mEtInput).subscribe(charSequence -> {
            String spaceReg = "\n|\t";
            if (TextUtils.isEmpty(charSequence.toString().replaceAll(spaceReg, ""))) {
                return;
            }
            mRbDaysGroup.clearCheck();
            mTollMoney = Double.parseDouble(charSequence.toString());
        }, throwable -> mTollMoney = 0);

        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.bindToLifecycle())
                .subscribe(checkedId -> {
                    if (checkedId != -1) {
                        setCustomMoneyDefault();
                    }
                    switch (checkedId) {
                        case R.id.rb_one:
                            mTollMoney = mSelectMoney.get(0);
                            break;
                        case R.id.rb_two:
                            mTollMoney = mSelectMoney.get(1);
                            break;
                        case R.id.rb_three:
                            mTollMoney = mSelectMoney.get(2);
                            break;
                        default:
                            break;
                    }
                });
    }

    /**
     * 设置自定义金额数量
     */
    private void setCustomMoneyDefault() {
        mEtInput.setText("");
    }

    /**
     * 初始化图片选择弹框
     * 现在不提供图片选择弹窗，进入界面是选择动态类型
     */
    @Deprecated
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
                    ArrayList<String> photos = new ArrayList<>();
                    // 最后一张是占位图
                    for (int i = 0; i < selectedPhotos.size(); i++) {
                        ImageBean imageBean = selectedPhotos.get(i);
                        if (!TextUtils.isEmpty(imageBean.getImgUrl())) {
                            photos.add(imageBean.getImgUrl());
                        }
                    }
                    mPhotoSelector.getPhotoListFromSelector(MAX_PHOTOS, photos);
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    ArrayList<String> photos = new ArrayList<>();
                    // 最后一张是占位图
                    for (int i = 0; i < selectedPhotos.size(); i++) {
                        ImageBean imageBean = selectedPhotos.get(i);
                        if (!TextUtils.isEmpty(imageBean.getImgUrl())) {
                            photos.add(imageBean.getImgUrl());
                        }
                    }
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(photos);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
    }

    /**
     * 初始化取消选择弹框
     */
    private void initCanclePopupWindow() {
        if (mCanclePopupWindow != null) {
            return;
        }
        mCanclePopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_send_cancel_hint))
                .item2Str(getString(R.string.determine))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(() -> {
                    mCanclePopupWindow.hide();
                    getActivity().finish();
                })
                .bottomClickListener(() -> mCanclePopupWindow.hide()).build();
    }

    /**
     * 初始化图片选择器
     */
    private void initPhotoSelector() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (isPhotoListChanged(cachePhotos, photoList)) {
            hasTollPic = false;
            selectedPhotos.clear();
            selectedPhotos.addAll(photoList);
            addPlaceHolder();
            setSendDynamicState();// 每次刷新图片后都要判断发布按钮状态
            mCommonAdapter.notifyDataSetChanged();
        }
    }

    private void addPlaceHolder() {
        if (selectedPhotos.size() < MAX_PHOTOS) {
            // 占位缺省图
            ImageBean camera = new ImageBean();
            selectedPhotos.add(camera);
        }

    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        ToastUtils.showToast(errorMsg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片选择器返回结果
        if (mPhotoSelector != null) {
            // 图片选择器界面数据保存操作
            if (data != null) {
                Bundle tollBundle = new Bundle();
                data.putExtra(TOLLBUNDLE, tollBundle);
                List<ImageBean> oldData = mCommonAdapter.getDatas();
                if (oldData == null) {
                    oldData = new ArrayList<>();
                }
                tollBundle.putParcelableArrayList(TOLLBUNDLE, new ArrayList<>(oldData));
            }
            mPhotoSelector.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setPresenter(SendDynamicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void sendDynamicComplete() {
        getActivity().finish();
    }

    @Override
    public SendDynamicDataBean getDynamicSendData() {
        Bundle bundle = getArguments();
        SendDynamicDataBean sendDynamicDataBean = null;
        if (bundle != null) {
            sendDynamicDataBean = bundle.getParcelable(SendDynamicActivity.SEND_DYNAMIC_DATA);
        }
        return sendDynamicDataBean;
    }

    @Override
    protected void setRightClick() {
        com.zhiyicx.common.utils.DeviceUtils.hideSoftKeyboard(getContext(), mToolbarRight);
        // 圈子
        if (isFromGroup) {
            mPresenter.sendGroupDynamic(packageGroupDynamicData());
        } else {
            mPresenter.sendDynamicV2(packageDynamicData());
        }

    }

    /**
     * 封装动态上传的数据
     */
    @Override
    public void packageDynamicStorageDataV2(SendDynamicDataBeanV2 sendDynamicDataBeanV2) {
        List<SendDynamicDataBeanV2.StorageTaskBean> storage_task = new ArrayList<>();
        List<ImageBean> photos = new ArrayList<>();
        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
            for (int i = 0; i < selectedPhotos.size(); i++) {
                if (!TextUtils.isEmpty(selectedPhotos.get(i).getImgUrl())) {
                    SendDynamicDataBeanV2.StorageTaskBean taskBean = new SendDynamicDataBeanV2.StorageTaskBean();
                    ImageBean imageBean = selectedPhotos.get(i);
                    photos.add(imageBean);
                    taskBean.setAmount(imageBean.getToll_monye() > 0 ? (long) (PayConfig.gameCurrency2RealCurrency(imageBean.getToll_monye(),
                            mPresenter.getRatio())) : null);
                    taskBean.setType(imageBean.getToll_monye() * imageBean.getToll_type() > 0
                            ? (imageBean.getToll_type() == LOOK_TOLL ? LOOK_TOLL_TYPE : DOWNLOAD_TOLL_TYPE) : null);
                    storage_task.add(taskBean);
                }
            }
        }
        sendDynamicDataBeanV2.setPhotos(photos);
        sendDynamicDataBeanV2.setStorage_task(storage_task);
    }

    @Override
    public void packageGroupDynamicStorageData(GroupSendDynamicDataBean sendDynamicDataBeanV2) {
        List<ImageBean> photos = new ArrayList<>();
        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
            for (int i = 0; i < selectedPhotos.size(); i++) {
                if (!TextUtils.isEmpty(selectedPhotos.get(i).getImgUrl())) {
                    SendDynamicDataBeanV2.StorageTaskBean taskBean = new SendDynamicDataBeanV2.StorageTaskBean();
                    ImageBean imageBean = selectedPhotos.get(i);
                    photos.add(imageBean);
                    taskBean.setAmount(imageBean.getToll_monye() > 0 ? (long) (PayConfig.gameCurrency2RealCurrency(imageBean.getToll_monye(),
                            mPresenter.getRatio())) : null);
                    taskBean.setType(imageBean.getToll_monye() * imageBean.getToll_type() > 0
                            ? (imageBean.getToll_type() == LOOK_TOLL ? LOOK_TOLL_TYPE : DOWNLOAD_TOLL_TYPE) : null);
                }
            }
        }
        sendDynamicDataBeanV2.setPhotos(photos);
    }

    @Override
    public boolean wordsNumLimit() {
        return mLLToll.getVisibility() == View.VISIBLE;
    }

    private void setLeftTextColor() {
        mToolbarLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
    }

    /**
     * 发布按钮的状态监听
     */
    private void initSendDynamicBtnState() {

        mEtDynamicContent.setContentChangedListener(s -> {
            hasContent = !TextUtils.isEmpty(s);
            setSendDynamicState();
        });

        mTvToll.setRightImageClickListener(v -> {
            if (!mPresenter.getSystemConfigBean().getFeed().hasPaycontrol()) {
                showSnackErrorMessage(getString(R.string.dynamic_close_pay));
                return;
            }
            isToll = !isToll;
            mTvToll.setRightImage(isToll ? R.mipmap.btn_open : R.mipmap.btn_close);
            if (dynamicType == SendDynamicDataBean.TEXT_ONLY_DYNAMIC) {
                mLLToll.setVisibility(isToll ? View.VISIBLE : View.GONE);
                if (!isToll) {
                    mTollMoney = 0;
                }
                sl_send_dynamic.smoothScrollTo(0, 0);
            } else {
                mCommonAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 设置动态发布按钮的点击状态
     */
    private void setSendDynamicState() {
        // 没有内容，并且只有占位图时不能够发送
        boolean noPic = selectedPhotos == null || !isToll && selectedPhotos.size() <= 1;
        if (!hasContent && noPic) {
            mToolbarRight.setEnabled(false);
        } else {
            // 有内容或者有图片时都可以发送
            mToolbarRight.setEnabled(true);
        }
    }

    /**
     * 封装动态上传的数据
     */
    private DynamicDetailBeanV2 packageDynamicData() {

        DynamicDetailBeanV2 dynamicDetailBeanV2 = new DynamicDetailBeanV2();

        long userId = AppApplication.getmCurrentLoginAuth() != null ? AppApplication
                .getmCurrentLoginAuth().getUser_id() : 0;
        String feedMarkString = userId + "" + System.currentTimeMillis();
        long feedMark = Long.parseLong(feedMarkString);

        // 浏览量没有 0 ，从1 开始
        dynamicDetailBeanV2.setFeed_view_count(1);
        dynamicDetailBeanV2.setFeed_mark(feedMark);
        dynamicDetailBeanV2.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        dynamicDetailBeanV2.setFeed_content(mEtDynamicContent.getInputContent());
        dynamicDetailBeanV2.setFeed_from(ApiConfig.ANDROID_PLATFORM);
        // 因为关注里面需要显示我的动态
        dynamicDetailBeanV2.setIsFollowed(true);
        dynamicDetailBeanV2.setState(DynamicDetailBeanV2.SEND_ING);
        dynamicDetailBeanV2.setComments(new ArrayList<>());
        dynamicDetailBeanV2.setUser_id(userId);
        dynamicDetailBeanV2.setAmount((long) mTollMoney);

        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
            List<DynamicDetailBeanV2.ImagesBean> images = new ArrayList<>();
            // 最后一张占位图，扔掉
            for (int i = 0; i < selectedPhotos.size(); i++) {
                if (!TextUtils.isEmpty(selectedPhotos.get(i).getImgUrl())) {
                    DynamicDetailBeanV2.ImagesBean imagesBean = new DynamicDetailBeanV2.ImagesBean();
                    imagesBean.setImgUrl(selectedPhotos.get(i).getImgUrl());
                    BitmapFactory.Options options = DrawableProvider.getPicsWHByFile
                            (selectedPhotos.get(i).getImgUrl());
                    imagesBean.setHeight(options.outHeight);
                    imagesBean.setWidth(options.outWidth);
                    imagesBean.setImgMimeType(options.outMimeType);
                    images.add(imagesBean);
                }
            }
            dynamicDetailBeanV2.setImages(images);
        }

        return dynamicDetailBeanV2;
    }

    private GroupDynamicListBean packageGroupDynamicData() {
        GroupDynamicListBean groupSendDynamicDataBean = new GroupDynamicListBean();
        long userId = AppApplication.getmCurrentLoginAuth() != null ? AppApplication
                .getmCurrentLoginAuth().getUser_id() : 0;

        String feedMarkString = userId + "" + System.currentTimeMillis();
        long feedMark = Long.parseLong(feedMarkString);

        groupSendDynamicDataBean.setViews(1);
        groupSendDynamicDataBean.setFeed_mark(feedMark);
        groupSendDynamicDataBean.setGroup_id((int) getDynamicSendData().getDynamicChannlId());
        groupSendDynamicDataBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        groupSendDynamicDataBean.setContent(mEtDynamicContent.getInputContent());
        groupSendDynamicDataBean.setTitle(mEtDynamicTitle.getInputContent());
        groupSendDynamicDataBean.setNew_comments(new ArrayList<>());
        groupSendDynamicDataBean.setUser_id(userId);


        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
            List<GroupDynamicListBean.ImagesBean> images = new ArrayList<>();
            // 最后一张占位图，扔掉
            for (int i = 0; i < selectedPhotos.size(); i++) {
                if (!TextUtils.isEmpty(selectedPhotos.get(i).getImgUrl())) {
                    GroupDynamicListBean.ImagesBean imagesBean = new GroupDynamicListBean.ImagesBean();
                    imagesBean.setImgUrl(selectedPhotos.get(i).getImgUrl());
                    BitmapFactory.Options options = DrawableProvider.getPicsWHByFile
                            (selectedPhotos.get(i).getImgUrl());
                    imagesBean.setHeight(options.outHeight);
                    imagesBean.setWidth(options.outWidth);
                    imagesBean.setImgMimeType(options.outMimeType);
                    images.add(imagesBean);
                }
            }
            groupSendDynamicDataBean.setImages(images);
        }

        return groupSendDynamicDataBean;
    }

    /**
     * 初始化图片列表
     */
    private void initPhotoList(Bundle bundle) {
        if (selectedPhotos == null) {
            selectedPhotos = new ArrayList<>();
        }
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), ITEM_COLUM);
        mRvPhotoList.setLayoutManager(gridLayoutManager);
        // 设置recyclerview的item之间的空白
        int witdh = ConvertUtils.dp2px(getContext(), 5);
        mRvPhotoList.addItemDecoration(new GridDecoration(witdh, witdh));
        // 占位缺省图
        addPlaceHolder();
        mCommonAdapter = new CommonAdapter<ImageBean>(getContext(), R.layout
                .item_send_dynamic_photo_list, selectedPhotos) {
            @Override
            protected void convert(ViewHolder holder, final ImageBean imageBean, final int
                    position) {
                // 固定每个item的宽高
                // 屏幕宽高减去左右margin以及item之间的空隙
                int width = UIUtils.getWindowWidth(getContext()) - getResources()
                        .getDimensionPixelSize(R.dimen.spacing_large) * 2
                        - ConvertUtils.dp2px(getContext(), 5) * (ITEM_COLUM - 1);
                View convertView = holder.getConvertView();
                convertView.getLayoutParams().width = width / ITEM_COLUM;
                convertView.getLayoutParams().height = width / ITEM_COLUM;
                final ImageView imageView = holder.getView(R.id.iv_dynamic_img);
                final ImageView paintView = holder.getView(R.id.iv_dynamic_img_paint);
                final View filterView = holder.getView(R.id.iv_dynamic_img_filter);
                if (TextUtils.isEmpty(imageBean.getImgUrl())) {
                    // 最后一项作为占位图
                    paintView.setImageResource(R.mipmap.ico_edit_pen);
                    filterView.setVisibility(View.GONE);
                    imageView.setImageResource(R.mipmap.img_edit_photo_frame);
                } else {
                    paintView.setVisibility(isToll ? View.VISIBLE : View.GONE);
                    filterView.setVisibility(isToll ? View.VISIBLE : View.GONE);
                    if (!isToll) {
                        imageBean.setToll(null);
                    }

                    if (imageBean.getToll_type() > 0) {
                        hasTollPic = true;
                        filterView.setVisibility(View.VISIBLE);
                        paintView.setImageResource(R.mipmap.ico_lock);
                    } else {
                        paintView.setImageResource(R.mipmap.ico_edit_pen);
                        filterView.setVisibility(View.GONE);
                    }
                    Glide.with(getContext())
                            .load(imageBean.getImgUrl())
                            .placeholder(R.drawable.shape_default_image)
                            .error(R.drawable.shape_default_image)
                            .override(convertView.getLayoutParams().width, convertView.getLayoutParams().height)
                            .into(imageView);

                }
                imageView.setOnClickListener(v -> {
                    DeviceUtils.hideSoftKeyboard(getContext(), v);
                    if (TextUtils.isEmpty(imageBean.getImgUrl())) {
                        ArrayList<String> photos = new ArrayList<>();
                        // 最后一张是占位图
                        for (int i = 0; i < selectedPhotos.size(); i++) {
                            ImageBean imageBean1 = selectedPhotos.get(i);
                            if (!TextUtils.isEmpty(imageBean1.getImgUrl())) {
                                photos.add(imageBean1.getImgUrl());
                            }
                        }
                        mPhotoSelector.getPhotoListFromSelector(MAX_PHOTOS, photos);
                    } else {
                        // 预览图片
                        ArrayList<String> photos = new ArrayList<>();
                        // 最后一张是占位图
                        for (int i = 0; i < selectedPhotos.size(); i++) {
                            ImageBean imageBean1 = selectedPhotos.get(i);
                            if (!TextUtils.isEmpty(imageBean1.getImgUrl())) {
                                photos.add(imageBean1.getImgUrl());
                            }
                        }
                        ArrayList<AnimationRectBean> animationRectBeanArrayList
                                = new ArrayList<>();
                        for (int i = 0; i < photos.size(); i++) {

                            if (i < gridLayoutManager.findFirstVisibleItemPosition()) {
                                // 顶部，无法全部看见的图片
                                AnimationRectBean rect = new AnimationRectBean();
                                animationRectBeanArrayList.add(rect);
                            } else if (i > gridLayoutManager.findLastVisibleItemPosition()) {
                                // 底部，无法完全看见的图片
                                AnimationRectBean rect = new AnimationRectBean();
                                animationRectBeanArrayList.add(rect);
                            } else {
                                View view = gridLayoutManager
                                        .getChildAt(i - gridLayoutManager
                                                .findFirstVisibleItemPosition());
                                ImageView imageView1 = (ImageView) view.findViewById(R.id
                                        .iv_dynamic_img);
                                // 可以完全看见的图片
                                AnimationRectBean rect = AnimationRectBean.buildFromImageView
                                        (imageView1);
                                animationRectBeanArrayList.add(rect);
                            }
                        }
                        ArrayList<ImageBean> datas = new ArrayList<>();
                        datas.addAll(selectedPhotos);
                        cachePhotos = new ArrayList<>(selectedPhotos);
                        PhotoViewActivity.startToPhotoView(SendDynamicFragment.this,
                                photos, photos, animationRectBeanArrayList, MAX_PHOTOS,
                                position, isToll, datas);
                    }
                });
            }
        };

        mRvPhotoList.setAdapter(mCommonAdapter);
    }

    /**
     * 根据 dynamicType 初始化界面布局，比如发纯文字动态就隐藏图片
     */
    private void initDynamicType() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            SendDynamicDataBean sendDynamicDataBean = bundle.getParcelable(SendDynamicActivity
                    .SEND_DYNAMIC_DATA);
            dynamicType = sendDynamicDataBean.getDynamicType();
            List<ImageBean> originPhotos = sendDynamicDataBean.getDynamicPrePhotos();
            if (originPhotos != null) {
                selectedPhotos = new ArrayList<>(MAX_PHOTOS);
                selectedPhotos.addAll(originPhotos);
            }
            if (sendDynamicDataBean.getDynamicBelong() == SendDynamicDataBean.GROUP_DYNAMIC) {
                isFromGroup = true;
//                mEtDynamicTitle.setVisibility(View.VISIBLE);
//                mTitleUnderLine.setVisibility(View.VISIBLE);
                mTvToll.setVisibility(View.GONE);
            }
        }
        switch (dynamicType) {
            case SendDynamicDataBean.PHOTO_TEXT_DYNAMIC:
                // 没有图片就初始化这些
                initPhotoSelector();
                initPhotoList(bundle);
                break;
            case SendDynamicDataBean.TEXT_ONLY_DYNAMIC:
                hasTollPic = true;
                // 隐藏图片控件
                mRvPhotoList.setVisibility(View.GONE);
                mEtDynamicContent.getEtContent().setHint(getString(R.string
                        .dynamic_content_no_pic_hint));
                break;
            default:
        }
    }

    /**
     * 图片列表返回后，判断图片列表内容以及顺序是否发生变化，如果没变，就可以不用刷新
     */
    private boolean isPhotoListChanged(List<ImageBean> oldList, List<ImageBean> newList) {
        if (!newList.isEmpty()) {
            return true;
        }
        if (oldList == null || oldList.isEmpty()) {
            return false;
        }
        // 取消了所有选择的图片
        if (newList == null || newList.isEmpty()) {
            return oldList.size() > 1;
        } else {
            int oldSize = 0;
            // 最后一张是占位图
            if (TextUtils.isEmpty(oldList.get(oldList.size() - 1).getImgUrl())) {
                oldSize = oldList.size() - 1;
            } else {
                oldSize = oldList.size();
            }
            if (oldSize != newList.size()) {
                // 如果长度不同，那肯定改变了
                return true;
            } else {
                // 继续判断内容和顺序变了没有
                for (int i = 0; i < newList.size(); i++) {
                    ImageBean newImageBean = newList.get(i);
                    ImageBean oldImageBean = oldList.get(i);

                    if (!oldImageBean.equals(newImageBean) || !newImageBean.getToll().equals(oldImageBean.getToll())) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    @Override
    public void initInstructionsPop(String title, String des) {
        if (mInstructionsPopupWindow != null) {
            mInstructionsPopupWindow = mInstructionsPopupWindow.newBuilder()
                    .item1Str(title)
                    .desStr(des)
                    .build();
            mInstructionsPopupWindow.show();
            return;
        }
        mInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(title)
                .desStr(des)
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mInstructionsPopupWindow.hide())
                .build();
        mInstructionsPopupWindow.show();
    }

}
