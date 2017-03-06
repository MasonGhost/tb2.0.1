package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */

public class DigListAdapter extends CommonAdapter<FollowFansBean> {
    public DigListAdapter(Context context, int layoutId, List<FollowFansBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, FollowFansBean followFansBean, int position) {
        FilterImageView filterImageView = holder.getView(R.id.iv_headpic);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView iv_follow = holder.getView(R.id.iv_follow);



    }
}
