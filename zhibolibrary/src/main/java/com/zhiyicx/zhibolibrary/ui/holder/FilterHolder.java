package com.zhiyicx.zhibolibrary.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.FilterMessage;
import com.zhiyicx.zhibolibrary.util.UiUtils;


/**
 * Created by jess on 8/19/16 18:11
 * Contact with jess.yan.effort@gmail.com
 */
public class FilterHolder extends ZBLBaseHolder<FilterMessage> {

    TextView mName;

    ImageView mBorder;


    public FilterHolder(View itemView) {
        super(itemView);
        mName = (TextView) itemView.findViewById(R.id.tv_filter_name);
        mBorder = (ImageView) itemView.findViewById(R.id.iv_filter_border);

    }


    @Override
    public void setData(FilterMessage data) {
        mName.setText(data.name);
        if (data.isClick) {
            mBorder.setVisibility(View.VISIBLE);
            mName.setTextColor(UiUtils.getColor(R.color.color_blue_button));
        }
        else {
            mBorder.setVisibility(View.GONE);
            mName.setTextColor(UiUtils.getColor(R.color.white));
        }
    }
}
