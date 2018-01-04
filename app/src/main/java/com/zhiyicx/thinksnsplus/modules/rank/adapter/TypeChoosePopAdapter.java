package com.zhiyicx.thinksnsplus.modules.rank.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckedTextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class TypeChoosePopAdapter extends CommonAdapter<String> {

    private BaseDynamicRepository.MyDynamicTypeEnum mDynamicType;
    private OnTypeChoosedListener mOnTypeChoosedListener;

    public TypeChoosePopAdapter(Context context, List<String> datas, BaseDynamicRepository.MyDynamicTypeEnum dynamicType, OnTypeChoosedListener
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
                        mDynamicType = BaseDynamicRepository.MyDynamicTypeEnum.ALL;

                    } else if (getContext().getString(R.string.pay_dynamic).equals(str)) {
                        mDynamicType = BaseDynamicRepository.MyDynamicTypeEnum.PAID;

                    } else if (getContext().getString(R.string.top_dynamic).equals(str)) {
                        mDynamicType = BaseDynamicRepository.MyDynamicTypeEnum.PINNED;
                    }

                    if (mOnTypeChoosedListener != null) {
                        mOnTypeChoosedListener.onChoosed(mDynamicType);
                    }
                    notifyDataSetChanged();

                });
    }

    public interface OnTypeChoosedListener

    {
        void onChoosed(BaseDynamicRepository.MyDynamicTypeEnum type);
    }
}
