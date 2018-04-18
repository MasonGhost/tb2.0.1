package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;

/**
 * Created by Administrator on 2018/4/18.
 */

public class CountTimerView extends LinearLayout {
    private TextView tvHourDecade;
    private TextView tvHourUnit;
    private TextView tvMinDecade;
    private TextView tvMinUnit;
    private TextView tvSecDecade;
    private TextView tvSecUnit;

    private TextView tvOne;
    private TextView tvTwo;
    private TextView tvThree;

    private Context context;

    private long time;
    private MyCount mc;

    private int hour_decade;
    private int hour_unit;
    private int min_decade;
    private int min_unit;
    private int sec_decade;
    private int sec_unit;

    private int days_decade;
    private int days_unit;

    private OnStopListener mOnStopListener;


    public void setOnStopListener(OnStopListener onStopListener) {
        this.mOnStopListener = onStopListener;
    }

    public interface OnStopListener {
        void isStop();
    }


    public CountTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.count_down_timer, this);
        tvHourDecade = (TextView) view.findViewById(R.id.tvHourDecade);
        tvHourUnit = (TextView) view.findViewById(R.id.tvHourUnit);
        tvMinDecade = (TextView) view.findViewById(R.id.tvMinDecade);
        tvMinUnit = (TextView) view.findViewById(R.id.tvMinUnit);
        tvSecDecade = (TextView) view.findViewById(R.id.tvSecDecade);
        tvSecUnit = (TextView) view.findViewById(R.id.tvSecUnit);
        tvOne = (TextView) view.findViewById(R.id.tvOne);
        tvTwo = (TextView) view.findViewById(R.id.tvTwo);
        tvThree = (TextView) view.findViewById(R.id.tvThree);

    }


    public void setTime(long ms) {
        time = ms / 1000;
        mc = new MyCount(time * 1000, 1000);
        mc.start();
    }


    private class MyCount extends CountDownTimer {


        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long ltime = l / 1000;
            int hour = (int) (ltime / (60 * 60));
            int minute = (int) ((ltime - hour * 60 * 60) / 60);
            int second = (int) (ltime - hour * 60 * 60 - minute * 60);

            hour_decade = hour / 10;
            hour_unit = hour % 10;

            min_decade = minute / 10;
            min_unit = minute % 10;

            sec_decade = second / 10;
            sec_unit = second % 10;


            if (ltime > 86400) {
                tvOne.setText("天");
                tvTwo.setText("时");
                tvThree.setText("分");

                int days = hour / 24;
                days_decade = days / 10;
                days_unit = days % 10;

                int hours = hour % 24;
                hour_decade = hours / 10;
                hour_unit = hours % 10;

                tvHourDecade.setText(days_decade + "");
                tvHourUnit.setText(days_unit + "");
                tvMinDecade.setText(hour_decade + "");
                tvMinUnit.setText(hour_unit + "");
                tvSecDecade.setText(min_decade + "");
                tvSecUnit.setText(min_unit + "");

            } else {
                tvOne.setText("时");
                tvTwo.setText("分");
                tvThree.setText("秒");

                tvHourDecade.setText(hour_decade + "");
                tvHourUnit.setText(hour_unit + "");
                tvMinDecade.setText(min_decade + "");
                tvMinUnit.setText(min_unit + "");
                tvSecDecade.setText(sec_decade + "");
                tvSecUnit.setText(sec_unit + "");

            }

        }

        @Override
        public void onFinish() {
            mc.cancel();
            if(mOnStopListener != null){
                mOnStopListener.isStop();
            }
        }
    }


}
