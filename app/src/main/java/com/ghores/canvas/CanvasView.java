package com.ghores.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class CanvasView extends ImageView {
    private Paint strokePaint;
    private Paint arcStrokePaint;
    private Paint fillPaint;
    private Paint textPaint;

    private Thread physicThread;
    private Thread renderThread;
    private int offset1 = 0;
    private int offset2 = 0;
    private int offset3 = 0;
    private int offset4 = 0;

    public CanvasView(Context context) {
        super(context);
        initialize();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        strokePaint = new Paint();
        strokePaint.setColor(Color.parseColor("#ffffff"));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(2);
        strokePaint.setAntiAlias(true);

        fillPaint = new Paint();
        fillPaint.setColor(Color.parseColor("#888888"));
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);

        arcStrokePaint = new Paint();
        arcStrokePaint.setColor(Color.parseColor("#ffffff"));
        arcStrokePaint.setStyle(Paint.Style.STROKE);
        arcStrokePaint.setStrokeWidth(10);
        arcStrokePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#000000"));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(20);

        physicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        offset1 = (int) (Math.cos((double) System.currentTimeMillis() / 1000.0) * 50);
                        offset2 = (int) (Math.sin((double) System.currentTimeMillis() / 500.0) * 50);
                        offset3 = (int) (Math.cos((double) System.currentTimeMillis() / 200.0) * 50);
                        offset4 = (int) (Math.sin((double) System.currentTimeMillis() / 2000.0) * 50);
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        renderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(25);
                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        physicThread.start();
        renderThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        fillPaint.setColor(Color.rgb(0, 200 + Math.abs(offset1), 200 + Math.abs(offset4)));

        canvas.drawCircle(centerX + offset1, centerY + offset2, 100, fillPaint);
        canvas.drawCircle(centerX + offset2, centerY + offset1, 100, strokePaint);

        arcStrokePaint.setColor(Color.rgb(200 + Math.abs(offset1), 200 + Math.abs(offset2), 0));
        int radius = 100;
        RectF rect = new RectF(centerX - radius + offset3, centerY - radius + offset4, centerX + radius + offset3, centerY + radius + offset4);
        canvas.drawArc(rect, 90 + offset1, 270 + offset4, false, arcStrokePaint);

        canvas.drawRect(rect, strokePaint);
        canvas.drawText("Hello Canvas", centerX + offset1, centerY + offset4, textPaint);
    }
}
