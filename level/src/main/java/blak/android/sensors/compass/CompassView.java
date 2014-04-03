package blak.android.sensors.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {
    private double mDirection;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CompassView(Context context) {
        super(context);
        init();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth() / 2;
        int height = getMeasuredHeight() / 2;

        float radiusCompass = (float) (Math.min(width, height) * 0.9);
        canvas.drawCircle(width, height, radiusCompass, mPaint);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        double radians = Math.toRadians(-mDirection);
        float stopX = (float) (width + radiusCompass * Math.sin(radians));
        float stopY = (float) (height - radiusCompass * Math.cos(radians));
        canvas.drawLine(width, height, stopX, stopY, mPaint);

        String text = String.format("%.1f", mDirection);
        canvas.drawText(text, width, height, mPaint);

    }

    public void updateDirection(double dir) {
        mDirection = dir;
        invalidate();
    }
}
