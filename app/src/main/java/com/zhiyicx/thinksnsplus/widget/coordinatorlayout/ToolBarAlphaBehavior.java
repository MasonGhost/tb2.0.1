package com.zhiyicx.thinksnsplus.widget.coordinatorlayout;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import java.util.Arrays;

/**
 * @author LiuChao
 * @describe toolBar跟随页面的滑动，改变背景
 * @date 2017/3/9
 * @contact email:450127106@qq.com
 */

public class ToolbarAlphaBehavior extends CoordinatorLayout.Behavior<Toolbar> {

    private static final String TAG = "ToolbarAlphaBehavior";
    private int offset = 0;
    private int startOffset = 0;
    private int endOffset = 0;
    private Context context;

    public ToolbarAlphaBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, Toolbar child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, Toolbar toolbar, View target, int dx, int dy, int[] consumed) {
        LogUtils.i("onNestedPreScroll--> dx" + dx + " dy-->" + dy + " consumed-->" + Arrays.toString(consumed));
        super.onNestedPreScroll(coordinatorLayout, toolbar, target, dx, dy, consumed);
        startOffset = 0;
        //endOffset = context.getResources().getDimensionPixelOffset(R.dimen.useredit_divier_marginleft) - toolbar.getHeight();
        endOffset = UIUtils.getWindowWidth(context) / 2 + context.getResources().getDimensionPixelSize(R.dimen.spacing_large);
        offset += dy;
        if (offset <= startOffset) {  //alpha为0
            toolbar.getBackground().setAlpha(0);
        } else if (offset > startOffset && offset < endOffset) { //alpha为0到255
            float precent = (float) (offset - startOffset) / endOffset;
            int alpha = Math.round(precent * 255);
            toolbar.getBackground().setAlpha(alpha);
        } else if (offset >= endOffset) {  //alpha为255
            toolbar.getBackground().setAlpha(255);
        }
        LogUtils.i("offset-->" + offset + " endOffset-->" + endOffset + " target-->" + target.getTop());
    }
}
