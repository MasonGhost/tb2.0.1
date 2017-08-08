package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.edittext.InfoInputEditText;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoTagsAdapter;
import com.zhiyicx.thinksnsplus.modules.information.publish.uploadcover.UploadCoverActivity;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 完善文章信息界面
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class AddInfoFragment extends TSFragment<AddInfoContract.Presenter> implements AddInfoContract.View {

    public static final String BUNDLE_PUBLISH_BEAN = "publish_bean";
    private static final int REQUST_CODE_CATEGORY = 5000;

    @BindView(R.id.bt_add_category)
    CombinationButton mBtAddCategory;
    @BindView(R.id.fl_tags)
    TagFlowLayout mFlTags;
    @BindView(R.id.ll_tag_container)
    LinearLayout mLlTagContainer;
    @BindView(R.id.tv_from)
    InfoInputEditText mTvFrom;
    @BindView(R.id.tv_author)
    InfoInputEditText mTvAuthor;
    @BindView(R.id.v_horizontal_line)
    View mVHorizontalLine;
    @BindView(R.id.et_info_summary)
    UserInfoInroduceInputView mEtInfoSummary;

    private UserInfoTagsAdapter mUserInfoTagsAdapter;
    private List<UserTagBean> mUserTagBeens = new ArrayList<>();
    private InfoPublishBean mInfoPublishBean;

    public static AddInfoFragment newInstance(Bundle bundle) {

        AddInfoFragment fragment = new AddInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mInfoPublishBean = getArguments().getParcelable(BUNDLE_PUBLISH_BEAN);
        }
        if (mInfoPublishBean == null) {
            mInfoPublishBean = new InfoPublishBean();
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_publish_add_info;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.info_title);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.next);
    }

    @Override
    protected void setRightClick() {
        Intent intent = new Intent(getActivity(), UploadCoverActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_PUBLISH_BEAN, mInfoPublishBean);
        intent.putExtras(bundle);
        startActivity(intent);
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
        mToolbarRight.setEnabled(false);
        initListener();
        if (mInfoPublishBean.getTags() != null) {
            mUserTagBeens.addAll(mInfoPublishBean.getTags());
        }
        mUserInfoTagsAdapter = new UserInfoTagsAdapter(mUserTagBeens, getContext());
        mFlTags.setAdapter(mUserInfoTagsAdapter);
        mFlTags.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                jumpToEditUserTag();
            }
            return true;
        });

    }

    private void jumpToEditUserTag() {
        EditUserTagFragment.startToEditTagActivity(getActivity(), TagFrom.INFO_PUBLISH, (ArrayList<UserTagBean>) mUserTagBeens);
    }

    @Override
    protected void initData() {
    }


    private void initListener() {
        // 栏目
        RxView.clicks(mBtAddCategory)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    Intent intent = new Intent(getActivity(), AddInfoCategoryActivity.class);
                    startActivityForResult(intent, REQUST_CODE_CATEGORY);
                });
        // 标签
        RxView.clicks(mLlTagContainer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    jumpToEditUserTag();
                });
        // 来源
        RxTextView.afterTextChangeEvents(mTvFrom.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSeques -> {
                    mInfoPublishBean.setFrom(charSeques.editable().toString().trim());
                });
        // 作者
        RxTextView.afterTextChangeEvents(mTvAuthor.getEditInput())
                .compose(this.bindToLifecycle())
                .subscribe(charSeques -> {
                    mInfoPublishBean.setAuthor(charSeques.editable().toString().trim());
                });
        // 摘要
        RxTextView.afterTextChangeEvents(mEtInfoSummary.getEtContent())
                .compose(this.bindToLifecycle())
                .subscribe(charSeques -> {
                    mInfoPublishBean.setSubject("> "+charSeques.editable().toString().trim());
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {

            if (requestCode == TagFrom.INFO_PUBLISH.id) {
                ArrayList<UserTagBean> choosedTags = data.getExtras().getParcelableArrayList(EditUserTagFragment.BUNDLE_CHOOSED_TAGS);
                mUserTagBeens.clear();
                mUserTagBeens.addAll(choosedTags);
                mUserInfoTagsAdapter.notifyDataChanged();
                mInfoPublishBean.setTags(mUserTagBeens);

            } else if (requestCode == REQUST_CODE_CATEGORY) {
                InfoTypeCatesBean category = data.getExtras().getParcelable(AddInfoCategoryFragment.BUNDLE_PUBLISH_CATEGORY);
                if (category == null) {
                    return;
                }
                mInfoPublishBean.setCategoryId(category.getId());
                mBtAddCategory.setRightText(category.getName());

            }
            checkNextButton();
        }

    }

    private void checkNextButton() {
        if (mInfoPublishBean.getCategoryId() != 0) {
            mToolbarRight.setEnabled(true);
        } else {
            mToolbarRight.setEnabled(false);
        }


    }
}
