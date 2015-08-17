package test.com.testview;

/**
 * Created by tong on 15/8/17.
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Number extends View {

    private static String TAG = Number.class.getSimpleName();
    private Context mContext;
    private int mNumber = 0;
    private Paint mForagePaint;
    private Paint mBackgroundPaint;
    private RectF mViewSize;

    private int mInterval = 30;

    public void setInterval(int interval) {
        mInterval = interval;
    }

    private int mTopMargin = 10;

    public void setTopMargin(int topMargin) {
        mTopMargin = topMargin;
    }

    private int mBottomMargin = 10;

    public void setBottomMargin(int bottomMargin) {
        mBottomMargin = bottomMargin;
    }

    private int mLeftMargin = 10;

    public void setLeftMargin(int leftMargin) {
        mLeftMargin = leftMargin;
    }

    private int mRightMargin = 10;

    public void setRightMargin(int rightMargin) {
        mRightMargin = rightMargin;
    }

    public void setMargin(int margin) {
        mTopMargin = mBottomMargin = mLeftMargin = mRightMargin = margin;
    }

    private volatile boolean runningDrawThread = true;

    private NumberStruct mNumberStruct;

    public int mDisplayNumber = 1234;

    public Number(Context context) {
        this(context, null);
    }

    public Number(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Number(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mViewSize = new RectF();
        mForagePaint = new Paint();
        mBackgroundPaint = new Paint();
        mForagePaint.setARGB(255, 250, 166, 83);
        mBackgroundPaint.setARGB(255, 76, 45, 34);
        setBackgroundColor(Color.rgb(36, 30, 29));
        mNumberStruct = NumberStruct.setRequireData(mViewSize, 0);
    }

    public void setBackgroundPaint(Paint p) {
        mBackgroundPaint = p;
    }

    public void setForagePaint(Paint p) {
        mForagePaint = p;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public void setPaintPixel(int px) {
        mNumberStruct.setPx(px);
    }

    private List<Integer> getNumChar(int number) {
        List<Integer> listInt = new ArrayList<Integer>();
        if (number > 99999) {
            Log.e(TAG, "the " + number + "is too big");
            for (int i = 0; i < 5; i++)
                listInt.add(9);
            return listInt;
        }
        int tSplit = 1;
        int remainder = 0;
        for (int i = 0; i < 5; ++i) {
            remainder = number % tSplit;
            tSplit *= 10;
            listInt.add((number % tSplit - remainder) / (tSplit / 10));
        }
        return listInt;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println(widthMeasureSpec + "X" + heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("Width:" + getWidth());
        int tWidth = getWidth() - mLeftMargin - mRightMargin;
        int tHeight = getHeight() - mTopMargin - mBottomMargin;
        int size = tWidth / 5;
        List<Integer> list = getNumChar(mDisplayNumber++);
        for (int i = list.size(); i > 0; i--) {
            mNumberStruct.setDrawPosition(
                    (list.size() - i) * size + mLeftMargin + mInterval,
                    mTopMargin, size - mInterval, tHeight);
            mNumberStruct.drawCurrentNumber(canvas, mForagePaint, mBackgroundPaint,
                    list.get(i - 1) == 0 ? (i > Integer.toString(mDisplayNumber).length() ? 10 : 0) : list.get(i - 1));
        }

    }

}

class NumberStruct {
    private RectDraw mLeftTopRect;
    private RectDraw mLeftBottomRect;
    private RectDraw mRightTopRect;
    private RectDraw mRightBottomRect;
    private RectDraw mTopRect;
    private RectDraw mMidRect;
    private RectDraw mBottomRect;

    private float mPx = 5; // paint width

    public void setPx(int px) {
        mPx = px;
    }

    class RectDraw {
        private RectF mRect;
        private boolean isDraw = true;// 默认显示

        public RectDraw(RectF r) {
            mRect = new RectF(r);
        }

        public void setRect(RectF r) {
            mRect = new RectF(r);
        }

        public RectF getRect() {
            return mRect;
        }

        public void setDraw(boolean draw) {
            isDraw = draw;
        }

        public boolean isDraw() {
            return isDraw;
        }
    }

    private float mX;
    private float mY;
    private float mWidth;
    private float mHeight;

    private RectF mTempRectF;

    private NumberStruct(RectF sRect, int number) {
        mX = sRect.left;
        mY = sRect.top;
        mWidth = sRect.right;
        mHeight = sRect.bottom;
        mTempRectF = new RectF();
// init();
    }

    private void init() {
        float mid = (mHeight) / 2;
        mTempRectF.set(mX + mPx, mY, mX + mWidth - mPx, mY + mPx); // top
        mTopRect = new RectDraw(mTempRectF);
        mTempRectF.set(mX, mY + mPx, mX + mPx, mY + mid - mPx / 2); // top left
        mLeftTopRect = new RectDraw(mTempRectF);
        mTempRectF.set(mX + mWidth - mPx, mY + mPx, mX + mWidth, mY + mid - mPx / 2); // right top
        mRightTopRect = new RectDraw(mTempRectF);
        mTempRectF.set(mX + mPx, mY + mid - mPx / 2, mX + mWidth - mPx, mY + mid + mPx / 2); // middle
        mMidRect = new RectDraw(mTempRectF);
        mTempRectF.set(mX, mY + mid + mPx / 2, mX + mPx, mY + mHeight - mPx); // left bottom
        mLeftBottomRect = new RectDraw(mTempRectF);
        mTempRectF.set(mX + mWidth - mPx, mY + mid + mPx / 2, mX + mWidth, mY + mHeight - mPx); // right bottom
        mRightBottomRect = new RectDraw(mTempRectF);
        mTempRectF.set(mX + mPx, mY + mHeight - mPx, mX + mWidth - mPx, mY + mHeight); // bottom
        mBottomRect = new RectDraw(mTempRectF);
    }

    public static NumberStruct setRequireData(RectF sRect, int number) {
        NumberStruct numberStruct = new NumberStruct(sRect, number);
        return numberStruct;
    }

    public void setDrawPosition(int left, int top, int right, int bottom) {
        mX = left;
        mY = top;
        mWidth = right;
        mHeight = bottom;
        init();
    }

    public void drawCurrentNumber(Canvas c, Paint foragePaint, Paint backgroundPaint, int num) {
        calcRectNumber(num);
        int r = 6;
        c.drawRoundRect(mLeftTopRect.getRect(), r, r, mLeftTopRect.isDraw() ? foragePaint : backgroundPaint);
        c.drawRoundRect(mLeftBottomRect.getRect(), r, r, mLeftBottomRect.isDraw() ? foragePaint : backgroundPaint);
        c.drawRoundRect(mRightTopRect.getRect(), r, r, mRightTopRect.isDraw() ? foragePaint : backgroundPaint);
        c.drawRoundRect(mRightBottomRect.getRect(), r, r, mRightBottomRect.isDraw() ? foragePaint : backgroundPaint);
        c.drawRoundRect(mTopRect.getRect(), r, r, mTopRect.isDraw() ? foragePaint : backgroundPaint);
        c.drawRoundRect(mMidRect.getRect(), r, r, mMidRect.isDraw() ? foragePaint : backgroundPaint);
        c.drawRoundRect(mBottomRect.getRect(), r, r, mBottomRect.isDraw() ? foragePaint : backgroundPaint);
    }

    private void calcRectNumber(int number) {

        switch (number) {
            case 10:
                mLeftTopRect.setDraw(false);
                mLeftBottomRect.setDraw(false);
                mRightTopRect.setDraw(false);
                mRightBottomRect.setDraw(false);
                mTopRect.setDraw(false);
                mMidRect.setDraw(false);
                mBottomRect.setDraw(false);
                break;
            case 0:
                mLeftTopRect.setDraw(true);
                mLeftBottomRect.setDraw(true);
                mRightTopRect.setDraw(true);
                mRightBottomRect.setDraw(true);
                mTopRect.setDraw(true);
                mMidRect.setDraw(false);
                mBottomRect.setDraw(true);
                break;
            case 1:
                mLeftTopRect.setDraw(false);
                mLeftBottomRect.setDraw(false);
                mRightTopRect.setDraw(true);
                mRightBottomRect.setDraw(true);
                mTopRect.setDraw(false);
                mMidRect.setDraw(false);
                mBottomRect.setDraw(false);
                break;
            case 2:
                mLeftTopRect.setDraw(false);
                mLeftBottomRect.setDraw(true);
                mRightTopRect.setDraw(true);
                mRightBottomRect.setDraw(false);
                mTopRect.setDraw(true);
                mMidRect.setDraw(true);
                mBottomRect.setDraw(true);
                break;
            case 3:
                mLeftTopRect.setDraw(false);
                mLeftBottomRect.setDraw(false);
                mRightTopRect.setDraw(true);
                mRightBottomRect.setDraw(true);
                mTopRect.setDraw(true);
                mMidRect.setDraw(true);
                mBottomRect.setDraw(true);
                break;
            case 4:
                mLeftTopRect.setDraw(true);
                mLeftBottomRect.setDraw(false);
                mRightTopRect.setDraw(true);
                mRightBottomRect.setDraw(true);
                mTopRect.setDraw(false);
                mMidRect.setDraw(true);
                mBottomRect.setDraw(false);
                break;
            case 5:
                mLeftTopRect.setDraw(true);
                mLeftBottomRect.setDraw(false);
                mRightTopRect.setDraw(false);
                mRightBottomRect.setDraw(true);
                mTopRect.setDraw(true);
                mMidRect.setDraw(true);
                mBottomRect.setDraw(true);
                break;
            case 6:
                mLeftTopRect.setDraw(true);
                mLeftBottomRect.setDraw(true);
                mRightTopRect.setDraw(false);
                mRightBottomRect.setDraw(true);
                mTopRect.setDraw(true);
                mMidRect.setDraw(true);
                mBottomRect.setDraw(true);
                break;
            case 7:
                mLeftTopRect.setDraw(false);
                mLeftBottomRect.setDraw(false);
                mRightTopRect.setDraw(true);
                mRightBottomRect.setDraw(true);
                mTopRect.setDraw(true);
                mMidRect.setDraw(false);
                mBottomRect.setDraw(false);
                break;
            case 8:
                mLeftTopRect.setDraw(true);
                mLeftBottomRect.setDraw(true);
                mRightTopRect.setDraw(true);
                mRightBottomRect.setDraw(true);
                mTopRect.setDraw(true);
                mMidRect.setDraw(true);
                mBottomRect.setDraw(true);
                break;
            case 9:
                mLeftTopRect.setDraw(true);
                mLeftBottomRect.setDraw(false);
                mRightTopRect.setDraw(true);
                mRightBottomRect.setDraw(true);
                mTopRect.setDraw(true);
                mMidRect.setDraw(true);
                mBottomRect.setDraw(true);
                break;
        }
    }

}