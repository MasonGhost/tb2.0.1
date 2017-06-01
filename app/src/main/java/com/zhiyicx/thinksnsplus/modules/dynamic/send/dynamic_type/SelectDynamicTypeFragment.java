package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/05/25/14:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SelectDynamicTypeFragment extends TSFragment {

    @BindView(R.id.send_words_dynamic)
    IconTextView mSendWordsDynamic;
    @BindView(R.id.send_image_dynamic)
    IconTextView mSendImageDynamic;
    @BindView(R.id.im_close_dynamic)
    ImageView mImCloseDynamic;

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        getActivity().getWindow().getDecorView().setBackgroundColor(getColor(R.color.tym));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_type;
    }

    @OnClick({R.id.send_words_dynamic, R.id.send_image_dynamic, R.id.im_close_dynamic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_words_dynamic:
                SendDynamicDataBean sendWordsDynamicDataBean = new SendDynamicDataBean();
                sendWordsDynamicDataBean.setDynamicBelong(SendDynamicDataBean.MORMAL_DYNAMIC);
                sendWordsDynamicDataBean.setDynamicType(SendDynamicDataBean.TEXT_ONLY_DYNAMIC);
                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendWordsDynamicDataBean);
                break;
            case R.id.send_image_dynamic:
                SendDynamicDataBean sendImageDynamicDataBean = new SendDynamicDataBean();
                sendImageDynamicDataBean.setDynamicBelong(SendDynamicDataBean.MORMAL_DYNAMIC);
                sendImageDynamicDataBean.setDynamicType(SendDynamicDataBean.PHOTO_TEXT_DYNAMIC);
                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendImageDynamicDataBean);
                break;
            case R.id.im_close_dynamic:

                break;
        }
        getActivity().finish();
        getActivity().overridePendingTransition(0, R.anim.slide_out_bottom);
    }

}
