package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.edittext.InfoInputEditText;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMoreCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMyCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoTagsAdapter;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 完善文章信息界面
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class AddInfoCategoryFragment extends TSFragment<AddInfoContract.Presenter> implements AddInfoContract.View {

    public static final String BUNDLE_PUBLISH_CATEGORY = "publish_category";

    @BindView(R.id.fragment_channel_content_unsubscribe)
    RecyclerView mFragmentChannelContentUnsubscribe;

    private List<InfoTypeMoreCatesBean> mMoreCatesBeen;
    private CommonAdapter mUnSubscribeAdapter;

    public static AddInfoCategoryFragment newInstance(Bundle bundle) {

        AddInfoCategoryFragment fragment = new AddInfoCategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_publish_add_category;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.info_choose_categorys);
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {

        mFragmentChannelContentUnsubscribe.setLayoutManager(new GridLayoutManager(getActivity(),
                4));
    }

    private CommonAdapter initUnsubscribeAdapter() {
        mUnSubscribeAdapter = new CommonAdapter<InfoTypeMoreCatesBean>(getActivity(),
                R.layout.item_info_channel, mMoreCatesBeen) {
            @Override
            protected void convert(ViewHolder holder, InfoTypeMoreCatesBean data,
                                   int position) {
                holder.setText(R.id.item_info_channel, data.getName());
            }

            @Override
            protected void setListener(ViewGroup parent, final ViewHolder viewHolder, int viewType) {
                RxView.clicks(viewHolder.itemView)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .compose(bindToLifecycle())
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                if (mOnItemClickListener != null) {
                                    int position = viewHolder.getAdapterPosition();
                                    mOnItemClickListener.onItemClick(viewHolder.itemView, viewHolder, position);
                                }
                            }
                        });

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mOnItemClickListener != null) {
                            int position = viewHolder.getAdapterPosition();
                            return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                        }
                        return true;
                    }
                });
            }
        };

        mUnSubscribeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                InfoTypeMoreCatesBean bean = mMoreCatesBeen.get(position);
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putParcelable(BUNDLE_PUBLISH_CATEGORY,bean);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK,intent);
                getActivity().finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });

        return mUnSubscribeAdapter;
    }


    @Override
    protected void initData() {
        mMoreCatesBeen = mPresenter.getInfoTypeBean();
        mFragmentChannelContentUnsubscribe.setAdapter(initUnsubscribeAdapter());
    }

}
