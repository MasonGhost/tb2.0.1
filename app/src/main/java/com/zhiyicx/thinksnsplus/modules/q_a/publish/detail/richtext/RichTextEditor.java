package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.richtext;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/8/7 11:08
 * @Email Jliuer@aliyun.com
 * @Description 图文混排编辑
 */
public class RichTextEditor extends ScrollView implements TextWatcher {
    private static final int EDIT_PADDING = 10; // edittext常规padding是10dp

    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private LayoutInflater inflater;
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private OnClickListener btnListener; // 图片右上角红叉按钮监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private EditText lastFocusEdit; // 最近被聚焦的EditText
    private EditText lastAddEdit; // 最后被添加的EditText
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
    private int editNormalPadding = 0; //
    private int disappearingImageIndex = 0;
    private String mHint; // 提示语

    private OnContentChangeListener mOnContentChangeListener;
    private boolean hasContent;
    private boolean isFirstHasContent = true;

    public RichTextEditor(Context context) {
        this(context, null);
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.MarkDownEditor);
        mHint = array.getString(R.styleable.MarkDownEditor_ts_md_hint);
        if (TextUtils.isEmpty(mHint)) {
            mHint = getResources().getString(R.string.info_content_hint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (allLayout.getChildCount() == 1 && ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastAddEdit.requestFocus();
            showKeyBoard();
        }
        return super.onTouchEvent(ev);
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.MarkDownEditor);
        mHint = array.getString(R.styleable.MarkDownEditor_ts_md_hint);
        if (TextUtils.isEmpty(mHint)) {
            mHint = getResources().getString(R.string.info_content_hint);
        }
        inflater = LayoutInflater.from(context);

        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        //allLayout.setBackgroundColor(Color.WHITE);
        setupLayoutTransitions();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        allLayout.setPadding(dip2px(getContext(), 20), dip2px(getContext(), 15), dip2px(getContext(), 20), 0);//设置间距，防止生成图片时文字太靠边，不能用margin，否则有黑边
        addView(allLayout, layoutParams);

        // 2. 初始化键盘退格监听
        // 主要用来处理点击回删按钮时，view的一些列合并操作
        keyListener = (v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                EditText edit = (EditText) v;
                onBackspacePress(edit);
            }
            return false;
        };

        // 3. 图片叉掉处理
        btnListener = v -> {
            RelativeLayout parentView = (RelativeLayout) v.getParent();
            onImageCloseClick(parentView);
        };

        focusListener = (v, hasFocus) -> {
            if (hasFocus) {
                lastFocusEdit = (EditText) v;
            }
        };

