package com.gary.chemmaster.entity;

import android.graphics.PointF;

/**
 * Created by gary on 16/12/5.
 */
public class CYLChemBond {

   public PointF startPoint;
   public PointF endPoint;
   public PointF midPoint;

    public CYLChemBond() {
    }

    public CYLChemBond(PointF startPoint, PointF endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.midPoint = new PointF((startPoint.x + endPoint.x)/2, (startPoint.y + endPoint.y)/2);
    }

    public void setStartPoint(PointF startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(PointF endPoint) {
        this.endPoint = endPoint;
    }

    public void setMidPoint(PointF midPoint) {
        this.midPoint = midPoint;
    }

    @Override
    public String toString() {
        return "CYLChemBond{" +
                "startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", midPoint=" + midPoint +
                '}';
    }
}
