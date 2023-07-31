package com.allan.boardbuddies.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.allan.boardbuddies.models.Stroke;
import com.allan.boardbuddies.models.TextBox;

import java.util.ArrayList;

public class CanvasView extends View{
    private static final float TOUCH_TOLERANCE = 4;
    private Paint paint = new Paint();
    private Paint bitmapPaint = new Paint(Paint.DITHER_FLAG);
    private Bitmap bitmap;
    private Canvas canvas;
    private ArrayList<Stroke> strokes = new ArrayList<>();
    private ArrayList<TextBox> texts = new ArrayList<>();
    private float currX, currY, offsetX, offsetY;
    private TextBox selectedBox;
    private Path path;
    private int strokeColor;
    private int strokeWidth;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAlpha(0xff);
        //paint.setTextAlign(Paint.Align.CENTER);
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
        paint.setStyle(Paint.Style.STROKE);
        for (Stroke stroke : strokes) {
            paint.setColor(stroke.getColor());
            paint.setStrokeWidth(stroke.getWidth());
            canvas.drawPath(stroke.getPath(), paint);
        }
        paint.setStyle(Paint.Style.FILL);
        for (TextBox textBox : texts) {
            paint.setTextSize(textBox.getTextSize()); // Set the text size for each item
            paint.setColor(textBox.getColor());
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            canvas.drawRect(getBoundsF(textBox), paint);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(textBox.getText(), textBox.getX(), textBox.getY(), paint);
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
                TextBox foundBox = findPressTextBox(x, y);
                if (foundBox != null){
                    selectedBox = foundBox;
                } else {
                    startDraw(x, y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (selectedBox != null){
                    touchMoveText(x,y);
                } else {
                    continueDraw(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (selectedBox != null){
                    selectedBox = null;
                } else {
                    touchUp();
                }
                break;
        }
        return true;
    }

    private void startDraw(float x, float y) {
        path = new Path();
        Stroke stroke = new Stroke(strokeColor, strokeWidth, path);
        strokes.add(stroke);
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

    private void touchUp(){
        path.lineTo(currX, currY);
        invalidate();
    }

    private void touchMoveText(float x, float y){
        selectedBox.setX(x - offsetX);
        selectedBox.setY(y + offsetY);
        invalidate();
    }

    public void newTextBox(String text){
        TextBox textBox = new TextBox(0, canvas.getHeight() / 2, text, 75, Color.BLUE);
        textBox.setX((canvas.getWidth() - getBoundsF(textBox).width()) / 2);
        texts.add(textBox);
        invalidate();
    }

    private TextBox findPressTextBox(float x, float y) {
        RectF textBoundsF;
        paint.setStyle(Paint.Style.FILL);
        for (TextBox i : texts){
            textBoundsF = getBoundsF(i);
            if (textBoundsF.contains(x, y)){
                offsetX = x - i.getX();
                offsetY = i.getY() - y;
                return i;
            }
        }
        return null;
    }

    private RectF getBoundsF(TextBox in){
        Rect textBounds = new Rect();
        paint.setTextSize(in.getTextSize());
        paint.getTextBounds(in.getText(), 0, in.getText().length(), textBounds);
        RectF textBoundsF = new RectF(textBounds);
        textBoundsF.offset(in.getX(), in.getY());
        return textBoundsF;
    }

    public ArrayList<Stroke> getStrokes(){
        return strokes;
    }

    public ArrayList<TextBox> getTextBoxes(){
        return texts;
    }
}

