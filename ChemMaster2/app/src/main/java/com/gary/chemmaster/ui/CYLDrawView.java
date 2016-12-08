package com.gary.chemmaster.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;

import com.gary.chemmaster.CYLEnum.DrawStatus;
import com.gary.chemmaster.entity.CYLChemBond;
import com.gary.chemmaster.entity.CYLDoubleBond;
import com.gary.chemmaster.entity.CYLSingleBong;
import com.gary.chemmaster.entity.CYLTripleBond;
import com.gary.chemmaster.fragment.CYLDrawFragement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by gary on 16/12/1.
 */
public class CYLDrawView extends View {

    private Handler processHandler;
    private Paint paint;
    private float curX;
    private float curY;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float marginOfBonds = 30;
    private DrawStatus status = DrawStatus.Draw_SingleBond;
    /*化学键的数组*/
    private ArrayList<CYLChemBond> bonds;
    /*辅助图的点的集合*/
    private ArrayList<PointF> assistancePoints;
    /*化学键，点的集合*/
    private ArrayList<PointF> points;
    /*显示当前正在绘制的线条*/
    private CYLSingleBong curBond;
    /*自动粘附的距离*/
    private float autoAttachLength = 50;
    /*化学键长度*/
    private float bondLength = 200;
    /*移除的键*/
    CYLChemBond erasedBond;
    /*点击的原子*/
    RadioButton selectAtom;
    /*是否在添加原子*/
    boolean isAttachAtom;
    ArrayList<CYLChemBond> operations;

