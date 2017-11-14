package com.zhiyicx.thinksnsplus.modules.circle.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/11/14/10:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainHeader {
    private DynamicDetailAdvertHeader mDynamicDetailAdvertHeader;
    private CombinationButton mCircleCount;
    private View mCircleMainHeader;

    public View getCircleMainHeader() {
        return mCircleMainHeader;
    }

    public CircleMainHeader(Context context, List<RealAdvertListBean> adverts) {

        mCircleMainHeader = LayoutInflater.from(context).inflate(R.layout
                .circle_main_header, null);

        mCircleCount = (CombinationButton) mCircleMainHeader.findViewById(R.id.tv_circle_count);

        initAdvert(context, adverts);
    }

    private void initAdvert(Context context, List<RealAdvertListBean> adverts) {
        mDynamicDetailAdvertHeader = new DynamicDetailAdvertHeader(context, mCircleMainHeader
                .findViewById(R.id.ll_advert));
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || adverts == null || adverts != null && adverts.isEmpty()) {
            mDynamicDetailAdvertHeader.hideAdvert();
            return;
        }
        mDynamicDetailAdvertHeader.setTitle("广告");
        mDynamicDetailAdvertHeader.setAdverts(adverts);
        mDynamicDetailAdvertHeader.setOnItemClickListener((v, position1, url) ->
                toAdvert(context, adverts.get(position1).getAdvertFormat().getImage().getLink(), adverts.get(position1).getTitle())
        );
    }

    private void toAdvert(Context context, String link, String title) {
        CustomWEBActivity.startToWEBActivity(context, link, title);
    }
}
