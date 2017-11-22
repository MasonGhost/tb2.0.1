package com.zhiyicx.zhibolibrary.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.utils.AutoLayoutHelper;


/**
 * Created by jess on 2015/11/19.
 */
public class FllowButtonView extends RelativeLayout {

    private  int _textSize;
    private View _view;
    private TextView _tvName;
    private AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    public FllowButtonView(Context context) {
        this(context, null);
    }

    public FllowButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);


//        _textSize = attrs.getAttributeResourceValue(NAMESPACE, "textsize", 14);
        init(context,attrs);
    }

    public FllowButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FllowButtonView);
        _textSize = array.getResourceId(R.styleable.FllowButtonView_namesize, 14);

        _view = View.inflate(this.getContext(), R.layout.zb_view_fllow_button, null);
        this.addView(_view);
        _tvName = (TextView) _view.findViewById(R.id.name);
        setNameSize(_textSize);
        setGravity(Gravity.CENTER);

    }

    public void setName(String name) {
        _tvName.setText(name);
    }

    public void setNameColor(int res) {

        _tvName.setTextColor(res);
    }

    public void setNameSize(int size) {
        _tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
    }

    public String getNameString() {
        return _tvName.getText().toString();
    }

    public void setNameLeftDrawable(Integer res) {
        _tvName.setCompoundDrawablePadding(8);
        if (res == null) {
            _tvName.setCompoundDrawables(null, null, null, null);
            return;
        }
        Drawable drawable = getResources().getDrawable(res);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        _tvName.setCompoundDrawables(drawable, null, null, null);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!isInEditMode())
            mHelper.adjustChildren();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FllowButtonView.LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends RelativeLayout.LayoutParams
            implements AutoLayoutHelper.AutoLayoutParams
    {
        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo()
        {
            return mAutoLayoutInfo;
        }


        public LayoutParams(int width, int height)
        {
            super(width, height);
        }


        public LayoutParams(ViewGroup.LayoutParams source)
        {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source)
        {
            super(source);
        }

    }

}
