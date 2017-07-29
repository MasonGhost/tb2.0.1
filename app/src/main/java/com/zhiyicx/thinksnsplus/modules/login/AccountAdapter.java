package com.zhiyicx.thinksnsplus.modules.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/27
 * @contact email:648129313@qq.com
 */

public class AccountAdapter extends ArrayAdapter<AccountBean> implements Filterable {

    private List<AccountBean> mData;
    private List<AccountBean> mOldData;
    private Context mContext;
    private OnItemSelectListener mListener;

    public AccountAdapter(@NonNull Context context, List<AccountBean> datas, OnItemSelectListener listener) {
        super(context, R.layout.item_account, datas);
        this.mData = datas;
        this.mOldData = datas;
        this.mContext = context;
        this.mListener = listener;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Nullable
    @Override
    public AccountBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_account, parent, false);
            holder = new ViewHolder();
            holder.tvAccountName = (TextView) convertView.findViewById(R.id.tv_account_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AccountBean accountBean = mData.get(position);
        holder.tvAccountName.setText(accountBean.getAccountName());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.tvAccountName.getLayoutParams();
//        // 第一个paddingTop 最后一项paddingBottom  50
//        if (position == 0) {
//            params.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.margin_25);
//        } else {
//            params.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.margin_25) / 2;
//        }
        convertView.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onItemSelect(accountBean);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tvAccountName;
    }

    private class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(null == constraint || 0 == constraint.length()) {
                constraint = "";
            }
            // 这里做一些简单的过滤
            String condition = String.valueOf(constraint).toLowerCase();
            List<AccountBean> temp = new ArrayList<>();
            for (AccountBean accountBean : mOldData) {
                if (accountBean.getAccountName().toLowerCase().contains(condition)) {
                    temp.add(accountBean);
                }
            }
            results.values = temp;
            results.count = temp.size();
            // 返回的results会在publishResult()函数中得到
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData = (ArrayList<AccountBean>) results.values;
            // 更新视图
            notifyDataSetChanged();
            if (mListener != null){
                mListener.onDataChange(mData.size());
            }
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new MyFilter();
    }

    public interface OnItemSelectListener{
        void onItemSelect(AccountBean accountBean);
        void onDataChange(int size);
    }
}