    public CYLDrawView(Context context) {
        super(context);
        bonds = new ArrayList<>();
        setPoints(new ArrayList<PointF>());
        assistancePoints = new ArrayList<>();
        operations = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public CYLDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bonds = new ArrayList<>();
        setPoints(new ArrayList<PointF>());
        assistancePoints = new ArrayList<>();
        operations = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public CYLDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bonds = new ArrayList<>();
        setPoints(new ArrayList<PointF>());
        assistancePoints = new ArrayList<>();
        operations = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        /*绘制实时线条*/
        if (curBond != null) canvas.drawLine(curBond.startPoint.x,curBond.startPoint.y,curBond.endPoint.x,curBond.endPoint.y,paint);

        /*绘制辅助线条*/
        for (PointF pointF : assistancePoints)
        {
            canvas.drawLine(startX,startY,pointF.x,pointF.y,paint);
        }

        /*绘制化学键*/
        for (CYLChemBond bond : bonds)
        {
            if (bond.getClass().equals(CYLDoubleBond.class))
            {
                CYLDoubleBond doubleBond = (CYLDoubleBond) bond;
                canvas.drawLine(doubleBond.startPoint.x,doubleBond.startPoint.y,doubleBond.endPoint.x,doubleBond.endPoint.y,paint);
                canvas.drawLine(doubleBond.startPoint2.x,doubleBond.startPoint2.y,doubleBond.endPoint2.x,doubleBond.endPoint2.y,paint);
            }
            else if (bond.getClass().equals(CYLTripleBond.class))
            {
                CYLTripleBond tripleBond = (CYLTripleBond) bond;
                canvas.drawLine(tripleBond.startPoint.x,tripleBond.startPoint.y,tripleBond.endPoint.x,tripleBond.endPoint.y,paint);
                canvas.drawLine(tripleBond.startPoint2.x,tripleBond.startPoint2.y,tripleBond.endPoint2.x,tripleBond.endPoint2.y,paint);
                canvas.drawLine(tripleBond.startPoint3.x,tripleBond.startPoint3.y,tripleBond.endPoint3.x,tripleBond.endPoint3.y,paint);
            }
            else if (bond.getClass().equals(CYLSingleBong.class))
            {
                canvas.drawLine(bond.startPoint.x,bond.startPoint.y,bond.endPoint.x,bond.endPoint.y,paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float X = event.getX();
        float Y = event.getY();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                startX = X;
                startY = Y;
               PointF downP = new PointF(X,Y);

                /*是否放置对应原子*/
                if (isAttachAtom)
                {
                    Class bondClass = CYLChemBond.class;
                    int flag = 1;
                    for (PointF point : points)
                    {
                        if (distanceFromTwoPoint(downP, point) <= bondLength/3)
                        {
                            for (CYLChemBond bond : bonds)
                            {
                                if (bond.startPoint.equals(point.x,point.y) || bond.endPoint.equals(point.x,point.y))
                                {
                                    /*单键单键*/
                                    if (bond.getClass().equals(CYLSingleBong.class) && bondClass.equals(CYLSingleBong.class)) {bondClass = CYLDoubleBond.class; flag = 2;}
                                  /*单键双键*/
                                    if (bond.getClass().equals(CYLSingleBong.class) && bondClass.equals(CYLDoubleBond.class)) {bondClass = CYLTripleBond.class; flag = 3;}
                                   /*单键*/
                                    if (bond.getClass().equals(CYLSingleBong.class) && !bondClass.equals(CYLDoubleBond.class) && !bondClass.equals(CYLTripleBond.class)) {bondClass = CYLSingleBong.class; flag = 1;}
                                    /*双键*/
                                    if (bond.getClass().equals(CYLDoubleBond.class) && !bondClass.equals(CYLTripleBond.class)) {bondClass = CYLDoubleBond.class; flag = 2;}
                                    /*三键*/
                                    if (bond.getClass().equals(CYLTripleBond.class)) {bondClass = CYLTripleBond.class;flag = 3;}

                                }
                            }
                            if (processHandler != null) Message.obtain(processHandler,CYLDrawFragement.MESSAGE_SHOW_ATOM,flag,flag,point).sendToTarget();
                        }
                    }

                }
                else
                {
                    if (status.equals(DrawStatus.Draw_SingleBond))
                    {
                        drawSingleBond(downP,MotionEvent.ACTION_DOWN);
                    }
                    else if (status.equals(DrawStatus.Draw_DoubleBond))
                    {
                        drawDoubleBond(downP);
                    }
                    else if (status.equals(DrawStatus.Draw_TripleBond))
                    {
                        drawTripleBond(downP);
                    }
                    else if (status.equals(DrawStatus.Draw_Erease))
                    {
                        eraseBond(downP);
                    }

                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (!isAttachAtom)
                {
                    curX = X;
                    curY = Y;
                    PointF curP = new PointF(curX,curY);

                    if (status.equals(DrawStatus.Draw_SingleBond))
                    {
                        drawSingleBond(curP,MotionEvent.ACTION_MOVE);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                if (!isAttachAtom)
                {
                    endX = X;
                    endY = Y;
                    PointF endP = new PointF(endX,endY);

                    if (status.equals(DrawStatus.Draw_SingleBond))
                    {
                        drawSingleBond(endP,MotionEvent.ACTION_UP);
                    }
                }
                break;
        }

        return true;
    }


    /*绘制单键*/
    private void drawSingleBond(PointF point, int EventAction)
    {
        if (EventAction == MotionEvent.ACTION_DOWN)
        {
            for (PointF pointF : points)
            {
                /*修正起始点*/
                if (distanceFromTwoPoint(pointF,point) <= autoAttachLength)
                {
                    startX = pointF.x;
                    startY = pointF.y;
                    point.set(startX,startY);
                }

            }

                /*告知界面去显示指示indicator*/
            if (processHandler != null)
                Message.obtain(processHandler, CYLDrawFragement.MESSAGE_TOUCH_DOWN,point).sendToTarget();

                /*显示辅助线条*/
            setAssistancePointsWithPoint();
        }
        else if (EventAction == MotionEvent.ACTION_MOVE)
        {
            curBond = new CYLSingleBong(new PointF(startX,startY),new PointF(curX,curY));


            /*贴紧某一点时 显示indictor*/
            for (PointF pointF : points)
            {
                if (distanceFromTwoPoint(pointF, point) <= autoAttachLength)
                {
                    if (processHandler!=null) Message.obtain(processHandler,CYLDrawFragement.MESSAGE_APPROACH_POINT,pointF).sendToTarget();
                    break;
                }
                else
                {
                    if (processHandler!=null) Message.obtain(processHandler,CYLDrawFragement.MESSAGE_DISAPPROACH_POINT).sendToTarget();
                }
            }

            for (PointF pointF : assistancePoints)
            {
                if (distanceFromTwoPoint(pointF, point) <= autoAttachLength)
                {
                    if (processHandler!=null) Message.obtain(processHandler,CYLDrawFragement.MESSAGE_APPROACH_POINT,pointF).sendToTarget();
                    break;
                }
                else
                {
                    if (processHandler!=null) Message.obtain(processHandler,CYLDrawFragement.MESSAGE_DISAPPROACH_POINT).sendToTarget();
                }
            }

            invalidate();
        }
        else if (EventAction == MotionEvent.ACTION_UP)
        {
            if (status.equals(DrawStatus.Draw_SingleBond))
            {
                PointF startP = new PointF(startX,startY);
//                point = new PointF(endX,endY);

                    /*修正end点的位置*/
                for (PointF pointF : points)
                {
                    if (distanceFromTwoPoint(pointF,point) <= autoAttachLength)
                    {
                        point = pointF;
                    }
                }

                for (PointF pointF : assistancePoints)
                {
                    if (distanceFromTwoPoint(pointF,point) <= autoAttachLength)
                    {
                        point = new PointF(pointF.x,pointF.y);
                    }
                }
                removeAssistancePoint();

                CYLSingleBong singleB = new CYLSingleBong(startP,point);
                bonds.add(singleB);

                if (!points.contains(startP))
                points.add(startP);

                if (!points.contains(point))
                points.add(point);

                invalidate();
            }


                /*告知界面除去指示indicator*/
            if (processHandler != null) {
                Message.obtain(processHandler, CYLDrawFragement.MESSAGE_TOUCH_UP).sendToTarget();
                Message.obtain(processHandler,CYLDrawFragement.MESSAGE_DISAPPROACH_POINT).sendToTarget();
            }

                /*放手时不需显示实时线条*/
            curBond = null;

        }

    }

    /*绘制双键*/
    private void drawDoubleBond(PointF point)
    {

        /*找到变化键*/
        for (int i = 0; i < bonds.size(); i ++)
        {
            CYLChemBond bond = bonds.get(i);
            if (distanceFromTwoPoint(point,bond.midPoint) <= (bondLength)/2)
            {
                /*创建双键*/
                CYLDoubleBond doubleBond = new CYLDoubleBond(bond.startPoint,bond.endPoint);
                if (bond.getClass().equals(CYLDoubleBond.class)) doubleBond = (CYLDoubleBond) bond;

                float spx,spy,edx,edy;

                /*计算斜率*/
                float angle = (float) Math.atan((bond.endPoint.y - bond.startPoint.y)/(bond.endPoint.x
                 - bond.startPoint.x));

                float distance = bondLength/4 * 1;

                if (angle < 0)
                {
                    if (doubleBond.bondDirectFlag)
                    {
                        /************************1.绘制另一条与原键平行的等长的先***********************/
                        spx = (float)(bond.startPoint.x - marginOfBonds * Math.sin((Math.PI - angle)));
                        spy = (float) (bond.startPoint.y - marginOfBonds * Math.cos((Math.PI - angle)));

                        edx = (float) (bond.endPoint.x - marginOfBonds * Math.sin((Math.PI - angle)));
                        edy = (float) (bond.endPoint.y - marginOfBonds * Math.cos((Math.PI - angle)));

                        doubleBond.setBondDirectFlag(!doubleBond.bondDirectFlag);
                    }
                    else
                    {
                        /************************1.绘制另一条与原键平行的等长的先***********************/


                        spx = (float) (bond.startPoint.x + marginOfBonds * Math.sin((Math.PI - angle)));
                        spy = (float) (bond.startPoint.y + marginOfBonds * Math.cos((Math.PI - angle)));

                        edx = (float) (bond.endPoint.x + marginOfBonds * Math.sin((Math.PI - angle)));
                        edy = (float) (bond.endPoint.y + marginOfBonds * Math.cos((Math.PI - angle)));

                        doubleBond.setBondDirectFlag(!doubleBond.bondDirectFlag);
                    }

                    /************************2.将此条线缩短一定距离***********************/
                    //新线段的中心点
                    PointF center = new PointF((spx + edx)/2, (spy + edy)/2);

                    edx = (float) (center.x - distance * Math.cos(Math.PI - angle));
                    edy = (float) (center.y + distance * Math.sin(Math.PI - angle));

                    spx = (float) (center.x + distance * Math.cos(Math.PI - angle));
                    spy = (float) (center.y - distance * Math.sin(Math.PI - angle));

                    doubleBond.setStartPoint2(new PointF(spx,spy));
                    doubleBond.setEndPoint2(new PointF(edx,edy));
                }//if
                else
                /*斜率小于90*/
                {
                    if (doubleBond.bondDirectFlag)
                    {
                        /************************1.绘制另一条与原键平行的等长的先***********************/
                        spx = (float)(bond.startPoint.x - marginOfBonds * Math.sin((angle)));
                        spy = (float) (bond.startPoint.y + marginOfBonds * Math.cos((angle)));

                        edx = (float) (bond.endPoint.x - marginOfBonds * Math.sin((angle)));
                        edy = (float) (bond.endPoint.y + marginOfBonds * Math.cos((angle)));

                        doubleBond.setBondDirectFlag(!doubleBond.bondDirectFlag);
                    }
                    else
                    {
                        /************************1.绘制另一条与原键平行的等长的先***********************/


                        spx = (float) (bond.startPoint.x + marginOfBonds * Math.sin((angle)));
                        spy = (float) (bond.startPoint.y - marginOfBonds * Math.cos(( angle)));

                        edx = (float) (bond.endPoint.x + marginOfBonds * Math.sin((angle)));
                        edy = (float) (bond.endPoint.y - marginOfBonds * Math.cos((angle)));

                        doubleBond.setBondDirectFlag(!doubleBond.bondDirectFlag);
                    }

                    /************************2.将此条线缩短一定距离***********************/
                    //新线段的中心点
                    PointF center = new PointF((spx + edx)/2, (spy + edy)/2);

                    edx = (float) (center.x - distance * Math.cos(angle));
                    edy = (float) (center.y - distance * Math.sin(angle));

                    spx = (float) (center.x + distance * Math.cos(angle));
                    spy = (float) (center.y + distance * Math.sin(angle));

                    doubleBond.setStartPoint2(new PointF(spx,spy));
                    doubleBond.setEndPoint2(new PointF(edx,edy));
                }

                /*移除单键 添加双键*/
                bonds.remove(bond);
                bonds.add(doubleBond);
            }

        }

        invalidate();
    }

    /*绘制三键*/
    private void drawTripleBond(PointF point)
    {
        /*找到点击的化学键*/
        for (int i = 0; i < bonds.size(); i ++)
        {
            CYLChemBond bond = bonds.get(i);

            if (distanceFromTwoPoint(point,bond.midPoint) <= bondLength/2 && !bond.getClass().equals(CYLTripleBond.class))
            {
                CYLTripleBond tripleBond = new CYLTripleBond(bond.startPoint,bond.endPoint);
                float spx1,spy1,edx1,edy1,spx2,spy2,edx2,edy2;

                //计算斜率角度
                float angle = (float) Math.atan((bond.endPoint.y - bond.startPoint.y)/(bond.endPoint.x - bond.startPoint.x));

                if (angle < 0)
                {//斜率大于九十度时

                    //第2条
                     spx1 = (float) (bond.startPoint.x + marginOfBonds * Math.sin((Math.PI - angle)));
                     spy1 = (float) (bond.startPoint.y + marginOfBonds * Math.cos((Math.PI - angle)));
                    PointF startTwo = new PointF(spx1, spy1);

                    edx1 = (float) (bond.endPoint.x + marginOfBonds * Math.sin((Math.PI - angle)));
                    edy1 = (float) (bond.endPoint.y + marginOfBonds * Math.cos((Math.PI - angle)));
                    PointF endTwo = new PointF(edx1,edy1);

                    tripleBond.startPoint2 = startTwo;
                    tripleBond.endPoint2 = endTwo;

                    //第三条
                    spx2 = (float) (bond.startPoint.x - marginOfBonds * Math.sin((Math.PI - angle)));
                    spy2 = (float) (bond.startPoint.y - marginOfBonds * Math.cos((Math.PI - angle)));
                    PointF startThree = new PointF(spx2, spy2);

                    edx2 = (float) (bond.endPoint.x - marginOfBonds * Math.sin((Math.PI - angle)));
                    edy2 = (float) (bond.endPoint.y - marginOfBonds * Math.cos((Math.PI - angle)));
                    PointF endThree = new PointF(edx2, edy2);

                    tripleBond.startPoint3 = startThree;
                    tripleBond.endPoint3 = endThree;

                }
                else
                {
                    //斜率小于90
                    //第2条
                    spx1 = (float) (bond.startPoint.x - marginOfBonds * Math.sin((angle)));
                    spy1 = (float) (bond.startPoint.y + marginOfBonds * Math.cos((angle)));
                    PointF startTwo = new PointF(spx1, spy1);

                    edx1 = (float) (bond.endPoint.x - marginOfBonds * Math.sin((angle)));
                    edy1 = (float) (bond.endPoint.y + marginOfBonds * Math.cos((angle)));
                    PointF endTwo = new PointF(edx1,edy1);

                    tripleBond.startPoint2 = startTwo;
                    tripleBond.endPoint2 = endTwo;


                    //第三条
                    spx2 = (float) (bond.startPoint.x + marginOfBonds * Math.sin((angle)));
                    spy2 = (float) (bond.startPoint.y - marginOfBonds * Math.cos((angle)));
                    PointF startThree = new PointF(spx2, spy2);

                    edx2 = (float) (bond.endPoint.x + marginOfBonds * Math.sin((angle)));
                    edy2 = (float) (bond.endPoint.y - marginOfBonds * Math.cos((angle)));
                    PointF endThree = new PointF(edx2, edy2);

                    tripleBond.startPoint3 = startThree;
                    tripleBond.endPoint3 = endThree;
                }

                bonds.remove(bond);
                bonds.add(tripleBond);
            }
        }

        invalidate();
    }

    /*擦除化学键*/
    private void eraseBond(PointF point)
    {
        CYLChemBond bondPrepareToRemove = null;
        for (int i = 0 ; i < bonds.size(); i++)
        {
            CYLChemBond bond = bonds.get(i);

            if (distanceFromTwoPoint(point,bond.midPoint) < bondLength/2)
            {
                bondPrepareToRemove = bond;
                erasedBond = bond;
            }
        }

        if (bondPrepareToRemove != null) bonds.remove(bondPrepareToRemove);
        invalidate();
    }

    /*undo*/
    private boolean isFirstUndo;
    public void undo()
    {
        /*防止点击一次造成两次undo*/
        if (isFirstUndo)
        {
            isFirstUndo = !isFirstUndo;
            return;
        }

        if (erasedBond != null) {
            bonds.add(erasedBond);
            erasedBond = null;
        }
        else
        {
            if (bonds.size() != 0)
            {
                CYLChemBond bond = bonds.get(bonds.size() - 1);
                bonds.remove(bond);
            }
        }

        invalidate();

        isFirstUndo = !isFirstUndo;
        Message.obtain(processHandler,CYLDrawFragement.MESSAGE_UNDO).sendToTarget();
    }

    /*clearAll*/
    public void clearAll()
    {
        bonds.clear();
        points.clear();
        invalidate();
        Message.obtain(processHandler,CYLDrawFragement.MESSAGE_CLEARAll).sendToTarget();
    }

    /*设置辅助线条的点*/
    private void setAssistancePointsWithPoint()
    {

        float p1X = (float) (startX - bondLength * Math.cos(Math.PI/6));
        float p1Y = (float) (startY - bondLength * Math.sin(Math.PI/6));
        PointF p1 = new PointF(p1X,p1Y);
        assistancePoints.add(p1);

        float p2X = (float) (startX + bondLength * Math.cos(Math.PI/6));
        float p2Y = (float) (startY - bondLength * Math.sin(Math.PI/6));
        PointF p2 = new PointF(p2X,p2Y);
        assistancePoints.add(p2);

        float p3X = startX;
        float p3Y = startY + bondLength;
        PointF p3 = new PointF(p3X,p3Y);
        assistancePoints.add(p3);

        float p4X = (float) (startX - bondLength * Math.cos(Math.PI/6));
        float p4Y = (float) (startY + bondLength * Math.sin(Math.PI/6));
        PointF p4 = new PointF(p4X,p4Y);
        assistancePoints.add(p4);

        float p5X = (float) (startX + bondLength * Math.cos(Math.PI/6));
        float p5Y = (float) (startY + bondLength * Math.sin(Math.PI/6));
        PointF p5 = new PointF(p5X,p5Y);
        assistancePoints.add(p5);

        float p6X = startX;
        float p6Y = startY - bondLength;
        PointF p6 = new PointF(p6X,p6Y);
        assistancePoints.add(p6);
    }

    /*移除辅助线条*/
    private void removeAssistancePoint()
    {
//        points.removeAll(assistancePoints);

        assistancePoints.clear();
    }

    private float distanceFromTwoPoint(PointF p1,PointF p2)
    {
        double deltaX = Math.pow((p1.x - p2.x),2);
        double deltaY = Math.pow((p1.y - p2.y),2);

        return (float) Math.sqrt(deltaX + deltaY);
    }


    /*setter*/
    public void setStatus(DrawStatus status) {
        this.status = status;
    }

    public void setProcessHandler(Handler processHandler) {
        this.processHandler = processHandler;
    }

    public void setSelectAtom(RadioButton selectAtom) {
        this.selectAtom = selectAtom;
    }

    public void setAttachAtom(boolean attachAtom) {
        isAttachAtom = attachAtom;
    }

    public void setPoints(ArrayList<PointF> points) {
        if (this.points == null)
        {
            this.points = points;
        }
    }
}
