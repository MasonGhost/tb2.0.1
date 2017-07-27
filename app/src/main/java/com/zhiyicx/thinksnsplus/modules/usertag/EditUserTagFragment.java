package com.zhiyicx.thinksnsplus.modules.usertag;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.recycleview.stickygridheaders.StickyHeaderGridLayoutManager;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class EditUserTagFragment extends TSFragment<EditUserTagContract.Presenter> implements EditUserTagContract.View {
    private static final int SPAN_SIZE = 4;
    private static final int SECTIONS = 10;
    private static final int SECTION_ITEMS = 5;

    @BindView(R.id.tv_choosed_tag_tip)
    TextView mTvChoosedTagTip;
    @BindView(R.id.tv_jump_over)
    TextView mTvJumpOver;
    @BindView(R.id.rv_choosed_tag)
    RecyclerView mRvChoosedTag;
    @BindView(R.id.rv_tag_class)
    RecyclerView mRvTagClass;

    private GridLayoutManager mChoosedTagLayoutManager;
    private StickyHeaderGridLayoutManager mTagClassLayoutManager;

    private CommonAdapter mChoosedTagAdapter;

    private List<UserTagBean> mChoosedTags = new ArrayList<>();
    private List<UserTagBean> mTagClasses = new ArrayList<>();

    private int mMaxChooseNums;
    private int mCurrentChooseNums = 0;


    public static EditUserTagFragment newInstance() {
        return new EditUserTagFragment();
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
    protected String setRightTitle() {
        return getString(R.string.save);
    }

    @Override
    protected void initView(View rootView) {
        mMaxChooseNums = getResources().getInteger(R.integer.user_tag_max_nums);
        mTvChoosedTagTip.setText(getString(R.string.user_tag_choosed_tag_format, mMaxChooseNums, mCurrentChooseNums));
        initRvChoosedTag();
        initRvTagClass();
        initListener();
    }

    private void initRvChoosedTag() {
        mChoosedTagLayoutManager = new GridLayoutManager(getContext(), SPAN_SIZE);
        initSubscribeAdapter();
    }

    private CommonAdapter initSubscribeAdapter() {
        mChoosedTagAdapter = new CommonAdapter<UserTagBean>(getActivity(), R.layout
                .item_info_channel, mChoosedTags) {
            @Override
            protected void convert(ViewHolder holder, UserTagBean data
                    , final int position) {
//                TextView textView = holder.getView(R.id.item_info_channel);
//                ImageView delete = holder.getView(R.id.item_info_channel_deal);
//
//                if (position == 0) {
//                    textView.setBackgroundResource(R.drawable.item_channel_bg_blue);
//                    if (isEditor) {
//                        textView.setBackgroundColor(Color.WHITE);
//                    }
//                } else {
//                    textView.setBackgroundResource(R.drawable.item_channel_bg_normal);
//                }
//                if (isEditor && position != 0) {
//                    delete.setVisibility(View.VISIBLE);
//                } else {
//                    delete.setVisibility(View.GONE);
//                }
//                holder.setText(R.id.item_info_channel, data.getName());
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

                viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
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
        mChoosedTagAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                if (isEditor && position != 0) {
//                    InfoTypeMyCatesBean bean = mMyCatesBeen.get(position);
//                    mSubscribeAdapter.removeItem(position);
//                    mUnSubscribeAdapter.addItem(new InfoTypeMoreCatesBean(bean.getId(),
//                            bean.getName()));
//                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
//                if (!isEditor) {
//                    mFragmentChannelEditor.performClick();
//                }
                return false;
            }
        });

        return mChoosedTagAdapter;
    }

    private void initRvTagClass() {
        mTagClassLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
        mTagClassLayoutManager.setHeaderBottomOverlapMargin(getResources().getDimensionPixelSize(R.dimen.spacing_small));
        mTagClassLayoutManager.setSpanSizeLookup(new StickyHeaderGridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int section, int position) {
                switch (section) {
                    case 0:
                        return 3;
                    case 1:
                        return 1;
                    case 2:
                        return 3 - position % 3;
                    case 3:
                        return position % 2 + 1;
                    default:
                        return 1;
                }
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
        mRvTagClass.setAdapter(new TagClassAdapter(SECTIONS, SECTION_ITEMS));

    }

    @Override
    protected void initData() {
    }


    private void initListener() {
//
//        // 退出登录
//        RxView.clicks(mBtLoginOut)
//                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
//                .compose(this.<Void>bindToLifecycle())
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        initLoginOutPopupWindow();
//                        mLoginoutPopupWindow.show();
//                    }
//                });
    }

    /**
     * check save button
     *
     * @return
     */
    private boolean checkSaveEnable() {
        return mCurrentChooseNums > 0;
    }

}
