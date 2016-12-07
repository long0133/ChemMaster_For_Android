package com.gary.chemmaster.entity;

import android.graphics.PointF;

/**
 * Created by gary on 16/12/6.
 */
public class CYLDoubleBond extends CYLChemBond {

    public PointF startPoint2;
    public PointF endPoint2;
    public boolean bondDirectFlag;



    public CYLDoubleBond(PointF startPoint, PointF endPoint) {
        super(startPoint, endPoint);
    }

    public CYLDoubleBond(CYLChemBond bond, PointF startPoint2, PointF endPoint2)
    {
        super(bond.startPoint,bond.endPoint);
        this.startPoint2 = startPoint2;
        this.endPoint2 = endPoint2;
    }

    public void setStartPoint2(PointF startPoint2) {
        this.startPoint2 = startPoint2;
    }

    public void setEndPoint2(PointF endPoint2) {
        this.endPoint2 = endPoint2;
    }

    public void setBondDirectFlag(boolean bondDirectFlag) {
        this.bondDirectFlag = bondDirectFlag;
    }

    @Override
    public String toString() {
        return "CYLDoubleBond{" +
                "startPoint2=" + startPoint2 +
                ", endPoint2=" + endPoint2 +
                ", bondDirectFlag=" + bondDirectFlag +
                '}';
    }
}
