package com.zhiyicx.thinksnsplus.widget.chooseview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.list.RewardListFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class SingleChooseView extends LinearLayout {
    private static final int DEFAULT_COLUMN = 3;
    private static final int DEFAULT_SHOW_IMAGE_SZIE = 10;
    protected TextView mTvTip;
    protected RecyclerView mRecyclerView;

    private CommonAdapter mCommonAdapter;
    private List<ChooseDataBean> mListData = new ArrayList<>();
    private int mCurrentChoosePositon = -1;
    private OnItemChooseChangeListener mOnItemChooseChangeListener;

    public void setOnItemChooseChangeListener(OnItemChooseChangeListener onItemChooseChangeListener) {
        mOnItemChooseChangeListener = onItemChooseChangeListener;
    }

    public SingleChooseView(@NonNull Context context) {
        this(context, null);
    }

    public SingleChooseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleChooseView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_single_choose, this);
        mTvTip = (TextView) findViewById(R.id.tv_tip);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        initRvUsers();
        initListener();
    }

    private void initRvUsers() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), DEFAULT_COLUMN);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_mid), getResources()
                .getDimensionPixelOffset(R.dimen.spacing_mid)));
        mCommonAdapter = new CommonAdapter<ChooseDataBean>(getContext(), R.layout.item_single_choose, mListData) {
            @Override
            protected void convert(ViewHolder holder, ChooseDataBean data, int position) {
                TextView checkBox = holder.getView(R.id.cb_item);
                checkBox.setText(data.getText());
                checkBox.setBackgroundResource(position == mCurrentChoosePositon ? R.drawable.shape_dynamic_topday_bg_radus_theme : R.drawable
                        .shape_dynamic_topday_bg_radus_grey);
                checkBox.setOnClickListener( l-> {
                    if (mCurrentChoosePositon == position) {
                        mCurrentChoosePositon = -1;
                    } else {
                        mCurrentChoosePositon = position;
                    }
                    if (mOnItemChooseChangeListener != null) {
                        mOnItemChooseChangeListener.onItemChooseChanged(mCurrentChoosePositon, mCurrentChoosePositon == -1 ? null : mListData.get
                                (mCurrentChoosePositon));
                    }

                    mCommonAdapter.notifyDataSetChanged();
                });
            }
        };
        mRecyclerView.setAdapter(mCommonAdapter);

    }

    private void initListener() {

    }

    /**
     * 更新打赏用户列表
     *
     * @param data user list for this rewad source
     */
    public void updateData(List<ChooseDataBean> data) {
        if (data == null) {
            return;
        }
        mListData.clear();
        if (data.size() > DEFAULT_SHOW_IMAGE_SZIE) {
            mListData.addAll(data.subList(0, DEFAULT_SHOW_IMAGE_SZIE - 1));
        } else {
            mListData.addAll(data);
        }
        mCommonAdapter.notifyDataSetChanged();

    }

    public void clearChoose() {
        mCurrentChoosePositon = -1;
        mCommonAdapter.notifyDataSetChanged();

    }

    public void setTip(String str) {
        mTvTip.setText(str);
    }

    public interface OnItemChooseChangeListener {
        void onItemChooseChanged(int position, ChooseDataBean dataBean);
    }

}
