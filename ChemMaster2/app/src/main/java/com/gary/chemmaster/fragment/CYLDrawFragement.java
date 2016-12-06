package com.gary.chemmaster.fragment;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.gary.chemmaster.CYLEnum.DrawStatus;
import com.gary.chemmaster.R;
import com.gary.chemmaster.ui.CYLDrawView;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLDrawFragement extends Fragment {

    public static final int MESSAGE_TOUCH_DOWN = 0;
    public static final int MESSAGE_TOUCH_UP = 1;
    public static final int MESSAGE_APPROACH_POINT = 2;
    public static final int MESSAGE_DISAPPROACH_POINT = 3;
    RadioGroup toolsRg;
    CYLDrawView drawView;
    RelativeLayout rl;
    ImageView imageView;
    ImageView approachImageV;

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
        imageView = new ImageView(getContext());
        approachImageV = new ImageView(getContext());

        drawView.setProcessHandler(handler);
        toolsRg.setOnCheckedChangeListener(new InnerToolsOnCheckedListener());

        return view;
    }

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
        Log.d("cyl","approach");
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
                    break;

                case R.id.erase:
                    break;

                case R.id.undo:
                    break;

                case R.id.clearAll:
                    break;
            }

        }
    }
}
