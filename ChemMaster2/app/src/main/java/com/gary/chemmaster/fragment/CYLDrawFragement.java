package com.gary.chemmaster.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gary.chemmaster.CYLEnum.DrawStatus;
import com.gary.chemmaster.R;
import com.gary.chemmaster.ui.CYLDrawView;
import com.gary.chemmaster.ui.CircleImageView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLDrawFragement extends Fragment {

    public static final int MESSAGE_TOUCH_DOWN = 0;
    public static final int MESSAGE_TOUCH_UP = 1;
    public static final int MESSAGE_APPROACH_POINT = 2;
    public static final int MESSAGE_DISAPPROACH_POINT = 3;
    public static final int MESSAGE_UNDO = 4;
    public static final int MESSAGE_CLEARAll = 5;
    public static final int MESSAGE_SHOW_ATOM = 6;
    RadioGroup toolsRg;
    CYLDrawView drawView;
    RelativeLayout rl;
    ImageView imageView;
    ImageView approachImageV;
    ImageButton atomToolsBtn;
    View standardV;
    RadioButton selectAtom;
    /*显示atomtools*/
    private PopupWindow popupWindow;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what)
            {
                case MESSAGE_TOUCH_DOWN:
                    PointF point = (PointF) msg.obj;
                    showIndicator(point);
                    break;

                case MESSAGE_TOUCH_UP:
                    removeIndicater();
                    break;

                case MESSAGE_APPROACH_POINT:
                    PointF pointf = (PointF) msg.obj;
                    showApporachIndicator(pointf);
                    break;

                case MESSAGE_DISAPPROACH_POINT:
                    removeApproachIndicater();
                    break;

                case MESSAGE_UNDO:
                    toolsRg.check(R.id.single_bond);
                    break;

                case MESSAGE_CLEARAll:
                    toolsRg.check(R.id.single_bond);
                    if (atoms != null && atoms.size() > 0)
                    {
                        for (TextView tv : atoms)
                        {
                            rl.removeView(tv);
                        }
                    }

                    break;

                case MESSAGE_SHOW_ATOM:
                    PointF pointA = (PointF) msg.obj;
                    int flag = msg.arg1;
                    showAtomAtPoint(pointA,flag);
                    break;
            }

        }
    };

    public CYLDrawFragement() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw, null);
        toolsRg = (RadioGroup) view.findViewById(R.id.drawRadioG);
        drawView = (CYLDrawView) view.findViewById(R.id.drawView);
        rl = (RelativeLayout) view.findViewById(R.id.draw_RelativeL);
        atomToolsBtn = (ImageButton) view.findViewById(R.id.showAtomToolsBtn);
        standardV = view.findViewById(R.id.standardView);
        imageView = new ImageView(getContext());
        approachImageV = new ImageView(getContext());

        drawView.setProcessHandler(handler);
        toolsRg.setOnCheckedChangeListener(new InnerToolsOnCheckedListener());
        atomToolsBtn.setOnClickListener(new InnerAtomToolsOnClickListener());

        setUpPopWindow(inflater);

        return view;
    }

    /*显示指示点图标*/
    private void showIndicator(PointF point)
    {
        rl.removeView(imageView);

        imageView.setX(point.x-30);
        imageView.setY(point.y-30);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(60,60);
        imageView.setLayoutParams(lp);
        imageView.setImageResource(R.mipmap.dot);

        rl.addView(imageView);
    }

    private void removeIndicater()
    {
        rl.removeView(imageView);
    }



    private void showApporachIndicator(PointF point)
    {
        rl.removeView(approachImageV);

        approachImageV.setX(point.x-30);
        approachImageV.setY(point.y-30);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(60,60);
        approachImageV.setLayoutParams(lp);
        approachImageV.setImageResource(R.mipmap.dot);

        rl.addView(approachImageV);
    }

    private void removeApproachIndicater()
    {
        rl.removeView(approachImageV);
    }

    /*设置popUpWindow*/
    private void setUpPopWindow(LayoutInflater inflater)
    {
        View popV = inflater.inflate(R.layout.atom_tools,null);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        popupWindow = new PopupWindow(popV,wm.getDefaultDisplay().getWidth(), ViewPager.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);

        final RadioGroup rg = (RadioGroup) popV.findViewById(R.id.atomToolRG);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.Nitrogen:
                        RadioButton N = (RadioButton) rg.findViewById(R.id.Nitrogen);
                        selectAtom = N;
                        break;

                    case R.id.Hydrogen:
                        RadioButton H = (RadioButton) rg.findViewById(R.id.Hydrogen);
                        selectAtom = H;
                        break;

                    case R.id.Oxygen:
                        RadioButton O = (RadioButton) rg.findViewById(R.id.Oxygen);
                        selectAtom = O;
                        break;

                    case R.id.florine:
                        RadioButton F = (RadioButton) rg.findViewById(R.id.florine);
                        selectAtom = F;
                        break;

                    case R.id.Chlore:
                        RadioButton Cl = (RadioButton) rg.findViewById(R.id.Chlore);
                        selectAtom = Cl;
                        break;

                    case R.id.Bromine:
                        RadioButton Br = (RadioButton) rg.findViewById(R.id.Bromine);
                        selectAtom = Br;
                        break;

                    case R.id.Sulfer:
                        RadioButton S = (RadioButton) rg.findViewById(R.id.Sulfer);
                        selectAtom = S;
                        break;
                }

                drawView.setSelectAtom(selectAtom);
            }
        });

    }

    /*在指定point显示相应的原子*/
    private int atomSize = 15;
    private ArrayList<TextView> atoms;
    private void showAtomAtPoint(PointF point, int flag)
    {
        if (atoms == null) atoms = new ArrayList<>();

        /*判断是否覆盖先前原子*/
        for (TextView tv : atoms)
        {
            PointF p = (PointF) tv.getTag();
            if (point.x == p.x && point.y == p.y)
            {
                rl.removeView(tv);
            }
        }

        TextView atom = new TextView(getContext());
        atom.setTag(point);

        String atomStr = getApproprateAtom(flag);
        if (atomStr == null) return;
        atom.setText(atomStr);
        atom.setTextColor(selectAtom.getCurrentTextColor());
        atom.setTextSize(atomSize);
        atom.setGravity(Gravity.CENTER);
        atom.setBackgroundResource(R.mipmap.white_circle);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(atomSize*4,atomSize*4);
        atom.setLayoutParams(lp);
        atom.setX(point.x - atomSize*2);
        atom.setY(point.y - atomSize*2);

        atoms.add(atom);

        rl.addView(atom);
    }


    /*获得正确形式的原子符号*/
    private String getApproprateAtom(int flag)
    {
        String atomStr = selectAtom.getText().toString();

        if (atomStr.equals("H") && flag != 1)
        {
            atomStr = null;
        }
        else if (atomStr.equals("N"))
        {
            if (flag == 1) atomStr = "NH3";
            if (flag == 2) atomStr = "NH2";
            if (flag == 3) atomStr = "NH";
        }
        else if (atomStr.equals("O"))
        {
            if (flag == 1) atomStr = "OH";
            else atomStr = "O";
        }
        else if (atomStr.equals("S"))
        {
            if (flag == 1) atomStr = "SH";
            else atomStr = "S";
        }

        return atomStr;
    }

    /*监听器*/
    class InnerAtomToolsOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            if (!popupWindow.isShowing())
            {
                popupWindow.showAsDropDown(standardV,0,0);
                drawView.setAttachAtom(true);
                ObjectAnimator.ofFloat(v,"rotationX",0,180).setDuration(500).start();
            }
            else
            {
                dismissPopWindow(v);
            }

        }
    }

    class InnerToolsOnCheckedListener implements RadioGroup.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId)
            {
                case R.id.single_bond:
                    drawView.setStatus(DrawStatus.Draw_SingleBond);
                    break;

                case R.id.double_bond:
                    drawView.setStatus(DrawStatus.Draw_DoubleBond);
                    break;

                case R.id.triple_bond:
                    drawView.setStatus(DrawStatus.Draw_TripleBond);
                    break;

                case R.id.erase:
                    drawView.setStatus(DrawStatus.Draw_Erease);
                    break;

                case R.id.undo:
                    drawView.undo();
                    break;

                case R.id.clearAll:
                    drawView.clearAll();
                    break;
            }

            dismissPopWindow(atomToolsBtn);
        }
    }

    public void dismissPopWindow()
    {
        if (popupWindow != null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
            drawView.setAttachAtom(false);
        }
    }

    public void dismissPopWindow(View v)
    {
        if (popupWindow != null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
            drawView.setAttachAtom(false);
            ObjectAnimator.ofFloat(v,"rotationX",-180,0).setDuration(500).start();
        }
    }

    public void setAtomSize(int atomSize) {
        this.atomSize = atomSize;
    }
}
