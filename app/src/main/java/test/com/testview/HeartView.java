package test.com.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by tong on 15/8/17.
 */
public class HeartView extends View {
    private static final String TAG = "HeartView";

    private Paint mPaint;

    private float mX;
    private float mY;
    private Path mPath;

    private float[] yData = new float[]{200f, 170f, 145f, 125f, 110f, 125f, 145f, 170f, 200f, 230f, 255f, 275f,
            290f, 275f, 255f, 230f, 200f, 190f, 170f, 190f, 200f};

    private ArrayList<Point> mPointList = new ArrayList<>();

    static class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mX = 0.0f;
        mY = 200.0f;
        mPath = new Path();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mX += 5.0f;

                    if (mX > getWidth()) {
                        mX = 0.0f;
                        mPointList.clear();
                    }

                    if (mX > 99.0f && mX < 200.1f) {
                        mY = yData[Math.round((mX - 100.0f) / 5)];
                    } else if (mX > 299.0f && mX < 400.0f) {
                        mY = yData[Math.round((mX - 300.0f) / 5)];
                    } else {
                        mY = 200.0f;
                    }

                    mPointList.add(new Point(mX, mY));
                    try {
                        Thread.currentThread().sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }

            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPath.reset();
        mPath.moveTo(0, 200);
        for (int i = 0; i < mPointList.size(); i++) {
            Point point = mPointList.get(i);
            mPath.lineTo(point.x, point.y);
        }
//        mPath.close();
        mPaint.setStrokeWidth(2);
        canvas.drawPath(mPath, mPaint);
        mPaint.setStrokeWidth(3);
        canvas.drawCircle(mX, mY, 2, mPaint);

    }
}
