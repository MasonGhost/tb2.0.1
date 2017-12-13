package com.zhiyicx.thinksnsplus.modules.usertag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.baseproject.widget.recycleview.stickygridheaders.StickyHeaderGridLayoutManager;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @uthor Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class EditUserTagFragment extends TSFragment<EditUserTagContract.Presenter> implements EditUserTagContract.View, TagClassAdapter
        .OnItemClickListener {
    public static final String BUNDLE_IS_FROM = "is_from";
    public static final String BUNDLE_CHOOSED_TAGS = "choosed_tags";


    private static final int SPAN_SIZE = 3;

    @BindView(R.id.tv_choosed_tag_tip)
    TextView mTvChoosedTagTip;
    @BindView(R.id.rv_choosed_tag)
    RecyclerView mRvChoosedTag;
    @BindView(R.id.rv_tag_class)
    RecyclerView mRvTagClass;

    private GridLayoutManager mChoosedTagLayoutManager;
    private StickyHeaderGridLayoutManager mTagClassLayoutManager;


    private ArrayList<UserTagBean> mChoosedTags = new ArrayList<>();
    private List<TagCategoryBean> mCategoryTags = new ArrayList<>();

    private int mMaxChooseNums;
    private int mCurrentChooseNums = 0;
    private TagClassAdapter mTagClassAdapter;
    private CommonAdapter<UserTagBean> mChoosedTagAdapter;

    private TagFrom mFrom = TagFrom.REGISTER;

    private CenterInfoPopWindow mRulePop;// 标签提示规则选择弹框

    /**
     * 标签选择页
     */
    public static void startToEditTagActivity(Context context, TagFrom from, ArrayList<UserTagBean> choosedTags) {

        Intent intent = new Intent(context, EditUserTagActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_IS_FROM, from);
        bundle.putParcelableArrayList(BUNDLE_CHOOSED_TAGS, choosedTags);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, from.id);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }
    }


    public static EditUserTagFragment newInstance(Bundle bundle) {
        EditUserTagFragment editUserTagFragment = new EditUserTagFragment();
        editUserTagFragment.setArguments(bundle);
        return editUserTagFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_edit_user_tag;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.user_tag_choose_tag);
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
    protected void setRightClick() {

        switch (mFrom) {

            case REGISTER:
                // 注册就进入主页，设置就返回
                startActivity(new Intent(getActivity(), HomeActivity.class));
                break;
            case USER_EDIT:
                setresult();
                break;
            case INFO_PUBLISH:

                break;
            case CREATE_CIRCLE:

                break;
            default:
        }

    }

    @Override
    public TagFrom getCurrentFrom() {
        return mFrom;
    }

    @Override
    protected void setLeftClick() {
        setresult();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFrom = (TagFrom) getArguments().getSerializable(BUNDLE_IS_FROM);
            if (getArguments().getParcelableArrayList(BUNDLE_CHOOSED_TAGS) != null) {
                mChoosedTags.addAll(getArguments().getParcelableArrayList(BUNDLE_CHOOSED_TAGS));
            }

        }
    }

    @Override
    protected void initView(View rootView) {
        mMaxChooseNums = getResources().getInteger(R.integer.user_tag_max_nums);
        switch (mFrom) {

            case REGISTER:
                mToolbarLeft.setVisibility(View.INVISIBLE);
                mToolbarRight.setTextColor(SkinUtils.getColor(R.color.themeColor));
                mToolbarRight.setVisibility(View.VISIBLE);
                break;
            case USER_EDIT:
                mToolbarRight.setVisibility(View.INVISIBLE);
                break;
            case INFO_PUBLISH:
                mToolbarRight.setVisibility(View.INVISIBLE);
                break;
            case CREATE_CIRCLE:
                mToolbarRight.setVisibility(View.INVISIBLE);
                break;
            default:
        }
        updateChooseTip();
        initRvChoosedTag();
        initRvTagClass();
        initListener();


    }

    private void updateChooseTip() {
        mTvChoosedTagTip.setText(getString(R.string.user_tag_choosed_tag_format, mMaxChooseNums, mCurrentChooseNums));
    }

    @Override
    public ArrayList<UserTagBean> getChoosedTags() {
        return mChoosedTags;
    }

    @Override
    protected void initData() {
        mPresenter.getAllTags();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (mFrom) {

            case REGISTER:
                getView().post(() -> initTipPop());
                break;
            case USER_EDIT:
                break;
            case INFO_PUBLISH:
                break;
            case CREATE_CIRCLE:
                break;
            default:
        }

    }

    private void initTipPop() {
        if (mRulePop != null) {
            mRulePop.show();
            return;
        }
        mRulePop = CenterInfoPopWindow.builder()
                .titleStr(getString(R.string.tips))
                .desStr(getString(R.string.tags_tips))
                .item1Str(getString(R.string.get_it))
                .item1Color(R.color.themeColor)
                .isOutsideTouch(true)
                .isFocus(true)
                .animationStyle(R.style.style_actionPopupAnimation)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .buildCenterPopWindowItem1ClickListener(() -> mRulePop.hide())
                .parentView(getView())
                .build();
        mRulePop.show();
    }


    private void initRvChoosedTag() {
        mChoosedTagLayoutManager = new GridLayoutManager(getContext(), SPAN_SIZE);
        mRvChoosedTag.setLayoutManager(mChoosedTagLayoutManager);
        mRvChoosedTag.setHasFixedSize(false);
        initSubscribeAdapter();
        mRvChoosedTag.setAdapter(mChoosedTagAdapter);
    }

    private CommonAdapter initSubscribeAdapter() {
        mChoosedTagAdapter = new CommonAdapter<UserTagBean>(getActivity(), R.layout
                .item_user_tag, mChoosedTags) {
            @Override
            protected void convert(ViewHolder holder, UserTagBean data
                    , final int position) {
                holder.setText(R.id.item_info_channel, data.getTagName());
                RxView.clicks(holder.getView(R.id.fl_container))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> {
                            switch (mFrom) {
                                case INFO_PUBLISH:
                                    deleteTagSuccess(position);
                                    break;
                                case CREATE_CIRCLE:
                                    deleteTagSuccess(position);
                                    break;
                                case REGISTER:
                                case USER_EDIT:
                                    mPresenter.deleteTag(mChoosedTags.get(position).getId(), position);
                                    break;
                                default:
                            }
                        });
            }
        };
