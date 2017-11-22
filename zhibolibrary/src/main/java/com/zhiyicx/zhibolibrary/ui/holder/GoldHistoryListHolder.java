package com.zhiyicx.zhibolibrary.ui.holder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.GoldHistoryJson;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLGoldHistoryDetailActivity;
import com.zhiyicx.zhibolibrary.util.ColorPhrase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class GoldHistoryListHolder extends ZBLBaseHolder<GoldHistoryJson>implements View.OnClickListener {
    private Context mContext;
    TextView mTimeTV;
    TextView mDescriptionTV;
    TextView mCostTV;
    private SimpleDateFormat mFormat;
    private GoldHistoryJson mData;


    public GoldHistoryListHolder(View itemView) {
        super(itemView);

         mTimeTV= (TextView) itemView.findViewById(R.id.tv_gold_history_item_time);
        mDescriptionTV= (TextView) itemView.findViewById(R.id.tv_gold_history_item_description);
        mCostTV= (TextView) itemView.findViewById(R.id.tv_gold_history_cost);
      itemView.findViewById(R.id.content_container).setOnClickListener(this);

        mContext = itemView.getContext();
        mFormat = new SimpleDateFormat("M.dd");
    }

    @Override
    public void setData(GoldHistoryJson data) {
        this.mData = data;
        Date date = new Date(data.ctime * 1000);
        mTimeTV.setText(mFormat.format(date));
        String num = "暂无数据";
        try {
            num = Integer.valueOf(mData.num) > 0 ? "+" + mData.num : mData.num;
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCostTV.setText(num);
        switch (data.order_type) {
            case "5":
                if(data.desction.contains("<")) break;
                try {
                    if (Integer.valueOf(data.num) > 0) {//收入
                        data.desction = "<" + data.uname + ">" + "赠送了你" + data.desction;
                    }
                    else {
                        data.desction = "你赠送了" + "<" + data.to_uname + ">" + data.desction;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;

        }
        CharSequence charSequence = ColorPhrase.from(data.desction)
                .withSeparator("<>").innerColor(0xff3e6ea0).outerColor(0xff333333).format();
        mDescriptionTV.setText(charSequence);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId()==R.id.content_container) {
                Intent intent = new Intent(mContext, ZBLGoldHistoryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ZBLGoldHistoryDetailActivity.KEY_DATA, mData);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
        }


    }
}
