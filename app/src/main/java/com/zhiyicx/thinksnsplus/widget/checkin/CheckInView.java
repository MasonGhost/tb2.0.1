package com.zhiyicx.thinksnsplus.widget.checkin;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.LinearGradientUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * //                       .::::.
 * //                     .::::::::.
 * //                    :::::::::::
 * //                 ..:::::::::::'
 * //              '::::::::::::'
 * //                .::::::::::
 * //           '::::::::::::::..
 * //                ..::::::::::::.
 * //              ``::::::::::::::::
 * //               ::::``:::::::::'        .:::.
 * //              ::::'   ':::::'       .::::::::.
 * //            .::::'      ::::     .:::::::'::::.
 * //           .:::'       :::::  .:::::::::' ':::::.
 * //          .::'        :::::.:::::::::'      ':::::.
 * //         .::'         ::::::::::::::'         ``::::.
 * //     ...:::           ::::::::::::'              ``::.
 * //    ```` ':.          ':::::::::'                  ::::..
 * //                       '.:::::'                    ':'````..
 */

public class CheckInView extends View {
    private static final int DEF_HEIGHT = 49; //默认高度
    private static final int TEXT_MARGIN_TOP = 13; // 文字距离团的marginTop值
    private static final float SECTION_SCALE = 1.5F / 2; //截面的缩放值
    private static final float SIGN_IN_BALL_SCALE = 1F / 2; //签到 六边形的缩放值
    private static final float SIGN_BG_RECT_SCALE = 1F / 7; //横线的 缩放值


    private int signInBgColor;  //签到背景颜色
    private int signInPbStartColor;  //签到横线颜色

    private int signInPbEndColor;  //签到横线颜色
    private int signInTextColor;  //第一天  第二天.....字体颜色
    private int signInTextSize;  //字体大小

    private Paint signInBgPaint;  //签到背景  画笔
    private Paint mCheckedPbPaint;    //签到画笔
    private Paint mCheckedCirclePaint;    //签到圆画笔
    private Paint signInTextPaint;  //字体画笔

    private LinearGradientUtil mLinearGradientUtil;

    private int viewHeight;  //控件高度
    private int viewWidth; //控件宽度
    private int signInBallRadio; //签到园形的 半径
    private int signInRectHeight; //横线高度

    private RectF signInBgRectF; //整个屏幕宽度的一条黑色的直线

    private int circleY;   //因为每个六边形的 Y坐标是不变的    这个是Y左边的值
    private int descY; //这个是文字的Y坐标

    private int currentSignInTag; //第几天的标识

    private List<String> viewData;  //存放 第一天 第二天......第七天
    private List<Point> circlePoints; //画园形的 各个中心点坐标
    private List<Path> signInDoublePaths;
    private List<Path> sexanglePaths;  //签到(六边形)路径   --->这个List 存放的是七个六边形的路径
    private List<Path> sexangleDoublePaths; //放大二倍六边形 路径
    private List<Point> descPoints; //矩形的点坐标
    private List<RectF> signInPbRectFs;  //签到的矩形
    private List<Path> selectLinePath;  //签到的矩形 做动画效果
    private List<Integer> checkedCircleColors; // 选中的园颜色

    private float mSexanglePercent;          //以下三个 是矩形  六边形  对号  的值动画(顺序不固定)    使用方法可以百度一下 - -
    private ValueAnimator mSexangleAnimator;
    private Path mSexangleDest;
    private Boolean isAnamitorStart = false;

    private float mSelectRectPercent;
    private ValueAnimator mSelectRectAnimator;
    private PathMeasure mRectPathMeasure;
    private Path mRectDest;
    private boolean isRectAnimatorStart = false;


    private float mSelectSignInPercent;
    private ValueAnimator mSelectSignInAnimator;
    private PathMeasure mSignInMeasure;
    private Path mSignInDest;
    private Boolean isSignInStaer;

    private Bitmap bitmap;   //礼物图标
    private Rect srcBitmap;
    private Rect desBitmap;

    public CheckInView(Context context) {
        this(context, null);
    }

