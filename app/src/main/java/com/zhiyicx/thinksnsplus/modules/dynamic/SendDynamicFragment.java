package com.zhiyicx.thinksnsplus.modules.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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

public class SendDynamicFragment extends TSFragment<SendDynamicContract.Presenter> implements PhotoSelectorImpl.IPhotoBackListener {
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
        initPhotoSelector();
        selectedPhotos = new ArrayList<>(9);
        // 占位缺省图
        ImageBean camera = new ImageBean();
        selectedPhotos.add(camera);
        mCommonAdapter = new CommonAdapter<ImageBean>(getContext(), R.layout.item_send_dynamic_photo_list, selectedPhotos) {
            @Override
            protected void convert(ViewHolder holder, ImageBean imageBean, final int position) {
                ImageView imageView = holder.getView(R.id.iv_dynamic_img);
                if (position == selectedPhotos.size() - 1) {
                    // 最后一项作为占位图
                    imageView.setImageResource(R.mipmap.img_edit_photo_frame);
                } else {
                    /*ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
                    imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                            .imagerView(imageView)
                            .url(imageBean.getImgUrl())
                            .build());*/
                    int width = UIUtils.getWindowWidth(getContext()) - getResources().getDimensionPixelSize(R.dimen.spacing_large) * 2 - ConvertUtils.dp2px(getContext(), 5) * 3;
                    Glide.with(getContext())
                            .load(imageBean.getImgUrl())
                            .override(width / 4, width / 4)
                            .into(imageView);

                }
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position == selectedPhotos.size() - 1) {
                            initPhotoPopupWindow();
                            mPhotoPopupWindow.show();
                        }
                    }
                });

            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        mRvPhotoList.setLayoutManager(gridLayoutManager);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
