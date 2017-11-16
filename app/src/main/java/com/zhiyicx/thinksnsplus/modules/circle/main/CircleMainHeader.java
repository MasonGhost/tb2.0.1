package com.zhiyicx.thinksnsplus.modules.circle.main;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;

import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;

import java.util.List;
import java.util.Locale;

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

    public CircleMainHeader(Context context, List<RealAdvertListBean> adverts, int count) {
        String circleCount = String.format(Locale.getDefault(), context.getString(R.string.group_count), count);
        int lengh = (count + "").length();
        SpannableStringBuilder countSpan = new SpannableStringBuilder(circleCount);
        countSpan.setSpan(new RelativeSizeSpan(1.66f), 0, lengh, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        countSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.important_for_note)), 0, lengh, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mCircleMainHeader = LayoutInflater.from(context).inflate(R.layout
                .circle_main_header, null);
        mCircleCount = (CombinationButton) mCircleMainHeader.findViewById(R.id.tv_circle_count);
        mCircleCount.setLeftTextSize(12f);
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
