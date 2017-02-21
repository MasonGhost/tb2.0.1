package com.zhiyicx.thinksnsplus.modules.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment;
import com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewActivity;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/20
 * @contact email:450127106@qq.com
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

    private List<ImageBean> selectedPhotos;
    private CommonAdapter<ImageBean> mCommonAdapter;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private PhotoSelectorImpl mPhotoSelector;
    private boolean hasTitle, hasContent, hasPics;


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
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        initSendDynamicBtnState();// 设置右边发布文字监听
        setLeftTextColor();// 设置左边取消文字的颜色为主题色
        initPhotoSelector();// 初始化图片选择器
        selectedPhotos = new ArrayList<>(MAX_PHOTOS);
        // 占位缺省图
        ImageBean camera = new ImageBean();
        selectedPhotos.add(camera);
        mCommonAdapter = new CommonAdapter<ImageBean>(getContext(), R.layout.item_send_dynamic_photo_list, selectedPhotos) {
            @Override
            protected void convert(ViewHolder holder, ImageBean imageBean, final int position) {
                // 固定每个item的宽高
                // 屏幕宽高减去左右margin以及item之间的空隙
                int width = UIUtils.getWindowWidth(getContext()) - getResources().getDimensionPixelSize(R.dimen.spacing_large) * 2
                        - ConvertUtils.dp2px(getContext(), 5) * (ITEM_COLUM - 1);
                View convertView = holder.getConvertView();
                convertView.getLayoutParams().width = width / ITEM_COLUM;
                convertView.getLayoutParams().height = width / ITEM_COLUM;
                ImageView imageView = holder.getView(R.id.iv_dynamic_img);
                if (position == selectedPhotos.size() - 1) {
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
                        if (position == selectedPhotos.size() - 1) {
                            if (selectedPhotos.size() >= MAX_PHOTOS) {
                                ToastUtils.showToast(getString(R.string.choose_max_photos, MAX_PHOTOS));
                                return;
                            }
                            initPhotoPopupWindow();
                            mPhotoPopupWindow.show();
                        } else {
                            // 预览图片
                            ArrayList<String> photos = new ArrayList<String>();
                            // 最后一张是占位图
                            for (int i = 0; i < selectedPhotos.size() - 1; i++) {
                                ImageBean imageBean = selectedPhotos.get(i);
                                photos.add(imageBean.getImgUrl());
                            }
                            int[] screenLocation = new int[2];
                            v.getLocationOnScreen(screenLocation);
                            Bundle bundle = new Bundle();
                            bundle.putInt(PhotoAlbumDetailsFragment.EXTRA_VIEW_INDEX, position);
                            bundle.putInt(PhotoAlbumDetailsFragment.EXTRA_VIEW_WIDTH, v.getWidth());
                            bundle.putInt(PhotoAlbumDetailsFragment.EXTRA_VIEW_HEIGHT, v.getHeight());
                            bundle.putIntArray(PhotoAlbumDetailsFragment.EXTRA_VIEW_LOCATION, screenLocation);
                            bundle.putStringArrayList(PhotoAlbumDetailsFragment.EXTRA_VIEW_ALL_PHOTOS, photos);
                            bundle.putStringArrayList(PhotoAlbumDetailsFragment.EXTRA_VIEW_SELECTED_PHOTOS, photos);
                            bundle.putInt(PhotoAlbumDetailsFragment.EXTRA_MAX_COUNT, MAX_PHOTOS);
                            Intent intent = new Intent(getContext(), PhotoViewActivity.class);
                            ActivityOptionsCompat options =
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                            v, "trans_photo");
                            intent.putExtras(bundle);
                            ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                        }
                    }
                });
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), ITEM_COLUM);
        mRvPhotoList.setLayoutManager(gridLayoutManager);
        // 设置recyclerview的item之间的空白
        int witdh = ConvertUtils.dp2px(getContext(), 5);
        mRvPhotoList.addItemDecoration(new GridDecoration(witdh, witdh));
        mRvPhotoList.setAdapter(mCommonAdapter);
    }

    @Override
    protected void initData() {

    }

    public static SendDynamicFragment initFragment(Bundle bundle) {
        SendDynamicFragment sendDynamicFragment = new SendDynamicFragment();
        sendDynamicFragment.setArguments(bundle);
        return sendDynamicFragment;
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
                        ArrayList<String> photos = new ArrayList<String>();
                        // 最后一张是占位图
                        for (int i = 0; i < selectedPhotos.size() - 1; i++) {
                            ImageBean imageBean = selectedPhotos.get(i);
                            photos.add(imageBean.getImgUrl());
                        }
                        mPhotoSelector.getPhotoListFromSelector(9, photos);
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
     * 初始化图片选择器
     */
    private void initPhotoSelector() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .SHAPE_RCTANGLE))
                .build().photoSelectorImpl();
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        selectedPhotos.clear();
        selectedPhotos.addAll(photoList);
        // 占位缺省图
        ImageBean camera = new ImageBean();
        selectedPhotos.add(camera);
        setSendDynamicState();// 每次刷新图片后都要判断发布按钮状态
        mCommonAdapter.notifyDataSetChanged();
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
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

    private void setLeftTextColor() {
        mToolbarLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
    }

    /**
     * 发布按钮的状态监听
     */
    private void initSendDynamicBtnState() {
        mEtDynamicTitle.setContentChangedListener(new UserInfoInroduceInputView.ContentChangedListener() {
            @Override
            public void contentChanged(CharSequence s) {
                hasTitle = !TextUtils.isEmpty(s);
                setSendDynamicState();
            }
        });
        mEtDynamicContent.setContentChangedListener(new UserInfoInroduceInputView.ContentChangedListener() {
            @Override
            public void contentChanged(CharSequence s) {
                hasContent = !TextUtils.isEmpty(s);
                setSendDynamicState();
            }
        });

    }

    /**
     * 设置动态发布按钮的点击状态
     */
    private void setSendDynamicState() {
        if (!hasTitle && !hasContent && !selectedPhotos.isEmpty()) {
            mToolbarRight.setEnabled(false);
        } else {
            mToolbarRight.setEnabled(true);
        }
    }
}