        addFirstEditText(mHint);
    }

    public void addFirstEditText(String hint) {
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //editNormalPadding = dip2px(EDIT_PADDING);
        EditText firstEdit;
        if (!hint.isEmpty()) {
            firstEdit = createEditText(hint, dip2px(getContext(), 0));
        } else {
            firstEdit = createEditText(mHint, dip2px(getContext(), 0));
        }
        firstEdit.setHintTextColor(getResources().getColor(R.color.general_for_hint));
        firstEdit.addTextChangedListener(this);
        firstEdit.setLayoutParams(firstEditParam);
        allLayout.addView(firstEdit);
        lastFocusEdit = firstEdit;
        lastAddEdit = firstEdit;
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        allLayout.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new LayoutTransition.TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                    // transition动画结束，合并EditText
//                     mergeEditText();
                }
            }
        });
        mTransitioner.setDuration(300);
    }

    public int dip2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = allLayout.indexOfChild(editTxt);
            View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    onImageCloseClick(preView);
                    onTextChanged("", 0, 0, 0);
                } else if (preView instanceof EditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();

                    allLayout.removeView(editTxt);

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    lastFocusEdit = preEdit;
                }
            }
        }
    }

    public void deleteImage() {
        onBackspacePress(lastAddEdit);
    }

    /**
     * 处理图片叉掉的点击事件
     *
     * @param view 整个image对应的relativeLayout view
     * @type 删除类型 0代表backspace删除 1代表按红叉按钮删除
     */
    private void onImageCloseClick(View view) {
        disappearingImageIndex = allLayout.indexOfChild(view);
        //删除文件夹里的图片
        List<EditData> dataList = buildEditData();
        EditData editData = dataList.get(disappearingImageIndex);
        //Log.i("", "editData: "+editData);
        if (editData.imagePath != null) {
            //SDCardUtil.deleteFile(editData.imagePath);
        }
        allLayout.removeView(view);
        if (mOnContentChangeListener != null) {
            mOnContentChangeListener.onImageDelete();
        }
    }

    public void clearAllLayout() {
        allLayout.removeAllViews();
    }

    public int getLastIndex() {
        int lastEditIndex = allLayout.getChildCount();
        return lastEditIndex;
    }

    /**
     * 生成文本输入框
     */
    public EditText createEditText(String hint, int paddingTop) {
        EditText editText = (EditText) inflater.inflate(R.layout.rich_edittext, null);
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, paddingTop);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(focusListener);
        editText.addTextChangedListener(this);
        return editText;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.rich_edit_imageview, null);
        layout.setTag(viewTagIndex++);
        View closeView = layout.findViewById(R.id.image_close);
        closeView.setVisibility(GONE);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        return layout;
    }

    /**
     * 根据绝对路径添加view
     */
    public SubsamplingScaleImageView insertImage(String imagePath, int width) {
        Bitmap bmp = getScaledBitmap(imagePath, width);

        return insertImage(bmp, imagePath);
    }

    /**
     * 插入一张图片
     */
    public SubsamplingScaleImageView insertImage(Bitmap bitmap, String imagePath) {
        hideKeyBoard();
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);

        if (lastEditStr.length() == 0 || editStr1.length() == 0) {
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            return addImageViewAtIndex(lastEditIndex, imagePath);
        } else {
            // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
            lastFocusEdit.setText(editStr1);
            String editStr2 = lastEditStr.substring(cursorIndex).trim();
            if (editStr2.length() == 0) {
                editStr2 = "";
            }
            if (allLayout.getChildCount() - 1 == lastEditIndex) {
                addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }
            lastAddEdit.requestFocus();
            lastAddEdit.setSelection(0);
            return addImageViewAtIndex(lastEditIndex + 1, imagePath);

        }

    }

    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(lastFocusEdit, InputMethodManager.SHOW_FORCED);
//        imm.showSoftInputFromInputMethod(lastFocusEdit.getApplicationWindowToken(),
//                InputMethodManager.SHOW_FORCED);
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    private void addEditTextAtIndex(final int index, CharSequence editStr) {
        lastAddEdit = createEditText("", EDIT_PADDING);
        lastAddEdit.setText(editStr);
        lastAddEdit.setOnFocusChangeListener(focusListener);

        allLayout.addView(lastAddEdit, index);
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    public void updateEditTextAtIndex(final int index, CharSequence editStr) {
        lastAddEdit = createEditText("", EDIT_PADDING);
        lastAddEdit.setOnFocusChangeListener(focusListener);
        lastAddEdit.setText(editStr);
        allLayout.addView(lastAddEdit, index);
    }

    /**
     * 在特定位置添加ImageView
     */
    private SubsamplingScaleImageView addImageViewAtIndex(final int index, String imagePath) {
        final RelativeLayout imageLayout = createImageLayout();
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) imageLayout.findViewById(R.id.edit_imageView);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        int scale = options.outWidth / allLayout.getWidth();

        // 暂时这样处理一下
//        imageHeight = imageHeight > DeviceUtils.getScreenHeight(getContext()) ? DeviceUtils.getScreenHeight(getContext()) : imageHeight;

        imageView.setImage(ImageSource.uri(imagePath)
                        .region(new Rect(0, 0, options.outWidth, options.outHeight)),
                ImageSource.resource(R.drawable.shape_default_image).dimensions(allLayout.getWidth(), allLayout.getWidth()));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.bottomMargin = 10;
        imageView.setLayoutParams(lp);
        imageView.setAbsolutePath(imagePath);//保留这句，后面保存数据会用


        allLayout.addView(imageLayout, index);

        onTextChanged("", 0, 0, 0);
        return imageView;
    }

    /**
     * 在特定位置添加ImageView
     */
    public void updateImageViewAtIndex(final int index, int id, String imagePath, String markdonw, boolean isLast) {
        if (allLayout.getChildCount() == 0) {
            addFirstEditText(" ");// 这个空格是有必要的，没有空格就是默认文字
        }
        final RelativeLayout imageLayout = createImageLayout();
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) imageLayout.findViewById(R.id.edit_imageView);
        imageView.setId(id);
        imageView.setAbsolutePath(markdonw);//保留这句，后面保存数据会用

        // 调整imageView的高度，根据宽度来调整高度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
        int imageHeight;
        if (bmp != null) {
            imageHeight = allLayout.getWidth() * bmp.getHeight() / bmp.getWidth();
            bmp.recycle();
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, imageHeight);//设置图片固定高度
            lp.bottomMargin = 10;
            imageView.setLayoutParams(lp);
        } else {
            imageHeight = allLayout.getWidth();
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, imageHeight);//设置图片固定高度
            imageView.setLayoutParams(lp);
            Bitmap defalut = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.icon_256);
            imageView.setImage(ImageSource.bitmap(defalut));
        }
        LogUtils.d("updateImageViewAtIndex::" + imagePath);
        Glide.with(getContext())
                .load(imagePath)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        String path= FileUtils.saveBitmapToFile(getContext(),resource,"qa"+id);

