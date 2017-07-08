package cjt2325.com.jprogressbarlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by cjt on 2017/7/8.
 */

public class JProgressBar extends View {

    private int color;
    private int bitmapID;

    private Paint mPaint;
    private Path mPath;
    private Path rectPath;

    private float center_X;
    private float center_Y;

    private Bitmap bitmap;

    private float radius;
    private float progress;
    private int padding;
    private int duration;

    RectF rect;

    public JProgressBar(Context context) {
        this(context, null);
    }

    public JProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        init();
    }

    //初始化参数
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPath = new Path();
        rectPath = new Path();
        progress = 0;
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.JProgressBarAttr);
        color = ta.getColor(R.styleable.JProgressBarAttr_JProgressColor, Color.RED);
        bitmapID = ta.getResourceId(R.styleable.JProgressBarAttr_JProgressSrc, R.drawable.google);
        duration = ta.getInteger(R.styleable.JProgressBarAttr_JProgressDuration, 1000);
        radius = ta.getInteger(R.styleable.JProgressBarAttr_JProgressRadius, 30);
        padding = ta.getInteger(R.styleable.JProgressBarAttr_JProgressPadding, 16);
        ta.recycle();

        Log.i("CJT", color + " == " + Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        center_X = getWidth() / 2;
        center_Y = getHeight() / 2;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制轮廓

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPath.reset();
        mPath.moveTo(radius + padding, center_Y - radius);
        rect = new RectF(padding, center_Y - radius, 2 * radius + padding, center_Y + radius);
        mPath.arcTo(rect, -90, -180, false);
        mPath.lineTo(getWidth() - radius - padding, center_Y + radius);
        rect = new RectF(getWidth() - 2 * radius - padding, center_Y - radius, getWidth() - padding, center_Y + radius);
        mPath.arcTo(rect, 90, -180, false);
        mPath.close();
        canvas.drawPath(mPath, mPaint);


        //绘制进度
        mPaint.setStyle(Paint.Style.FILL);
        mPath.reset();
        mPath.moveTo(radius + padding, center_Y - radius);
        rect = new RectF(padding, center_Y - radius, 2 * radius + padding, center_Y + radius);
        mPath.arcTo(rect, -90, -180, false);
        mPath.lineTo(getWidth() - radius - padding, center_Y + radius);
        rect = new RectF(getWidth() - 2 * radius - padding, center_Y - radius, getWidth() - padding, center_Y + radius);
        mPath.arcTo(rect, 90, -180, false);
        mPath.close();

        rectPath.reset();
        rectPath.moveTo(padding, center_Y - radius);
        rectPath.lineTo(padding, center_Y + radius);
        rectPath.lineTo(progress, center_Y + radius);
        rectPath.lineTo(progress, center_Y - radius);
        rectPath.close();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rectPath.op(mPath, Path.Op.INTERSECT);
        } else {
            canvas.clipPath(mPath, Region.Op.INTERSECT);
        }
        canvas.drawPath(rectPath, mPaint);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), bitmapID);
        }
        canvas.drawBitmap(bitmap, progress - bitmap.getWidth(), center_Y + radius - bitmap.getHeight(), new Paint());
    }

    public void setProgressWithAnimation(int progress) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, getWidthByProgress(progress));
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                JProgressBar.this.progress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public void setProgress(int progress) {
        this.progress = getWidthByProgress(progress);
        invalidate();
    }

    private float getWidthByProgress(int progress) {
        float width = getWidth() - padding;
        if (progress >= 100) {
            return width;
        } else if (progress <= 0) {
            return 0;
        } else {
            return width / 100 * progress;
        }
    }
}