    public CheckInView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckInView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化颜色字体大小
        initAttrs(context, attrs, defStyleAttr);
        //初始化一些工具
        initToolsAndData();
        initAnimator();
        setBackgroundColor(getResources().getColor(R.color.white));
        mLinearGradientUtil = new LinearGradientUtil(signInPbStartColor, signInPbEndColor);
    }

    /**
     * 当View  发小发生改变的时候调用这个方法
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int textMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_MARGIN_TOP, getResources().getDisplayMetrics());

        viewWidth = w;
        viewHeight = h;

        signInBallRadio = (int) (viewHeight * SIGN_IN_BALL_SCALE / 2);
        signInRectHeight = (int) (signInBallRadio * SIGN_BG_RECT_SCALE);

        signInBgRectF = new RectF(0, viewHeight * SECTION_SCALE - signInBallRadio - signInRectHeight, viewWidth, viewHeight * SECTION_SCALE -
                signInBallRadio);

        circleY = (int) (signInBgRectF.top + signInRectHeight / 2);
//        descY = (int) (viewHeight * SECTION_SCALE + textMarginTop);
        descY = circleY;

        //计算各个点 图形的位置
        calcucateCirclePoints(viewData);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (circlePoints == null || circlePoints.isEmpty()) {
            return;
        }
        // 签到背景横线
        drawSignInBgRect(canvas);
        // 签到背景圆
        drawSignInNormalCircle(canvas);

        //选择第几天之前的矩形
        drawSignInPbRect(canvas);
        // checked 的圆
        drawSignInCheck(canvas);
        //绘制文字
        drawTextDesc(canvas);
        //绘制礼物图标  如果不用  可以注释掉
        drawBitmap(canvas);
    }

    /**
     * 签到背景线
     *
     * @param canvas
     */
    private void drawSignInBgRect(Canvas canvas) {
        canvas.drawRect(signInBgRectF, signInBgPaint);
    }

    /**
     * 签到背景圆
     *
     * @param canvas
     */
    private void drawSignInNormalCircle(Canvas canvas) {

        if (null != circlePoints && circlePoints.size() > 0) {
            for (Point circlePoint : circlePoints) {
                canvas.drawCircle(circlePoint.x, circlePoint.y, signInBallRadio, signInBgPaint);
            }
        }
    }


    private void drawSignInPbRect(Canvas canvas) {
        if (isNeedReturn()) {
            return;
        }
        mCheckedPbPaint = createPaint(signInPbStartColor, 0, Paint.Style.FILL_AND_STROKE, 5);
        mRectPathMeasure.setPath(selectLinePath.get(currentSignInTag), false);
        mRectPathMeasure.getSegment(0, mSelectRectPercent * mRectPathMeasure.getLength(), mRectDest, true);
        canvas.drawPath(mRectDest, mCheckedPbPaint);
        if (!isRectAnimatorStart) {
            mCheckedCirclePaint = createPaint(signInPbStartColor, 0, Paint.Style.FILL, 0);
            Shader mShader = new LinearGradient(signInPbRectFs.get(0).centerX(), signInPbRectFs.get(0).centerY()
                    , signInPbRectFs.get(currentSignInTag).centerX(), signInPbRectFs.get(currentSignInTag).centerY()
                    , new int[]{signInPbStartColor, checkedCircleColors.get(currentSignInTag)}
                    , null
                    , Shader.TileMode.CLAMP);
            mCheckedCirclePaint.setShader(mShader);
            canvas.drawRect(currentSignInTag == viewData.size() - 1 ? signInBgRectF : signInPbRectFs.get(currentSignInTag), mCheckedCirclePaint);
        }

    }

    /**
     * 画选中了的园
     *
     * @param canvas
     */
    private void drawSignInCheck(Canvas canvas) {
        if (isNeedReturn()) {
            return;
        }
        mCheckedPbPaint = createPaint(signInPbStartColor, 0, Paint.Style.FILL, 0);
        for (int i = 0; i <= currentSignInTag; i++) {
            mCheckedPbPaint.setColor(checkedCircleColors.get(i));
            canvas.drawCircle(circlePoints.get(i).x, circlePoints.get(i).y, signInBallRadio, mCheckedPbPaint);
        }
    }

    private void drawSignOldSignIn(Canvas canvas) {
        if (isNeedReturn()) {
            return;
        }
        for (int i = 0; i < currentSignInTag; i++) {
            canvas.drawCircle(circlePoints.get(i).x, circlePoints.get(i).y, signInBallRadio, mCheckedPbPaint);

        }

    }


    private void drawTextDesc(Canvas canvas) {
        if (null != viewData && viewData.size() > 0) {
            // 最有一个是图片，就不画文字了
            for (int i = 0; i < viewData.size() - 1; i++) {
                if (i <= currentSignInTag) {
                    signInTextPaint = createPaint(Color.WHITE, signInTextSize, Paint.Style.FILL, 0);
                } else {
                    signInTextPaint = createPaint(signInTextColor, signInTextSize, Paint.Style.FILL, 0);
                }
                canvas.drawText(viewData.get(i), descPoints.get(i).x, descPoints.get(i).y, signInTextPaint);

            }
        }
    }

    private void drawBitmap(Canvas canvas) {
        if (null == bitmap || null == srcBitmap || null == desBitmap || null == signInTextPaint) {
            return;
        }
        canvas.drawBitmap(bitmap, srcBitmap, desBitmap, signInTextPaint);
    }

    private Boolean isNeedReturn() {
        return currentSignInTag < 0 || currentSignInTag >= viewData.size();
    }

    private void calcucateCirclePoints(List<String> viewData) {
        if (null != viewData && !viewData.isEmpty()) {
            //横向平分屏幕  计算每段距离大小
            int intervalSize = viewData.size() - 1; // 总共多少分线
            int onePiece = (viewWidth - signInBallRadio * 2 * viewData.size()) / intervalSize;
            circlePoints.clear();
            descPoints.clear();
            signInPbRectFs.clear();
            signInDoublePaths.clear();
            sexangleDoublePaths.clear();
            sexanglePaths.clear();
            selectLinePath.clear();
            checkedCircleColors.clear();

            for (int i = 0; i < viewData.size(); i++) {
                //每个六边形的 位置
                Point circlePoint = new Point((i) * onePiece + ((i + 1) * 2 - 1) * signInBallRadio, circleY);
                Rect rect = new Rect();
                signInTextPaint.getTextBounds(viewData.get(i), 0, viewData.get(i).length(), rect);
                int height = rect.height();//文本的高度
                //矩形的位置
                Point descPoint = new Point((int) ((i) * onePiece + ((i + 1) * 2 - 1) * signInBallRadio -
                        signInTextPaint.measureText(viewData.get(i)) / 2), circleY + height / 2);
                //签到的矩形
                RectF rectF = new RectF(0, viewHeight * SECTION_SCALE - signInBallRadio - signInRectHeight, circlePoint.x - signInBallRadio + 3,
                        viewHeight * SECTION_SCALE - signInBallRadio);
                //签到时候矩形的路径
                Path selectPath = new Path();//选择矩形
                selectPath.moveTo(i == 0 ? 0 : i * onePiece + (2 * i - 1) * signInBallRadio, circlePoint.y);
                if (i == viewData.size() - 1) {
                    selectPath.lineTo(viewWidth, circlePoint.y);
                } else {
                    selectPath.lineTo(circlePoint.x - signInBallRadio, circlePoint.y);
                }

                //没用到
                Path signInPath = new Path();
                signInPath.moveTo(circlePoint.x - signInBallRadio / 2, circlePoint.y);
                signInPath.lineTo(circlePoint.x, circlePoint.y + signInBallRadio / 2);
                signInPath.lineTo(circlePoint.x + signInBallRadio / 2, circlePoint.y - signInBallRadio + signInBallRadio / 2);

                //大的六边形 路径
                Path signInDoublePath = new Path();
                signInDoublePath.moveTo((float) (circlePoint.x - signInBallRadio * 1.75 + 10), circlePoint.y);
                signInDoublePath.lineTo((float) (circlePoint.x - signInBallRadio * 1.75 / 4), (float) (circlePoint.y + 1.65 * signInBallRadio / 2 -
                        5));
                signInDoublePath.lineTo((float) (circlePoint.x + signInBallRadio * 1.75 / 2 + 5), (float) (circlePoint.y - 1.65 * signInBallRadio /
                        3 - 5));
                signInDoublePath.lineTo((float) (circlePoint.x - signInBallRadio * 1.75 / 4), (float) (circlePoint.y + 1.65 * signInBallRadio / 4 *
                        3));
                signInDoublePath.close();

                //小得六边形
                Path sexanglePath = new Path();
                sexanglePath.moveTo(circlePoint.x - signInBallRadio, circlePoint.y);
                sexanglePath.lineTo(circlePoint.x - signInBallRadio / 2, circlePoint.y - 4 * signInBallRadio / 4);
                sexanglePath.lineTo(circlePoint.x + signInBallRadio / 2, circlePoint.y - 4 * signInBallRadio / 4);
                sexanglePath.lineTo(circlePoint.x + signInBallRadio, circlePoint.y);
                sexanglePath.lineTo(circlePoint.x + signInBallRadio / 2, circlePoint.y + 4 * signInBallRadio / 4);
                sexanglePath.lineTo(circlePoint.x - signInBallRadio / 2, circlePoint.y + 4 * signInBallRadio / 4);
                sexanglePath.close();

                // 大的六边形  做动画
                Path sexangleDoublePath = new Path();
                sexangleDoublePath.moveTo((float) (circlePoint.x - signInBallRadio * 1.75), circlePoint.y);
                sexangleDoublePath.lineTo(circlePoint.x - signInBallRadio, (float) (circlePoint.y - (1.65 * signInBallRadio)));
                sexangleDoublePath.lineTo(circlePoint.x + signInBallRadio, (float) (circlePoint.y - 1.65 * signInBallRadio));
                sexangleDoublePath.lineTo((float) (circlePoint.x + signInBallRadio * 1.75), circlePoint.y);
                sexangleDoublePath.lineTo(circlePoint.x + signInBallRadio, (float) (circlePoint.y + 1.65 * signInBallRadio));
                sexangleDoublePath.lineTo(circlePoint.x - signInBallRadio, (float) (circlePoint.y + 1.65 * signInBallRadio));
                sexangleDoublePath.close();

                // 选中颜色
                Integer chooseColor = mLinearGradientUtil.getColor((float) i / viewData.size());


                circlePoints.add(circlePoint);
                descPoints.add(descPoint);
                signInPbRectFs.add(rectF);
                signInDoublePaths.add(signInDoublePath);
                sexangleDoublePaths.add(sexangleDoublePath);
                sexanglePaths.add(sexanglePath);
                selectLinePath.add(selectPath);
                checkedCircleColors.add(chooseColor);
            }

            //设置礼物图标
            int new_H = circlePoints.get(viewData.size() - 1).y - bitmap.getHeight() / 2;
            int new_W = circlePoints.get(viewData.size() - 1).y - bitmap.getHeight() / 2;

            desBitmap = new Rect(circlePoints.get(viewData.size() - 1).x - bitmap.getWidth() / 2,
                    circlePoints.get(viewData.size() - 1).y - bitmap.getHeight() / 2,
                    circlePoints.get(viewData.size() - 1).x + bitmap.getWidth() / 2,
                    circlePoints.get(viewData.size() - 1).y + bitmap.getHeight() / 2);
        }
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int newHeight;
        //如果不是精准模式   就使用默认的高度      具体用法请百度 MeasureSpec.getMode()
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            newHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_HEIGHT, getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void initToolsAndData() {
        //存放路径 点 ....的一些集合
        circlePoints = new ArrayList<>();
        descPoints = new ArrayList<>();
        signInPbRectFs = new ArrayList<>();
        sexanglePaths = new ArrayList<>();
        signInDoublePaths = new ArrayList<>();
        sexangleDoublePaths = new ArrayList<>();
        selectLinePath = new ArrayList<>();
        checkedCircleColors = new ArrayList<>();
        //默认设置成 -1
        currentSignInTag = -1;

        //初始化  画笔  抽取一个工具类
        signInBgPaint = createPaint(signInBgColor, 0, Paint.Style.FILL, 0);
        mCheckedPbPaint = createPaint(signInPbStartColor, 0, Paint.Style.FILL, 0);
        signInTextPaint = createPaint(signInTextColor, signInTextSize, Paint.Style.FILL, 0);

        //礼物图标  使用方法 可以百度一下
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_gift);
        srcBitmap = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    /**
     * 处理图片
     *
     * @param bm 所要转换的bitmap
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    private void initAnimator() {
        mSexangleDest = new Path();
        mSexangleAnimator = ValueAnimator.ofFloat(0, 1);
        mSexangleAnimator.setInterpolator(new LinearInterpolator());
        mSexangleAnimator.setDuration(1000);
        mSexangleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSexanglePercent = (float) animation.getAnimatedValue();
                if (mSexanglePercent == 1) {
                    isAnamitorStart = false;
                    mSexangleDest.reset();
                }
                invalidate();
            }
        });

        mSelectRectAnimator = ValueAnimator.ofFloat(0, 1);
        mRectPathMeasure = new PathMeasure();
        mRectDest = new Path();
        mSelectRectAnimator.setDuration(1000);
        mSelectRectAnimator.setInterpolator(new LinearInterpolator());
        mSelectRectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSelectRectPercent = (float) animation.getAnimatedValue();
//                if (currentSignInTag == viewData.size() - 1) {
//                    if (mSelectRectPercent > 0.5) {
//                        mRectDest.reset();
//                        isRectAnimatorStart = false;
//                        isAnamitorStart = true;
//                        mSexangleAnimator.start();
//                        mSelectSignInAnimator.start();
//                    }
//                } else if (mSelectRectPercent > 0.85) {
                mRectDest.reset();
                isRectAnimatorStart = false;
                isAnamitorStart = true;
                mSexangleAnimator.start();
                mSelectSignInAnimator.start();
//                }
                invalidate();
            }
        });


        mSignInMeasure = new PathMeasure();
        mSignInDest = new Path();
        mSelectSignInAnimator = ValueAnimator.ofFloat(0, 1);
        mSelectSignInAnimator.setDuration(1000);
        mSelectSignInAnimator.setInterpolator(new LinearInterpolator());
        mSelectSignInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSelectSignInPercent = (float) animation.getAnimatedValue();
                if (mSelectSignInPercent >= 1) {
                    isSignInStaer = false;
                    mSignInDest.reset();
                }
                invalidate();
            }
        });

    }

    private Paint createPaint(int paintColor, int textSize, Paint.Style style, int linWidth) {
        Paint p = new Paint();
        p.setColor(paintColor);
        p.setAntiAlias(true);
        p.setStrokeWidth(linWidth);
        p.setDither(true);
        p.setTextSize(textSize);
        p.setStyle(style);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeJoin(Paint.Join.ROUND);
        return p;
    }


    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        //参数2   定义的颜色 字体,    参数 4    默认字体颜色 字体大小
        //参数2  和参数4 的值 需要一一对应 要不然报错.....
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Signin, defStyleAttr, R.style.def_checkinView);
        int indexCount = typedArray.getIndexCount();
        //循环  如果有值则赋值   没有 则使用默认的
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.Signin_sign_in_bg_color:
                    signInBgColor = typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.Signin_sign_in_pb_start_color:
                    signInPbStartColor = typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.Signin_sign_in_pb_end_color:
                    signInPbEndColor = typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.Signin_sign_in_text_color:
                    signInTextColor = typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.Signin_sign_in_text_siz:
                    signInTextSize = typedArray.getDimensionPixelSize(index, 0);
                    break;
                default:
            }
        }
        typedArray.recycle();
    }

    /*******************************************  操作  *********************************************/


    public void setSignInData(List<String> data) {
        if (null != data) {
            viewData = data;
        }
        //计算各个点 图形的位置
        calcucateCirclePoints(viewData);
    }

    public void setSignInEvent() {
        currentSignInTag++;
        if (currentSignInTag >= viewData.size()) {
            return;
        }
        isRectAnimatorStart = true;
        mSexangleDest.reset();
        mRectDest.reset();
        mSignInDest.reset();
        mSelectRectAnimator.start();
    }

    public void setSignInClear() {
        currentSignInTag = -1;
        mRectDest.reset();
        mSexangleDest.reset();
        mSignInDest.reset();
        invalidate();
    }

    public void setCurretn(int i) {
        currentSignInTag = i - 1;
        if (currentSignInTag >= viewData.size() || currentSignInTag < 0) {
            return;
        }
        mSignInDest.reset();
        mRectDest.reset();
        mSexangleDest.reset();
        invalidate();
    }
}














