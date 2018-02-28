package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Jungle68
 * @describe
 * @date 2018/2/28
 * @contact master.jungle68@gmail.com
 */
public class TBDynamicFragment extends DynamicFragment {

    public static TBDynamicFragment newInstance(String dynamicType, OnCommentClickListener l) {
        TBDynamicFragment fragment = new TBDynamicFragment();
        fragment.setOnCommentClickListener(l);
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, dynamicType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onMenuItemClick(View view, int dataPosition, int viewPosition) {
        dataPosition -= mHeaderAndFooterWrapper.getHeadersCount();
        switch (viewPosition) {
            // 0 1 2 3 代表 view item 位置

            case 0:
                // 喜欢
                if ((!TouristConfig.DYNAMIC_CAN_DIGG && mPresenter.handleTouristControl()) ||
                        mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0) {
                    return;
                }
                handleLike(dataPosition);
                break;

            case 2:
                // 分享
                break;

            case 3:
                // 更多
                Bitmap shareBitMap = null;
                try {
                    ImageView imageView = (ImageView) layoutManager.findViewByPosition
                            (dataPosition + mHeaderAndFooterWrapper.getHeadersCount()).findViewById(R.id.siv_0);
                    shareBitMap = ConvertUtils.drawable2BitmapWithWhiteBg(getContext(), imageView
                            .getDrawable(), R.mipmap.icon);
                } catch (Exception e) {
                }
                if (AppApplication.getmCurrentLoginAuth() != null && mListDatas.get(dataPosition)
                        .getUser_id() == AppApplication.getMyUserIdWithdefault()) {
                    initMyDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition,
                            mListDatas.get(dataPosition)
                                    .isHas_collect(), shareBitMap);
                    mMyDynamicPopWindow.show();
                } else if (mListDatas.get(dataPosition).getFeed_from() != -1) {
                    initOtherDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition,
                            mListDatas.get(dataPosition)
                                    .isHas_collect(), shareBitMap);
                    mOtherDynamicPopWindow.show();
                } else {
                    // 广告

                }
                break;
            default:

        }
    }

}
