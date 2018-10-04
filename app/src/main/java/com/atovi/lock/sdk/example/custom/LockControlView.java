package com.atovi.locksdk.example.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.atovi.locksdk.example.R;
import com.atovi.lock.sdk.constant.LockStatus;

public class LockControlView extends View {

    private static final double DEFAULT_ANGLE = 2.79253;
    private static int OUT_STROKE_SIZE;
    private static int DOT_SIZE;
    private static float IN_STROKE_SIZE = 2;
    Handler mHandler = new Handler();
    private LockStatus mStatus = LockStatus.DISCONNECT;
    private Paint mOutCirclePaint;
    private Paint mInCirclePaint;
    private Paint mSmallCirclePaint;
    private Paint mPointCirclePaint;
    private float mCircleRadius;
    private float mWidthCenter;
    private float mHeightCenter;
    private float mWidth;
    private float mHeight;
    private int mTickConnectTime = 0;
    Runnable mTickConnect = new Runnable() {
        public void run() {
            mTickConnectTime += 1;
            invalidate();
            mHandler.postDelayed(this, 200); // 20ms == 60fps
        }
    };
    private double mCurrentAngle = DEFAULT_ANGLE;
    Runnable mTickControl = new Runnable() {
        public void run() {
            if (mStatus == LockStatus.UNLOCKING) {
                mCurrentAngle -= 0.12;
            }
            if (mStatus == LockStatus.LOCKING) {
                mCurrentAngle += 0.12;
            }
            invalidate();
            mHandler.postDelayed(this, 15); // 20ms == 60fps
        }
    };
    private float mCurrentPointX;
    private float mCurrentPointY;
    private RectF oval;
    private RectF roofOval;
    private Context mContext;
    private Paint mDot1;
    private Paint mDot2;
    private Paint mDot3;

