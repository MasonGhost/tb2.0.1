package com.zhiyicx.thinksnsplus.modules.login;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AccountBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/27
 * @contact email:648129313@qq.com
 */

public class AccountAdapterV2 extends CommonAdapter<AccountBean>{

    private List<AccountBean> mOldData;

    public AccountAdapterV2(Context context, List<AccountBean> datas) {
        super(context, R.layout.item_account, datas);
        this.mOldData = datas;
    }

    @Override
    protected void convert(ViewHolder holder, AccountBean accountBean, int position) {
        TextView tvAccountName = holder.getView(R.id.tv_account_name);
        tvAccountName.setText(accountBean.getAccountName());
//        // 第一个paddingTop 最后一项paddingBottom  50
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvAccountName.getLayoutParams();
//        if (position == 0) {
//            params.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.margin_25);
//        } else {
//            params.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.margin_25) / 2;
//        }
    }
}
