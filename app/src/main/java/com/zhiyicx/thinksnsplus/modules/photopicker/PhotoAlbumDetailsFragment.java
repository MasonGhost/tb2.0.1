package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.adapter.PhotoGridAdapter;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.utils.MediaStoreHelper;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.ALL_PHOTOS;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.SELECTED_DIRECTORY_NAME;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.SELECTED_DIRECTORY_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/6
 * @contact email:450127106@qq.com
 */

public class PhotoAlbumDetailsFragment extends TSFragment {
    private final static String EXTRA_ORIGIN = "origin";
    private final static String EXTRA_COLUMN = "column";
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
    protected void initView(View rootView) {
        mGlideRequestManager = Glide.with(this);
        directories = getArguments().getParcelableArrayList(ALL_PHOTOS);
        if (directories == null) {
            directories = new ArrayList<>();
        }
        column = getArguments().getInt(EXTRA_COLUMN, DEFAULT_COLUMN_NUMBER);
        originalPhotos = getArguments().getStringArrayList(EXTRA_ORIGIN);
        selected_directory = getArguments().getInt(SELECTED_DIRECTORY_NUMBER, 0);

        photoGridAdapter = new PhotoGridAdapter(getActivity(), mGlideRequestManager, directories, originalPhotos, column);
        photoGridAdapter.setShowCamera(false);
        photoGridAdapter.setPreviewEnable(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRvAlbumDetails.setLayoutManager(layoutManager);
        mRvAlbumDetails.setAdapter(photoGridAdapter);
        photoGridAdapter.setCurrentDirectoryIndex(selected_directory);
        photoGridAdapter.notifyDataSetChanged();
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
                break;
            case R.id.bt_complete:
                break;
        }
    }

    /**
     * 设置完成按钮的状态
     */
    private void setCompleteButtonState() {

        //mBtComplete.setEnabled(enable);
    }
}
