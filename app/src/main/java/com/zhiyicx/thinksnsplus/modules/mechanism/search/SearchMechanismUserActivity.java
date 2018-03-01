package com.zhiyicx.thinksnsplus.modules.mechanism.search;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Describe 圈子搜索容器
 * @Author Jungle68
 * @Date 2017/12/7
 * @Contact master.jungle68@gmail.com
 */
public class SearchMechanismUserActivity extends TSActivity<SearchMechanismUserPresenter, SearchMechanismUserFragment> {

    @Override
    protected void componentInject() {

    }

    @Override
    protected SearchMechanismUserFragment getFragment() {
        return SearchMechanismUserFragment.newInstance();
    }
//
//    /**
//     * @param context not application context
//     * @param circleInfo 圈子的信息
//     */
//    public static void startCircelPostSearchActivity(Context context, CircleInfo circleInfo) {
//
//        Intent intent = new Intent(context, SearchMechanismUserActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(BaseCircleDetailFragment.CIRCLE, circleInfo);
//        intent.putExtras(bundle);
//        if (context instanceof Activity) {
//            context.startActivity(intent);
//        } else {
//            throw new IllegalAccessError("context must instance of Activity");
//        }
//    }

}
