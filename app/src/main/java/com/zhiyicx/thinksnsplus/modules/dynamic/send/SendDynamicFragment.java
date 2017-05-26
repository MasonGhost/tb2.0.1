package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewActivity;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;


/**
 * @author LiuChao
 * @describe
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 * @See
 */
public class SendDynamicFragment extends TSFragment<SendDynamicContract.Presenter> implements SendDynamicContract.View, PhotoSelectorImpl.IPhotoBackListener {
    private static final int ITEM_COLUM = 4;// recyclerView的每行item个数
    private static final int MAX_PHOTOS = 9;// 一共可选的图片数量
    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.et_dynamic_title)
    UserInfoInroduceInputView mEtDynamicTitle;
    @BindView(R.id.et_dynamic_content)
    UserInfoInroduceInputView mEtDynamicContent;
    @BindView(R.id.tv_toll)
    CombinationButton mTvToll;
    @BindView(R.id.ll_toll)
    LinearLayout mLLToll;
    @BindView(R.id.tv_choose_tip)
    TextView mTvChooseTip;
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

    private List<ImageBean> selectedPhotos;
    private CommonAdapter<ImageBean> mCommonAdapter;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private ActionPopupWindow mCanclePopupWindow;// 取消提示选择弹框
    private PhotoSelectorImpl mPhotoSelector;
    private boolean hasContent, hasPics;// 状态值用来判断发送状态
    private int dynamicType;// 需要发送的动态类型
    private boolean isToll;
    private ArrayList<Float> mSelectDays;

    private int mPayType;

    private double mRechargeMoney;

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
        if (!TextUtils.isEmpty(mEtDynamicContent.getInputContent()) // 有正文
                // || !TextUtils.isEmpty(mEtDynamicTitle.getInputContent())//
                // 有图片，并且长度大于1，因为为1的时候是，占位图
                || (selectedPhotos != null && selectedPhotos.size() > 1)) {
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

        initSendDynamicBtnState();// 设置右边发布文字监听
        setLeftTextColor();// 设置左边取消文字的颜色为主题色
        initDynamicType();
        setSendDynamicState();
        initWordsToll();

    }

    @Override
    protected void initData() {
        mSelectDays = new ArrayList<>();
        mSelectDays.add(1f);
        mSelectDays.add(5f);
        mSelectDays.add(10f);
        initSelectDays(mSelectDays);
    }

    private void initSelectDays(List<Float> mSelectDays) {
        mRbOne.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectDays.get(2)));
    }

    private void initWordsToll() {
        mTvChooseTip.setText(R.string.dynamic_send_toll_words_count);
        RxTextView.afterTextChangeEvents(mEtInput)
                .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                        if (TextUtils.isEmpty(textViewAfterTextChangeEvent.editable().toString())) {
                            return;
                        }

                        if (textViewAfterTextChangeEvent.editable().toString().contains(".")) {
                            setCustomMoneyDefault();
                            com.zhiyicx.common.utils.DeviceUtils.hideSoftKeyboard(getContext(), mEtInput);
                        } else {
                            mRbDaysGroup.clearCheck();
                            try {
                                mRechargeMoney = Double.parseDouble(textViewAfterTextChangeEvent.editable().toString());
                            } catch (NumberFormatException ne) {

                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        setCustomMoneyDefault();
                        mRechargeMoney = 0;
                    }
                });
        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer checkedId) {
                        switch (checkedId) {
                            case R.id.rb_one:
                                mRechargeMoney = mSelectDays.get(0);
                                break;
                            case R.id.rb_two:
                                mRechargeMoney = mSelectDays.get(1);
                                break;
                            case R.id.rb_three:
                                mRechargeMoney = mSelectDays.get(2);
                                break;
                        }
                        setCustomMoneyDefault();
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
                    public void onItemClicked() {
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
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        ArrayList<String> photos = new ArrayList<String>();
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
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPhotoPopupWindow.hide();
                    }
                }).build();
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
                .item2Str(getString(R.string.sure))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mCanclePopupWindow.hide();
                        getActivity().finish();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mCanclePopupWindow.hide();
                    }
                }).build();
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
        Glide.with(getActivity()).load("");
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (isPhotoListChanged(selectedPhotos, photoList)) {
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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片选择器返回结果
        if (mPhotoSelector != null) {
            mPhotoSelector.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setPresenter(SendDynamicContract.Presenter presenter) {
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
        mPresenter.sendDynamic(packageDynamicData());
    }

    private void setLeftTextColor() {
        mToolbarLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
    }

    /**
     * 发布按钮的状态监听
     */
    private void initSendDynamicBtnState() {
        mEtDynamicContent.setContentChangedListener(new UserInfoInroduceInputView.ContentChangedListener() {
            @Override
            public void contentChanged(CharSequence s) {
                hasContent = !TextUtils.isEmpty(s);
                setSendDynamicState();
            }
        });
        mTvToll.setRightImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToll = !isToll;
                mTvToll.setRightImage(isToll ? R.mipmap.btn_open : R.mipmap.btn_close);
                if (dynamicType == SendDynamicDataBean.TEXT_ONLY_DYNAMIC) {
                    mLLToll.setVisibility(isToll ? View.VISIBLE : View.GONE);
                }

            }
        });

    }

    /**
     * 设置动态发布按钮的点击状态
     */
    private void setSendDynamicState() {
        // 没有内容，并且只有占位图时不能够发送
        if (!hasContent && (selectedPhotos == null || selectedPhotos.size() <= 1)) {
            mToolbarRight.setEnabled(false);
        } else {
            // 有内容或者有图片时都可以发送
            mToolbarRight.setEnabled(true);
        }
    }

    /**
     * 封装动态上传的数据
     */
    private DynamicBean packageDynamicData() {
        long userId = AppApplication.getmCurrentLoginAuth() != null ? AppApplication.getmCurrentLoginAuth().getUser_id() : 0;
        String feedMarkString = userId + "" + System.currentTimeMillis();
        long feedMark = Long.parseLong(feedMarkString);
        DynamicToolBean toolBean = new DynamicToolBean();
        toolBean.setFeed_mark(feedMark);
        toolBean.setFeed_view_count(1);// 浏览量没有 0 ，从1 开始
        DynamicDetailBean dynamicDetailBean = new DynamicDetailBean();
        dynamicDetailBean.setFeed_mark(feedMark);
        dynamicDetailBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        dynamicDetailBean.setContent(mEtDynamicContent.getInputContent());
        dynamicDetailBean.setTitle(mEtDynamicTitle.getInputContent());
        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
            List<ImageBean> photos = new ArrayList<>();
            // 最后一张占位图，扔掉
            for (int i = 0; i < selectedPhotos.size(); i++) {
                if (!TextUtils.isEmpty(selectedPhotos.get(i).getImgUrl())) {
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImgUrl(selectedPhotos.get(i).getImgUrl());
                    BitmapFactory.Options options = DrawableProvider.getPicsWHByFile(selectedPhotos.get(i).getImgUrl());
                    imageBean.setHeight(options.outHeight);
                    imageBean.setWidth(options.outWidth);
                    imageBean.setImgMimeType(options.outMimeType);
                    photos.add(imageBean);
                }
            }

            dynamicDetailBean.setStorages(photos);
        }
        dynamicDetailBean.setFeed_from(ApiConfig.ANDROID_PLATFORM);
        DynamicBean dynamicBean = new DynamicBean();
        dynamicBean.setIsFollowed(true); // 因为关注里面需要显示我的动态
        dynamicBean.setFeed(dynamicDetailBean);
        dynamicBean.setState(DynamicBean.SEND_ING);
        dynamicBean.setFeed_mark(feedMark);
        dynamicBean.setUser_id(userId);
        dynamicBean.setTool(toolBean);
        return dynamicBean;
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
        mCommonAdapter = new CommonAdapter<ImageBean>(getContext(), R.layout.item_send_dynamic_photo_list, selectedPhotos) {
            @Override
            protected void convert(ViewHolder holder, final ImageBean imageBean, final int position) {
                // 固定每个item的宽高
                // 屏幕宽高减去左右margin以及item之间的空隙
                int width = UIUtils.getWindowWidth(getContext()) - getResources().getDimensionPixelSize(R.dimen.spacing_large) * 2
                        - ConvertUtils.dp2px(getContext(), 5) * (ITEM_COLUM - 1);
                View convertView = holder.getConvertView();
                convertView.getLayoutParams().width = width / ITEM_COLUM;
                convertView.getLayoutParams().height = width / ITEM_COLUM;
                final ImageView imageView = holder.getView(R.id.iv_dynamic_img);
                if (TextUtils.isEmpty(imageBean.getImgUrl())) {
                    // 最后一项作为占位图
                    imageView.setImageResource(R.mipmap.img_edit_photo_frame);
                } else {
                    ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
                    imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                            .imagerView(imageView)
                            .url(imageBean.getImgUrl())
                            .build());
                }
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeviceUtils.hideSoftKeyboard(getContext(), v);
                        if (TextUtils.isEmpty(imageBean.getImgUrl())) {
                            initPhotoPopupWindow();
                            mPhotoPopupWindow.show();
                        } else {
                            // 预览图片
                            ArrayList<String> photos = new ArrayList<String>();
                            // 最后一张是占位图
                            for (int i = 0; i < selectedPhotos.size(); i++) {
                                ImageBean imageBean = selectedPhotos.get(i);
                                if (!TextUtils.isEmpty(imageBean.getImgUrl())) {
                                    photos.add(imageBean.getImgUrl());
                                }
                            }
                            ArrayList<AnimationRectBean> animationRectBeanArrayList
                                    = new ArrayList<AnimationRectBean>();
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
                                            .getChildAt(i - gridLayoutManager.findFirstVisibleItemPosition());
                                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_dynamic_img);
                                    // 可以完全看见的图片
                                    AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView);
                                    animationRectBeanArrayList.add(rect);
                                }
                            }
                            PhotoViewActivity.startToPhotoView(SendDynamicFragment.this, photos, photos, animationRectBeanArrayList, MAX_PHOTOS, position);
                        }
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
            SendDynamicDataBean sendDynamicDataBean = bundle.getParcelable(SendDynamicActivity.SEND_DYNAMIC_DATA);
            dynamicType = sendDynamicDataBean.getDynamicType();
            List<ImageBean> originPhotos = sendDynamicDataBean.getDynamicPrePhotos();
            if (originPhotos != null) {
                selectedPhotos = new ArrayList<>(MAX_PHOTOS);
                selectedPhotos.addAll(originPhotos);
            }
        }
        switch (dynamicType) {
            case SendDynamicDataBean.PHOTO_TEXT_DYNAMIC:
                // 没有图片就初始化这些
                initPhotoSelector();
                initPhotoList(bundle);

                break;
            case SendDynamicDataBean.TEXT_ONLY_DYNAMIC:
                mLLToll.setVisibility(View.GONE);
                mRvPhotoList.setVisibility(View.GONE);// 隐藏图片控件
                mEtDynamicContent.getEtContent().setHint(getString(R.string.dynamic_content_no_pic_hint));
                break;
            default:
        }
    }

    /**
     * 图片列表返回后，判断图片列表内容以及顺序是否发生变化，如果没变，就可以不用刷新
     */
    private boolean isPhotoListChanged(List<ImageBean> oldList, List<ImageBean> newList) {
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
                    if (!newImageBean.getImgUrl().equals(oldImageBean.getImgUrl())) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

}
