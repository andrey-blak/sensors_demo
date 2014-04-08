package blak.android.sensors.movement;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MovementView extends View {
    private float mX;
    private float mY;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public MovementView(Context context) {
        super(context);
        init();
    }

    public MovementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovementView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.BLACK);
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

        float rate = radiusCompass / 4;
        float stopX = width + mX * rate;
        float stopY = height - mY * rate;
        canvas.drawLine(width, height, stopX, stopY, mPaint);
    }

    public void updateDirection(float x, float y) {
        mX = x;
        mY = y;
        invalidate();
    }
}
