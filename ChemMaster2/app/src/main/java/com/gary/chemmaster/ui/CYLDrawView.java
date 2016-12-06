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

import com.gary.chemmaster.CYLEnum.DrawStatus;
import com.gary.chemmaster.entity.CYLChemBond;
import com.gary.chemmaster.fragment.CYLDrawFragement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gary on 16/12/1.
 */
public class CYLDrawView extends View {

    private Handler processHandler;
    private String doThing;
    private Canvas canvas;
    private Paint paint;
    private float curX;
    private float curY;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private DrawStatus status = DrawStatus.Draw_SingleBond;
    /*化学键的数组*/
    private ArrayList<CYLChemBond> bonds;
    /*辅助图的点的集合*/
    private ArrayList<PointF> assistancePoints;
    /*化学键，点的集合*/
    private Set<PointF> points;
    /*显示当前正在绘制的线条*/
    private CYLChemBond curBond;
    /*自动粘附的距离*/
    private float autoAttachLength = 50;

    private float bondLength = 200;

    public CYLDrawView(Context context) {
        super(context);
        bonds = new ArrayList<>();
        points = new HashSet<>();
        assistancePoints = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    public CYLDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bonds = new ArrayList<>();
        points = new HashSet<>();
        assistancePoints = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    public CYLDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bonds = new ArrayList<>();
        points = new HashSet<>();
        assistancePoints = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        /*绘制实时线条*/
        if (curBond != null) canvas.drawLine(curBond.startPoint.x,curBond.startPoint.y,curBond.endPoint.x,curBond.endPoint.y,paint);

        /*绘制化学键*/
        for (CYLChemBond bond : bonds)
        {
            if (bond.getClass().equals(CYLChemBond.class))
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
//                Log.d("cyl", "X = "+ X + " Y = "+Y);
                startX = X;
                startY = Y;
                PointF downP = new PointF(X,Y);

                for (PointF point : points)
                {
                    if (distanceFromTwoPoint(point,downP) <= autoAttachLength)
                    {
                        startX = point.x;
                        startY = point.y;
                        downP.set(startX,startY);
                    }

                }
                /*告知界面去显示指示indicator*/
                if (processHandler != null)
                    Message.obtain(processHandler, CYLDrawFragement.MESSAGE_TOUCH_DOWN,downP).sendToTarget();

                break;

            case MotionEvent.ACTION_MOVE:
//                Log.d("cyl", "X = "+ X + " Y = "+Y);
                curX = X;
                curY = Y;
                PointF curP = new PointF(curX,curY);

                if (status.equals(DrawStatus.Draw_SingleBond))
                {
                    curBond = new CYLChemBond(new PointF(startX,startY),new PointF(curX,curY));
                }

                for (PointF point : points)
                {
                    if (distanceFromTwoPoint(point, curP) <= autoAttachLength)
                    {
                        if (processHandler!=null) Message.obtain(processHandler,CYLDrawFragement.MESSAGE_APPROACH_POINT,point).sendToTarget();
                        break;
                    }
                    else
                    {
                        if (processHandler!=null) Message.obtain(processHandler,CYLDrawFragement.MESSAGE_DISAPPROACH_POINT).sendToTarget();
                    }
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
//                Log.d("cyl", "X = "+ X + " Y = "+Y);
                endX = X;
                endY = Y;

//                Message.obtain(processHandler,CYLDrawFragement.MESSAGE_TOUCH_UP).sendToTarget();

                if (status.equals(DrawStatus.Draw_SingleBond))
                {
                    Log.d("cyl","single draw");
                    PointF startP = new PointF(startX,startY);
                    PointF endP = new PointF(endX,endY);

                    /*修正end点的位置*/
                    for (PointF point : points)
                    {
                        if (distanceFromTwoPoint(point,endP) <= autoAttachLength)
                        {
                            endP = point;
                        }
                    }

                    CYLChemBond singleB = new CYLChemBond(startP,endP);
                    bonds.add(singleB);

                    points.add(startP);
                    points.add(endP);

                    invalidate();
                }


                /*告知界面除去指示indicator*/
                if (processHandler != null) {
                    Message.obtain(processHandler, CYLDrawFragement.MESSAGE_TOUCH_UP).sendToTarget();
                    Message.obtain(processHandler,CYLDrawFragement.MESSAGE_DISAPPROACH_POINT).sendToTarget();
                }
                /*放手时不需显示实时线条*/
                curBond = null;

                break;
        }

        return true;
    }


    /*绘制单键*/
    private void drawSingleBond(float x, float y)
    {

    }

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
}
