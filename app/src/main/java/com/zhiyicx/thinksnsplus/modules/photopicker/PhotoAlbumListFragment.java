package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.adapter.PopupDirectoryListAdapter;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.utils.MediaStoreHelper;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment.EXTRA_ORIGIN;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/6
 * @contact email:450127106@qq.com
 */

public class PhotoAlbumListFragment extends TSFragment {
    // 相册列表被选中的位置
    public static final String SELECTED_DIRECTORY_NUMBER = "selected_directory_number";
    // 相册列表被选中的名称
    public static final String SELECTED_DIRECTORY_NAME = "selected_directory_name";
    // 相册列表的内容
    public static final String ALL_PHOTOS = "all_photos";
    @BindView(R.id.rv_photo_album_list)
    RecyclerView mRvPhotoAlbumList;
    private CommonAdapter<PhotoDirectory> listAdapter;
    // 所有 photos 的路径
    private ArrayList<PhotoDirectory> directories;
    private final static String EXTRA_GIF = "gif";

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_photo_album_list;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.album_list);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected void setRightClick() {
        getActivity().finish();
    }

    @Override
    protected void initView(View rootView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvPhotoAlbumList.setLayoutManager(linearLayoutManager);
        directories = new ArrayList<>();
        listAdapter = new CommonAdapter<PhotoDirectory>(getContext(), me.iwf.photopicker.R.layout.__picker_item_directory, directories) {
            @Override
            protected void convert(ViewHolder holder, final PhotoDirectory photoDirectory, final int position) {
                ImageView ivCover = holder.getView(me.iwf.photopicker.R.id.iv_dir_cover);
                TextView tvName = holder.getView(me.iwf.photopicker.R.id.tv_dir_name);
                TextView tvCount = holder.getView(me.iwf.photopicker.R.id.tv_dir_count);
                ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
                imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                        .url(photoDirectory.getCoverPath())
                        .imagerView(ivCover)
                        .build());
                tvName.setText(photoDirectory.getName());
                tvCount.setText(tvCount.getContext().getString(me.iwf.photopicker.R.string.__picker_image_count, photoDirectory.getPhotos().size()));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(SELECTED_DIRECTORY_NUMBER, position);
                        bundle.putParcelableArrayList(ALL_PHOTOS, directories);
                        bundle.putString(SELECTED_DIRECTORY_NAME, photoDirectory.getName());
                        bundle.putStringArrayList(EXTRA_ORIGIN,getArguments().getStringArrayList(EXTRA_ORIGIN));
                        Intent intent = new Intent(getActivity(), PhotoAlbumDetailsActivity.class);
                        intent.putExtras(bundle);
                        PhotoAlbumListFragment.this.startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        };

        mRvPhotoAlbumList.setAdapter(listAdapter);
    }

    @Override
    protected void initData() {
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, true);
        MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        listAdapter.notifyDataSetChanged();
                        //adjustHeight();
                    }
                });
    }

    public static PhotoAlbumListFragment initFragment(Bundle bundle) {
        PhotoAlbumListFragment photoAlbumListFragment = new PhotoAlbumListFragment();
        photoAlbumListFragment.setArguments(bundle);
        return photoAlbumListFragment;
    }

}
