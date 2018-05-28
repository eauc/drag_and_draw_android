package com.bignerdranch.android.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

public class Box implements Parcelable {
    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF origin) {
        mOrigin = origin;
    }

    private Box(Parcel in) {
        mOrigin.readFromParcel(in);
        mCurrent.readFromParcel(in);
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        mOrigin.writeToParcel(dest, flags);
        mCurrent.writeToParcel(dest, flags);
    }

    public static final Parcelable.Creator<Box> CREATOR = new Parcelable.Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel source) {
            Box b = new Box(source);
            return b;
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };
}
