package com.zhiyicx.thinksnsplus.modules.rank.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhiyicx.thinksnsplus.data.source.repository.PersonalCenterRepository;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.rank.type_list.RankTypeListActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.rank.type_list.RankTypeListActivity.BUNDLE_RANK_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class TypeChoosePopAdapter extends CommonAdapter<String> {

    private PersonalCenterRepository.MyDynamicTypeEnum mDynamicType;
    private OnTypeChoosedListener mOnTypeChoosedListener;

    public TypeChoosePopAdapter(Context context, List<String> datas, PersonalCenterRepository.MyDynamicTypeEnum dynamicType, OnTypeChoosedListener
            onTypeChoosedListener) {
        super(context, R.layout.item_type_choose_pop, datas);
        this.mDynamicType = dynamicType;
        this.mOnTypeChoosedListener = onTypeChoosedListener;
    }

    public TypeChoosePopAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_type_choose_pop, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String str, int position) {
        AppCompatCheckedTextView textView = holder.getView(R.id.tv_title);
        textView.setText(str);
        switch (mDynamicType) {

            case ALL:
                if (getContext().getString(R.string.all_dynamic).equals(str)) {
                    textView.setChecked(true);

                } else {
                    textView.setChecked(false);
                }

                break;
            case PAID:
                if (getContext().getString(R.string.pay_dynamic).equals(str)) {
                    textView.setChecked(true);

                } else {
                    textView.setChecked(false);
                }

                break;
            case PINNED:
                if (getContext().getString(R.string.top_dynamic).equals(str)) {
                    textView.setChecked(true);

                } else {
                    textView.setChecked(false);
                }


                break;
            default:

        }
        RxView.clicks(textView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (getContext().getString(R.string.all_dynamic).equals(str)) {
                        mDynamicType = PersonalCenterRepository.MyDynamicTypeEnum.ALL;

                    } else if (getContext().getString(R.string.pay_dynamic).equals(str)) {
                        mDynamicType = PersonalCenterRepository.MyDynamicTypeEnum.PAID;

                    } else if (getContext().getString(R.string.top_dynamic).equals(str)) {
                        mDynamicType = PersonalCenterRepository.MyDynamicTypeEnum.PINNED;
                    }

                    if (mOnTypeChoosedListener != null) {
                        mOnTypeChoosedListener.onChoosed(mDynamicType);
                    }
                    notifyDataSetChanged();

                });
    }

    public interface OnTypeChoosedListener

    {
        void onChoosed(PersonalCenterRepository.MyDynamicTypeEnum type);
    }
}
