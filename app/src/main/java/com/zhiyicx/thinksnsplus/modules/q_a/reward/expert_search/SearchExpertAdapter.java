package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import android.content.Context;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/26
 * @contact email:648129313@qq.com
 */

public class SearchExpertAdapter extends CommonAdapter<ExpertBean>{

    public SearchExpertAdapter(Context context, List<ExpertBean> datas) {
        super(context, R.layout.item_search_expert, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ExpertBean expertBean, int position) {

    }
}