    public LockControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    private void init(Context context) {
        mOutCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutCirclePaint.setStyle(Paint.Style.STROKE);
        mOutCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mInCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInCirclePaint.setStyle(Paint.Style.STROKE);
        mInCirclePaint.setStrokeWidth(IN_STROKE_SIZE);
        mInCirclePaint.setColor(ContextCompat.getColor(context, R.color.circle_in));

        mSmallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSmallCirclePaint.setColor(ContextCompat.getColor(context, R.color.circle_small));

        mPointCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mDot1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDot1.setStyle(Paint.Style.FILL);
        mDot1.setAntiAlias(true);

        mDot2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDot2.setStyle(Paint.Style.FILL);
        mDot2.setAntiAlias(true);

        mDot3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDot3.setStyle(Paint.Style.FILL);
        mDot3.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int color;
        double factor;
        switch (mStatus) {
            case DISCONNECT:
                color = R.color.circle_disable;
                factor = Math.sqrt(3) / 2;
                break;
            case CONNECTING:
                color = R.color.circle_disable;
                factor = Math.sqrt(3) / 2;
                break;
            case UNLOCK:
                color = R.color.primary;
                factor = 1;
                break;
            case LOCK:
                color = R.color.red;
                factor = Math.sqrt(3) / 2;
                break;
            case UNLOCKING:
                color = R.color.primary;
                factor = 1;
                break;
            case LOCKING:
                color = R.color.red;
                factor = Math.sqrt(3) / 2;
                break;
            default:
                color = R.color.circle_disable;
                factor = 1;
                break;
        }

        mOutCirclePaint.setStrokeWidth(OUT_STROKE_SIZE);
        mOutCirclePaint.setColor(ContextCompat.getColor(mContext, color));
        mPointCirclePaint.setColor(ContextCompat.getColor(mContext, color));
        canvas.drawCircle(mWidthCenter, mHeightCenter, mCircleRadius - OUT_STROKE_SIZE,
                mInCirclePaint);
        canvas.drawArc(oval, -130, 300, false, mOutCirclePaint);

        roofOval.set(mWidthCenter - (mCircleRadius - OUT_STROKE_SIZE) / 2,
                (float) ((mHeightCenter - mCircleRadius * factor)
                        - (mCircleRadius - OUT_STROKE_SIZE) / 2),
                mWidthCenter + (mCircleRadius - OUT_STROKE_SIZE) / 2,
                (float) ((mHeightCenter - mCircleRadius * factor)
                        + (mCircleRadius - OUT_STROKE_SIZE) / 2));

        canvas.drawArc(roofOval, -180, 180, false, mOutCirclePaint);
        canvas.drawCircle(mWidthCenter, mHeightCenter,
                (mCircleRadius - OUT_STROKE_SIZE) * 5 / 6, mSmallCirclePaint);

        if (mStatus == LockStatus.CONNECTING) {
            mDot1.setColor(ContextCompat.getColor(mContext, R.color.circle_disable));
            mDot2.setColor(ContextCompat.getColor(mContext, R.color.circle_disable));
            mDot3.setColor(ContextCompat.getColor(mContext, R.color.circle_disable));
            switch (mTickConnectTime % 3) {
                case 0:
                    mDot3.setColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case 1:
                    mDot1.setColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case 2:
                    mDot2.setColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
            }
            canvas.drawCircle(mWidthCenter - DOT_SIZE * 7 / 2, mHeightCenter, DOT_SIZE, mDot1);
            canvas.drawCircle(mWidthCenter, mHeightCenter, DOT_SIZE, mDot2);
            canvas.drawCircle(mWidthCenter + DOT_SIZE * 7 / 2, mHeightCenter, DOT_SIZE, mDot3);
        } else if (mStatus == LockStatus.DISCONNECT) {

        } else {
            float r = (mCircleRadius - OUT_STROKE_SIZE) * 2 / 3;
            mCurrentPointX = (float) (mWidthCenter + r * Math.cos(mCurrentAngle));
            mCurrentPointY = (float) (mHeightCenter + r * Math.sin(mCurrentAngle));
            canvas.drawCircle(mCurrentPointX, mCurrentPointY, OUT_STROKE_SIZE * 4 / 5,
                    mPointCirclePaint);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        OUT_STROKE_SIZE = Math.min(width, height) / 45;
        DOT_SIZE = Math.min(width, height) / 45;
        int maxEdge = Math.min(width, height * 4 / 5 - OUT_STROKE_SIZE);
        setMeasuredDimension(maxEdge, maxEdge * 5 / 4 + OUT_STROKE_SIZE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        mWidth = (float) w - xpad;
        mHeight = (float) h - ypad;

        // Figure out how big we can make the pie.
        mCircleRadius = Math.min(mWidth, mHeight) / 2;
        mWidthCenter = w / 2;
        mHeightCenter = h - w / 2;

        oval = new RectF(mWidthCenter - (mCircleRadius - OUT_STROKE_SIZE),
                mHeightCenter - (mCircleRadius - OUT_STROKE_SIZE),
                mWidthCenter + (mCircleRadius - OUT_STROKE_SIZE),
                mHeightCenter + (mCircleRadius - OUT_STROKE_SIZE));

        roofOval = new RectF();

    }

    private void startControlAnim() {
        mHandler.removeCallbacks(mTickControl);
        mHandler.post(mTickControl);
    }

    private void startConnectAnim() {
        mHandler.removeCallbacks(mTickConnect);
        mHandler.post(mTickConnect);
    }

    public void stopControlAnim() {
        mCurrentAngle = DEFAULT_ANGLE;
        mHandler.removeCallbacks(mTickControl);
    }

    public void stopConnectAnim() {
        mTickConnectTime = 0;
        mHandler.removeCallbacks(mTickConnect);
    }

    public LockStatus getStatus() {
        return this.mStatus;
    }

    public void setStatus(LockStatus status) {
        this.mStatus = status;
        switch (mStatus) {
            case DISCONNECT:
                stopControlAnim();
                stopConnectAnim();
                break;
            case CONNECTING:
                stopControlAnim();
                startConnectAnim();
                break;
            case UNLOCK:
                stopConnectAnim();
                stopControlAnim();
                break;
            case LOCK:
                stopConnectAnim();
                stopControlAnim();
                break;
            case UNLOCKING:
                stopConnectAnim();
                startControlAnim();
                break;
            case LOCKING:
                stopConnectAnim();
                startControlAnim();
                break;
            default:
                stopConnectAnim();
                stopControlAnim();
                break;
        }

        invalidate();
    }
}