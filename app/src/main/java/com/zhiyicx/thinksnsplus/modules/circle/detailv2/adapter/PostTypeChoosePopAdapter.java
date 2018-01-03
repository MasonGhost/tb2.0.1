package com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckedTextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.rank.adapter.TypeChoosePopAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jliuer
 * @Date 2017/12/07/17:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PostTypeChoosePopAdapter extends TypeChoosePopAdapter {

    private MyPostTypeEnum mMyPostTypeEnum;
    private OnTypeChoosedListener mOnTypeChoosedListener;

    public PostTypeChoosePopAdapter(Context context,List<String> datas,
                                    MyPostTypeEnum myPostTypeEnum, OnTypeChoosedListener onTypeChoosedListener) {
        super(context, datas);
        mMyPostTypeEnum = myPostTypeEnum;
        mOnTypeChoosedListener = onTypeChoosedListener;
    }

    @Override
    protected void convert(ViewHolder holder, String str, int position) {
        AppCompatCheckedTextView textView = holder.getView(R.id.tv_title);
        textView.setText(str);
        switch (mMyPostTypeEnum) {

            case ALL:
                if (getContext().getString(R.string.post_typpe_all).equals(str)) {
                    textView.setChecked(true);

                } else {
                    textView.setChecked(false);
                }

                break;
            case LATEST_POST:
                if (getContext().getString(R.string.post_typpe_new).equals(str)) {
                    textView.setChecked(true);

                } else {
                    textView.setChecked(false);
                }

                break;
            case LATEST_COMMENT:
                if (getContext().getString(R.string.post_typpe_reply).equals(str)) {
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
                    if (getContext().getString(R.string.post_typpe_all).equals(str)) {
                        mMyPostTypeEnum = MyPostTypeEnum.ALL;

                    } else if (getContext().getString(R.string.post_typpe_new).equals(str)) {
                        mMyPostTypeEnum = MyPostTypeEnum.LATEST_POST;

                    } else if (getContext().getString(R.string.post_typpe_reply).equals(str)) {
                        mMyPostTypeEnum = MyPostTypeEnum.LATEST_COMMENT;
                    }

                    if (mOnTypeChoosedListener != null) {
                        mOnTypeChoosedListener.onChoosed(mMyPostTypeEnum);
                    }
                    notifyDataSetChanged();

                });
    }

    public interface OnTypeChoosedListener {
        void onChoosed(MyPostTypeEnum type);
    }

    public enum MyPostTypeEnum {
        ALL("latest_post"),
        LATEST_POST("latest_post"),
        LATEST_COMMENT("latest_reply");
        public String value;

        MyPostTypeEnum(String value) {
            this.value = value;
        }
    }
}