//                        imageView.setImage(ImageSource.uri(path)
//                                        .region(new Rect(0, 0, resource.getWidth(), resource.getHeight())));
//                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
//                        lp.bottomMargin = 10;
//                        imageView.setLayoutParams(lp);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        LogUtils.e("onLoadFailed::"+e.toString());
                    }
                });

        allLayout.addView(imageLayout, index);
        if (isLast) {
            addEditTextAtIndex(getLastIndex(), "");
        }
        onTextChanged("", 0, 0, 0);
    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    public Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width
                + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 对外提供的接口, 生成编辑数据上传
     */
    public List<EditData> buildEditData() {
        List<EditData> dataList = new ArrayList<>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            EditData itemData = new EditData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                itemData.inputStr = item.getText().toString();
            } else if (itemView instanceof RelativeLayout) {
                SubsamplingScaleImageView item = (SubsamplingScaleImageView) itemView.findViewById(R.id.edit_imageView);
                itemData.imagePath = item.getAbsolutePath();
                itemData.imageId = item.getId();
            }
            dataList.add(itemData);
        }

        return dataList;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mOnContentChangeListener != null) {
            List<EditData> data = buildEditData();
            hasContent = data.size() > 1 || data.size() == 1 && !data.get(0).inputStr.isEmpty();
            mOnContentChangeListener.onContentChange(hasContent);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        View view = allLayout.getChildAt(0);
        if (view != null && view instanceof EditText) {
            int tag = (int) view.getTag();
            EditText firstEditText = (EditText) view;

            if (tag == 1 && isFirstHasContent) {
                isFirstHasContent = false;
                firstEditText.setHint("");
            }
            if (tag == 1 && s.toString().isEmpty()) {
                isFirstHasContent = true;
                firstEditText.setHint(mHint);
            }
        }
    }

    public void setOnContentEmptyListener(OnContentChangeListener onContentChangeListener) {
        mOnContentChangeListener = onContentChangeListener;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
    }

    public class EditData {
        public String inputStr = "";
        public String imagePath = "";
        public int imageId;
    }

    public interface OnContentChangeListener {
        void onContentChange(boolean hasContent);

        void onImageDelete();
    }

    /**
     * 对外提供的设置提示语的方法
     *
     * @param hint 提示语
     */
    public void setHint(String hint) {
        this.mHint = hint;
        if (allLayout.getChildAt(0) instanceof EditText) {
            EditText text = (EditText) allLayout.getChildAt(0);
            text.setHint(mHint);
        }
    }
}
