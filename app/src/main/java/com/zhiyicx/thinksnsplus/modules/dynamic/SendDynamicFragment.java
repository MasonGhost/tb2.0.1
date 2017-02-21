package com.zhiyicx.thinksnsplus.modules.dynamic;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.thinksnsplus.R;
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

public class SendDynamicFragment extends TSFragment<SendDynamicContract.Presenter> {
    @BindView(R.id.et_dynamic_title)
    EditText mEtDynamicTitle;
    @BindView(R.id.et_dynamic_content)
    EditText mEtDynamicContent;
    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    private List<ImageBean> selectedPhotos;
    private CommonAdapter<ImageBean> mCommonAdapter;

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
        selectedPhotos = new ArrayList<>(9);
        ImageBean camera = new ImageBean();
        selectedPhotos.add(camera);
        mCommonAdapter = new CommonAdapter<ImageBean>(getContext(), R.layout.item_send_dynamic_photo_list, selectedPhotos) {
            @Override
            protected void convert(ViewHolder holder, ImageBean imageBean, int position) {

            }
        };
    }

    @Override
    protected void initData() {

    }

    public static SendDynamicFragment initFragment(Bundle bundle) {
        SendDynamicFragment sendDynamicFragment = new SendDynamicFragment();
        sendDynamicFragment.setArguments(bundle);
        return sendDynamicFragment;
    }

}
