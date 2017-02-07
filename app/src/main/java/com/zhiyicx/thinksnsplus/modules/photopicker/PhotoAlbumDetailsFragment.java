package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;

import org.simple.eventbus.Subscriber;

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

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.ALL_PHOTOS;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.SELECTED_DIRECTORY_NAME;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.SELECTED_DIRECTORY_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;
import static me.iwf.photopicker.PhotoPicker.KEY_SELECTED_PHOTOS;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/6
 * @contact email:450127106@qq.com
 */

public class PhotoAlbumDetailsFragment extends TSFragment {
    private final static String EXTRA_ORIGIN = "origin";
    private final static String EXTRA_COLUMN = "column";
    public final static String EXTRA_VIEW_INDEX = "view_index";
    public static final String EXTRA_VIEW_WIDTH = "view_width";
    public static final String EXTRA_VIEW_HEIGHT = "view_height";
    public static final String EXTRA_VIEW_LOCATION = "view_location";
    public static final String EXTRA_VIEW_ALL_PHOTOS = "view_photos";
    public static final String EXTRA_VIEW_SELECTED_PHOTOS = "view_selected_photos";
    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";
    private int maxCount = DEFAULT_MAX_COUNT;

    @BindView(R.id.rv_album_details)
    RecyclerView mRvAlbumDetails;
    @BindView(R.id.tv_preview)
    TextView mTvPreview;
    @BindView(R.id.bt_complete)
    Button mBtComplete;
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
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRvAlbumDetails.setLayoutManager(layoutManager);
        mRvAlbumDetails.setAdapter(photoGridAdapter);
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
                    // 已经选择过的图片，取消选择
                    if (!photos.contains(photo.getPath())) {
                        photos.clear();
                        photoGridAdapter.notifyDataSetChanged();
                    }
                    return true;
                }
                // 数量超过时，进行提示
                if (selectedItemCount > maxCount) {
                    Toast.makeText(getActivity(), getString(me.iwf.photopicker.R.string.__picker_over_max_count_tips, maxCount),
                            LENGTH_LONG).show();
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
                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_VIEW_INDEX, index);
                bundle.putInt(EXTRA_VIEW_WIDTH, v.getWidth());
                bundle.putInt(EXTRA_VIEW_HEIGHT, v.getHeight());
                bundle.putIntArray(EXTRA_VIEW_LOCATION, screenLocation);
                bundle.putStringArrayList(EXTRA_VIEW_ALL_PHOTOS, (ArrayList<String>) allPhotos);
                bundle.putStringArrayList(EXTRA_VIEW_SELECTED_PHOTOS, selectedPhotos);
                bundle.putInt(EXTRA_MAX_COUNT, maxCount);
                Intent intent = new Intent(getContext(), PhotoViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

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
                            //adjustHeight();
                        }
                    });
        }
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
                //int index = showCamera ? position - 1 : position;
                ArrayList<String> allPhotos = photoGridAdapter.getSelectedPhotoPaths();
                ArrayList<String> selectedPhoto = photoGridAdapter.getSelectedPhotoPaths();
                int[] screenLocation = new int[2];
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_VIEW_INDEX, 0);
                bundle.putInt(EXTRA_VIEW_WIDTH, 0);
                bundle.putInt(EXTRA_VIEW_HEIGHT, 0);
                bundle.putIntArray(EXTRA_VIEW_LOCATION, screenLocation);
                bundle.putStringArrayList(EXTRA_VIEW_ALL_PHOTOS, allPhotos);
                bundle.putStringArrayList(EXTRA_VIEW_SELECTED_PHOTOS, selectedPhoto);
                bundle.putInt(EXTRA_MAX_COUNT, maxCount);
                Intent intent1 = new Intent(getContext(), PhotoViewActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.bt_complete:
                Intent intent = new Intent();
                ArrayList<String> selectedPhotos = photoGridAdapter.getSelectedPhotoPaths();
                intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
        }
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_SELECTED_PHOTO_UPDATE)
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

}
