package com.gary.chemmaster.entity;

import android.graphics.PointF;

/**
 * Created by gary on 16/12/7.
 */
public class CYLTripleBond extends CYLChemBond {

    public PointF startPoint2;
    public PointF endPoint2;

    public PointF startPoint3;
    public PointF endPoint3;

    public CYLTripleBond(PointF startPoint, PointF endPoint) {
        super(startPoint, endPoint);
    }

    public void setStartPoint2(PointF startPoint2) {
        this.startPoint2 = startPoint2;
    }

    public void setEndPoint2(PointF endPoint2) {
        this.endPoint2 = endPoint2;
    }

    public void setStartPoint3(PointF startPoint3) {
        this.startPoint3 = startPoint3;
    }

    public void setEndPoint3(PointF endPoint3) {
        this.endPoint3 = endPoint3;
    }

    @Override
    public String toString() {
        return "CYLTripleBond{" +
                "startPoint2=" + startPoint2 +
                ", endPoint2=" + endPoint2 +
                ", startPoint3=" + startPoint3 +
                ", endPoint3=" + endPoint3 +
                '}';
    }
}