//        mChoosedTagAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//            @Override
//            public void onCommentTextClick(View view, RecyclerView.ViewHolder holder, int position) {
//                if (position < 0) {
//                    return;
//                }
//
//
//
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//
//        });

        return mChoosedTagAdapter;
    }

    private void initRvTagClass() {
        mTagClassLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
        mTagClassLayoutManager.setHeaderBottomOverlapMargin(getResources().getDimensionPixelSize(R.dimen.spacing_small));
        mTagClassLayoutManager.setSpanSizeLookup(new StickyHeaderGridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int section, int position) {
                return 1;
            }
        });

        // Workaround RecyclerView limitation when playing remove animations. RecyclerView always
        // puts the removed item on the top of other views and it will be drawn above sticky header.
        // The only way to fix this, abandon remove animations :(
        mRvTagClass.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        mRvTagClass.setLayoutManager(mTagClassLayoutManager);
        mTagClassAdapter = new TagClassAdapter(mCategoryTags);
        mTagClassAdapter.setOnItemClickListener(this);
        mRvTagClass.setAdapter(mTagClassAdapter);

    }

    private void initListener() {


    }

    /**
     * check save button
     *
     * @return
     */
    private void updateChooseAboutView() {
        mCurrentChooseNums = mChoosedTags.size();
        updateChooseTip();
        if (mCurrentChooseNums > 0) {
            mToolbarRight.setTextColor(SkinUtils.getColor(R.color.themeColor));
            mToolbarRight.setText(getString(R.string.next));
        } else {
            mToolbarRight.setTextColor(SkinUtils.getColor(R.color.themeColor));
            mToolbarRight.setText(R.string.jump_over);
        }
    }

    /**
     * 更新所有标签
     *
     * @param tagCategoryBeanList
     */
    @Override
    public void updateTagsFromNet(List<TagCategoryBean> tagCategoryBeanList) {
        if (tagCategoryBeanList == null) {
            return;
        }
        this.mCategoryTags.clear();
        this.mCategoryTags.addAll(tagCategoryBeanList);
        this.mTagClassAdapter.notifyAllSectionsDataSetChanged();
    }

    @Override
    public void updateMineTagsFromNet(List<UserTagBean> tags) {
        if (tags == null) {
            return;
        }
        switch (mFrom) {
            case INFO_PUBLISH:
                break;
            case CREATE_CIRCLE:
                break;
            case REGISTER:
            case USER_EDIT:
                mChoosedTags.clear();
                mChoosedTags.addAll(tags);
                mChoosedTagAdapter.notifyDataSetChanged();
                updateChooseAboutView();
                break;
            default:
        }


    }

    @Override
    public void onItemClick(int categoryPosition, int tagPosition) {
        if (mCategoryTags.get(categoryPosition).getTags().get(tagPosition).isMine_has()) {
            return;
        }
        if (mChoosedTags.size() >= mMaxChooseNums) {
            showSnackErrorMessage(getString(R.string.user_tag_choosed_tag_format_tip, mMaxChooseNums));
            return;
        }
        switch (mFrom) {
            case INFO_PUBLISH:
                addTagSuccess(categoryPosition, tagPosition);
                break;
            case CREATE_CIRCLE:
                addTagSuccess(categoryPosition, tagPosition);
                break;
            case REGISTER:
            case USER_EDIT:
                mPresenter.addTags(mCategoryTags.get(categoryPosition).getTags().get(tagPosition).getId(), categoryPosition, tagPosition);
                break;
            default:
        }


    }

    @Override
    public void addTagSuccess(int categoryPosition, int tagPosition) {
        mCategoryTags.get(categoryPosition).getTags().get(tagPosition).setMine_has(true);
        switch (mFrom) {
            case REGISTER:
            case USER_EDIT:
                mPresenter.handleCategoryTagsClick(mCategoryTags.get(categoryPosition).getTags().get(tagPosition));
                break;
            default:
        }
        mChoosedTags.add(mCategoryTags.get(categoryPosition).getTags().get(tagPosition));
        mChoosedTagAdapter.notifyDataSetChanged();
        mTagClassAdapter.notifyAllSectionsDataSetChanged();
        updateChooseAboutView();

    }

    @Override
    public void deleteTagSuccess(int position) {
        mChoosedTags.get(position).setMine_has(false);
        switch (mFrom) {
            case REGISTER:
            case USER_EDIT:
                mPresenter.handleCategoryTagsClick(mChoosedTags.get(position));
                break;
            default:
        }

        for (TagCategoryBean categoryTag : mCategoryTags) {
            if (categoryTag.getId() == mChoosedTags.get(position).getTag_category_id()) {
                for (UserTagBean userTagBean : categoryTag.getTags()) {
                    if (userTagBean.getId() == mChoosedTags.get(position).getId()) {
                        userTagBean.setMine_has(false);
                        break;
                    }
                }
                break;
            }
        }
        mChoosedTagAdapter.removeItem(position);
        mTagClassAdapter.notifyAllSectionsDataSetChanged();
        updateChooseAboutView();

    }

    @Override
    public void onBackPressed() {
        switch (mFrom) {
            case REGISTER:
                break;
            case USER_EDIT:
                setresult();
                break;
            case INFO_PUBLISH:
                setresult();
                break;
            case CREATE_CIRCLE:
                setresult();
                break;
            default:
        }

    }

    private void setresult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BUNDLE_CHOOSED_TAGS, mChoosedTags);
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
