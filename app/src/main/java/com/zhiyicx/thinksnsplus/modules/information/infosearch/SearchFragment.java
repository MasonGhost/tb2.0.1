package com.zhiyicx.thinksnsplus.modules.information.infosearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.info.InfoListDataBean;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment.BUNDLE_INFO_TYPE;

/**
 * @Author Jliuer
 * @Date 2017/03/06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SearchFragment extends TSListFragment<SearchContract.Presenter, InfoListDataBean>
        implements SearchContract.View {

    @BindView(R.id.fragment_info_search_back)
    ImageView mFragmentInfoSearchBack;
    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;
    @BindView(R.id.fragment_info_search_cancle)
    TextView mFragmentInfoSearchCancle;
    private List<InfoListDataBean> mData = new ArrayList<>();
    private ImageLoader mImageLoader;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_search;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mEmptyView.setVisibility(View.GONE);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mFragmentInfoSearchEdittext.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            requestNetData(0L, false);
                            return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @OnClick({R.id.fragment_info_search_back, R.id.fragment_info_search_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_info_search_back:
                getActivity().finish();
                break;
            case R.id.fragment_info_search_cancle:
                getActivity().finish();
                break;
        }
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        return new CommonAdapter<InfoListDataBean>(getActivity(),
                R.layout.item_info, mListDatas) {
            @Override
            protected void convert(ViewHolder holder,final InfoListDataBean realData,
                                   final int position) {
                final TextView title = holder.getView(R.id.item_info_title);
                ImageView imageView = holder.getView(R.id.item_info_imag);
                if (AppApplication.sOverRead.contains(position + "")) {
                    title.setTextColor(getResources()
                            .getColor(R.color.normal_for_assist_text));
                }
                title.setText(realData.getTitle());
                String url = String.format(ApiConfig.IMAGE_PATH, realData.getStorage().getId(), 50);
                mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                        .url(url)
                        .imagerView(imageView)
                        .build());
                holder.setText(R.id.item_info_timeform, TimeUtils.getTimeFriendlyNormal(realData
                        .getUpdated_at()));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!AppApplication.sOverRead.contains(position + "")) {
                            AppApplication.sOverRead.add(position + "");
                        }
                        title.setTextColor(getResources()
                                .getColor(R.color.normal_for_assist_text));
                        Intent intent = new Intent(getActivity(), InfoDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(BUNDLE_INFO, realData);
                        intent.putExtra(BUNDLE_INFO, bundle);
                        startActivity(intent);
                    }
                });
            }
        };
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String getKeyWords() {
        return mFragmentInfoSearchEdittext.getText().toString();
    }

    @Override
    protected Long getMaxId(@NotNull List<InfoListDataBean> data) {
        return (long) data.get(data.size() - 1).getId();
    }

    @Override
    public void onCacheResponseSuccess(@NotNull List<InfoListDataBean> data, boolean
            isLoadMore) {
    }
}
