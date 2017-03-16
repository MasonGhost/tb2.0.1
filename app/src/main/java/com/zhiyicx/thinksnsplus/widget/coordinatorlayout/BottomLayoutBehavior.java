package com.zhiyicx.thinksnsplus.widget.coordinatorlayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @author LiuChao
 * @describe ToolBar跟随RecyclerView滑动隐藏，底部控件依赖ToolBar移动，
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public class BottomLayoutBehavior extends CoordinatorLayout.Behavior<View> {

    public BottomLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        // 需要依赖的控件是AppBarLayout类型的
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.abs(dependency.getTop());//获取更随布局的顶部位置
        LogUtils.i("CoordinatorLayout-->  dependency-->" + dependency.getHeight() + " child-->" + child.getHeight());
        // 因为底部控件必ToolBar高，此时如果移动相同的距离，底部控件会还有一截露出来，好尴尬- 。-
        translationY = translationY * child.getHeight() / dependency.getHeight();
        child.setTranslationY(translationY);
        return true;
    }
}
