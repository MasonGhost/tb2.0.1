package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/27
 * @contact email:648129313@qq.com
 */

public class UserTagAdapter extends BaseAdapter{

    private Context mContext;

    public UserTagAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return "xxxxxxxx";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tag, null);
        TextView tvTagName = (TextView) view.findViewById(R.id.tv_tag_name);
        tvTagName.setText(position + "xxxx");
        return view;
    }
}
