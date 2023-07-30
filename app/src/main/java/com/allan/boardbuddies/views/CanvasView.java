package com.allan.boardbuddies.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.allan.boardbuddies.models.Stroke;

import java.util.ArrayList;

public class CanvasView extends View{
    private static final float TOUCH_TOLERANCE = 4;
    private Paint paint = new Paint();
    private Paint bitmapPaint = new Paint(Paint.DITHER_FLAG);
    private Bitmap bitmap;
    private Canvas canvas;
    private ArrayList<Stroke> paths = new ArrayList<>();
    private float currX, currY;
    private Path path;
    private int strokeColor;
    private int strokeWidth;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAlpha(0xff);
    }

    public void init(int width, int height) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        this.strokeColor = Color.RED;
        this.strokeWidth = 10;
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.save();
        canvas.drawColor(Color.WHITE);
        for (Stroke stroke : paths) {
            paint.setColor(stroke.getColor());
            paint.setStrokeWidth(stroke.getWidth());
            canvas.drawPath(stroke.getPath(), paint);
        }

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDraw(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                continueDraw(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void startDraw(float x, float y) {
        path = new Path();
        Stroke stroke = new Stroke(strokeColor, strokeWidth, path);
        paths.add(stroke);
        path.moveTo(x, y);
        invalidate();
        currX = x;
        currY = y;
    }

    private void continueDraw(float x, float y) {
        float dx = Math.abs(x - currX);
        float dy = Math.abs(y - currY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) { //checking if the touch has moved a significant amount (defined by touch_tolerance)
            path.quadTo(currX, currY, (x + currX) / 2, (y + currY) / 2);
            invalidate();
            currX = x;
            currY = y;
        }
    }
}
