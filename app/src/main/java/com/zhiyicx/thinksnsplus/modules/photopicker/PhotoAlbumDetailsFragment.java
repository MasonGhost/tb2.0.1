package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.adapter.PhotoGridAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.event.OnPhotoClickListener;
import me.iwf.photopicker.utils.MediaStoreHelper;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.ALL_PHOTOS;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.SELECTED_DIRECTORY_NAME;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.SELECTED_DIRECTORY_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/6
 * @contact email:450127106@qq.com
 */

public class PhotoAlbumDetailsFragment extends TSFragment {
    public static final int COMPLETE_REQUEST_CODE = 1000;
    public static final String EXTRA_BACK_HERE = "back_here";// 回到当前图片列表页面，是否停留
    public final static String EXTRA_ORIGIN = "ORIGINAL_PHOTOS";
    private final static String EXTRA_COLUMN = "column";
    public final static String EXTRA_VIEW_INDEX = "view_index";

    public static final String EXTRA_VIEW_ALL_PHOTOS = "view_photos";
    public static final String EXTRA_VIEW_SELECTED_PHOTOS = "view_selected_photos";

    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";
    private int maxCount = DEFAULT_MAX_COUNT;

    @BindView(R.id.rv_album_details)
    RecyclerView mRvAlbumDetails;
    @BindView(R.id.tv_preview)
    TextView mTvPreview;
    @BindView(R.id.bt_complete)
    TextView mBtComplete;
    private PhotoGridAdapter photoGridAdapter;
    //所有photos的路径
    private List<PhotoDirectory> directories;
    //传入的已选照片
    private ArrayList<String> originalPhotos;
    private RequestManager mGlideRequestManager;
    private int column;// 图片列数
    private int selected_directory;// 获取被选中的目录位置


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_photo_album_details;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected String setCenterTitle() {
        return getArguments().getString(SELECTED_DIRECTORY_NAME);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void setLeftClick() {
        // 回到相册列表页面，同时将当前数据传递过去
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRA_ORIGIN, photoGridAdapter.getSelectedPhotoPaths());
        Intent intent = new Intent();
        intent.setClass(getContext(), PhotoAlbumListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    protected void setRightClick() {
        getActivity().finish();
    }

    @Override
    protected void initView(View rootView) {
        mGlideRequestManager = Glide.with(this);
        directories = getArguments().getParcelableArrayList(ALL_PHOTOS);
        if (directories == null) {
            directories = new ArrayList<>();
        }
        column = getArguments().getInt(EXTRA_COLUMN, DEFAULT_COLUMN_NUMBER);
        originalPhotos = getArguments().getStringArrayList(EXTRA_ORIGIN);
        selected_directory = getArguments().getInt(SELECTED_DIRECTORY_NUMBER, 0);
        maxCount = getArguments().getInt(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);

        photoGridAdapter = new PhotoGridAdapter(getActivity(), mGlideRequestManager, directories, originalPhotos, column);
        photoGridAdapter.setShowCamera(false);
        photoGridAdapter.setPreviewEnable(true);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), column);
        mRvAlbumDetails.setLayoutManager(layoutManager);
        mRvAlbumDetails.setItemAnimator(new DefaultItemAnimator());
        mRvAlbumDetails.setAdapter(photoGridAdapter);
        mRvAlbumDetails.addItemDecoration(new GridDecoration(getContext(), com.zhiyicx.baseproject.R.drawable.shape_recyclerview_divider_white_small));
        photoGridAdapter.setCurrentDirectoryIndex(selected_directory);
        photoGridAdapter.notifyDataSetChanged();
        // 设置图片选择的监听
        photoGridAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean onItemCheck(int position, Photo photo, int selectedItemCount) {
                mBtComplete.setEnabled(selectedItemCount > 0);
                // 设置预览按钮的状态
                mTvPreview.setEnabled(selectedItemCount > 0);
                if (maxCount <= 1) {
                    List<String> photos = photoGridAdapter.getSelectedPhotos();
                    // 当前选择的图片，没有被选择过
                    if (!photos.contains(photo.getPath())) {
                        // 之前已经选择过该图片，就需要-1张
                        if (!photos.isEmpty()) {
                            selectedItemCount -= 1;
                        }
                        photos.clear();
                        photoGridAdapter.notifyDataSetChanged();
                    }
                    // 设置当前选择的数量
                    mBtComplete.setText(getString(R.string.album_selected_count, selectedItemCount, maxCount));
                    return true;
                }
                // 数量超过时，进行提示
                if (selectedItemCount > maxCount) {
                    ToastUtils.showToast(getString(R.string.choose_max_photos, maxCount));
                    return false;
                }
                // 设置当前选择的数量
                mBtComplete.setText(getString(R.string.album_selected_count, selectedItemCount, maxCount));
                return true;
            }
        });
        // 设置图片item的点击事件
        photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View v, int position, boolean showCamera) {
                int index = showCamera ? position - 1 : position;
                List<String> allPhotos = photoGridAdapter.getCurrentPhotoPaths();
                ArrayList<String> selectedPhotos = photoGridAdapter.getSelectedPhotoPaths();
                ArrayList<AnimationRectBean> animationRectBeanArrayList
                        = new ArrayList<AnimationRectBean>();
                for (int i = 0; i < allPhotos.size(); i++) {

                    if (i < layoutManager.findFirstVisibleItemPosition()) {
                        // 顶部，无法看见的图片
                        animationRectBeanArrayList.add(null);
                    } else if (i > layoutManager.findLastVisibleItemPosition()) {
                        // 底部，无法看见的图片
                        animationRectBeanArrayList.add(null);
                    } else {
                        View view = layoutManager
                                .getChildAt(i - layoutManager.findFirstVisibleItemPosition());
                        ImageView imageView = (ImageView) view.findViewById(R.id.iv_photo);
                        // 可以完全看见的图片
                        AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView);
                        animationRectBeanArrayList.add(rect);
                    }
                }
                PhotoViewActivity.startToPhotoView(PhotoAlbumDetailsFragment.this, (ArrayList<String>) allPhotos
                        , selectedPhotos, animationRectBeanArrayList, maxCount, index);
            }
        });
    }


    @Override
    protected void initData() {
        // 页面没有从上级页面获取相册数据，那么自己重新获取一次
        if (directories.isEmpty()) {
            Bundle mediaStoreArgs = new Bundle();
            mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, true);
            MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
                    new MediaStoreHelper.PhotosResultCallback() {
                        @Override
                        public void onResultCallback(List<PhotoDirectory> dirs) {
                            directories.clear();
                            directories.addAll(dirs);
                            photoGridAdapter.notifyDataSetChanged();
                        }
                    });
        }
        int selectedCount = photoGridAdapter.getSelectedPhotoPaths().size();
        // 初始化数据
        mBtComplete.setEnabled(selectedCount > 0);
        mTvPreview.setEnabled(selectedCount > 0);
        mBtComplete.setText(getString(R.string.album_selected_count, selectedCount, maxCount));
    }

    public static PhotoAlbumDetailsFragment initFragment(Bundle bundle) {
        PhotoAlbumDetailsFragment photoAlbumDetailsFragment = new PhotoAlbumDetailsFragment();
        photoAlbumDetailsFragment.setArguments(bundle);
        return photoAlbumDetailsFragment;
    }

    @OnClick({R.id.tv_preview, R.id.bt_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_preview:
                // 预览图片和选择图片是相同的
                ArrayList<String> allPhotos = photoGridAdapter.getSelectedPhotoPaths();
                ArrayList<String> selectedPhoto = photoGridAdapter.getSelectedPhotoPaths();
                ArrayList<AnimationRectBean> animationRectBeanArrayList = new ArrayList<>();
                for (String path : selectedPhoto) {
                    animationRectBeanArrayList.add(null);
                }
                PhotoViewActivity.startToPhotoView(this, allPhotos, selectedPhoto, animationRectBeanArrayList, maxCount, 0);
                break;
            case R.id.bt_complete:
                Intent it = new Intent();
                it.putStringArrayListExtra("photos", photoGridAdapter.getSelectedPhotoPaths());
                getActivity().setResult(Activity.RESULT_OK, it);
                getActivity().finish();
                break;
            default:
        }
    }

    // @Subscriber(tag = EventBusTagConfig.EVENT_SELECTED_PHOTO_UPDATE)
    public void refreshDataAndUI(List<String> selectedPhoto) {
        int selectedCount = selectedPhoto.size();
        List<String> oldSelectedPhotos = photoGridAdapter.getSelectedPhotos();
        oldSelectedPhotos.clear();
        oldSelectedPhotos.addAll(selectedPhoto);
        photoGridAdapter.notifyDataSetChanged();
        mBtComplete.setEnabled(selectedCount > 0);
        // 设置预览按钮的状态
        mTvPreview.setEnabled(selectedCount > 0);
        mBtComplete.setText(getString(R.string.album_selected_count, selectedCount, maxCount));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMPLETE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            boolean stayHere = data.getBooleanExtra(EXTRA_BACK_HERE, false);
            if (stayHere) {
                // 如果停在该页面，刷新图片列表选择状态
                List<String> selectedPhoto = data.getStringArrayListExtra("photos");
                refreshDataAndUI(selectedPhoto);
            } else {
                // 否则，直接将结果返回到该去的地方
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
        }
    }
}
