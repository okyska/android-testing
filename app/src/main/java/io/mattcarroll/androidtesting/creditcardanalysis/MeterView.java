package io.mattcarroll.androidtesting.creditcardanalysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Draws a horizontal meter that can be filled from 0% to 100%.
 */
public class MeterView extends View {

    private float meterPercentage = 0.5f;
    private float cornerRadius = 0;

    private Paint backgroundPaint;
    private Paint fillPaint;
    private RectF backgroundRectF;
    private RectF fillRectF;

    public MeterView(Context context) {
        this(context, null);
    }

    public MeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(0xFFDDDDDD);
        backgroundRectF = new RectF();

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(0xFF66FF66);
        fillRectF = new RectF();
    }

    public void setMeterPercentage(float percentage) {
        meterPercentage = percentage;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        cornerRadius = getMeasuredHeight() / 2;

        backgroundRectF.right = getMeasuredWidth();
        backgroundRectF.bottom = getMeasuredHeight();

        fillRectF.right = getFillWidth();
        fillRectF.bottom = getMeasuredHeight();

        fillPaint.setShader(new LinearGradient(0, 0, getFillWidth(), 0, 0xFF008800, 0xFFCCFFCC, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint);
        canvas.drawRoundRect(fillRectF, cornerRadius, cornerRadius, fillPaint);
    }

    private float getFillWidth() {
        return getWidth() * meterPercentage;
    }
}
