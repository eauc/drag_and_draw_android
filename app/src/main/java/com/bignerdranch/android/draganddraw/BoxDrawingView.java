package com.bignerdranch.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BOX_DRAWING_VIEW";
    private static final String STATE_PARENT_KEY = "parent";
    private static final String STATE_BOXEN_KEY = "boxen";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            {
                action= "ACTION_DOWN";
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                action= "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                action= "ACTION_UP";
                mCurrentBox = null;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            {
                action= "ACTION_CANCEL";
                mCurrentBox = null;
                break;
            }
        }
        Log.i(TAG, action + " at X=" + current.x + ", y=" + current.y);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parent = super.onSaveInstanceState();

        Bundle state = new Bundle();
        state.putParcelable(STATE_PARENT_KEY, parent);
        state.putParcelableArray(STATE_BOXEN_KEY, mBoxen.toArray(new Box[mBoxen.size()]));

        Log.i(TAG, "Save state");
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state != null && state instanceof Bundle) {
            Log.i(TAG, "Restore state");
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(STATE_PARENT_KEY));
            Box[] boxen = (Box[]) bundle.getParcelableArray(STATE_BOXEN_KEY);
            mBoxen = new ArrayList<>(Arrays.asList(boxen));
        } else {
            Log.i(TAG, "Restore state (super)");
            super.onRestoreInstanceState(state);
        }
    }
}
