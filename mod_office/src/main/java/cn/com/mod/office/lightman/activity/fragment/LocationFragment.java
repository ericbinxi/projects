package cn.com.mod.office.lightman.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.widget.HorizonAngleView;
import cn.com.mod.office.lightman.widget.LeftRightView;
import cn.com.mod.office.lightman.widget.LightAngleView;
import cn.com.mod.office.lightman.widget.UpDownView;
import cn.com.mod.office.lightman.widget.VerticalAngleView;

/**
 * Created by Administrator on 2016/10/18.
 */
public class LocationFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private ScrollView scrollView;
    private View rootView;
    private UpDownView upDownView;
    private ImageView btnUpDown;

    private LeftRightView leftRightView;
    private ImageView btnLeft, btnRight;

    private VerticalAngleView verticalAngleView;
    private ImageView btn_v_reduce, btn_v_add;
    private TextView tv_v_angle;
    private int currentVerticalAngle;

    private HorizonAngleView horizonAngleView;
    private ImageView btn_h_reduce, btn_h_add;
    private TextView tv_h_angle;
    private int currentHorizonAngle;

    private LightAngleView lightAngleView;
    private ImageView btn_l_reduce, btn_l_add;
    private TextView tv_l_angle;
    private int currentlightAngle = 15;

    private OnOperateLampDegreeListener onOperateLampDegreeListener;

    public void setOnOperateLampDegreeListener(OnOperateLampDegreeListener onOperateLampDegreeListener) {
        this.onOperateLampDegreeListener = onOperateLampDegreeListener;
    }

    public int getCurrentHorizonAngle() {
        return currentHorizonAngle;
    }

    public int getCurrentlightAngle() {
        return currentlightAngle;
    }

    public int getCurrentVerticalAngle() {
        return currentVerticalAngle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location, container, false);
        upDownView = (UpDownView) rootView.findViewById(R.id.view_up_down);
        btnUpDown = (ImageView) rootView.findViewById(R.id.btn_up_down);

        leftRightView = (LeftRightView) rootView.findViewById(R.id.view_left_right);
        btnLeft = (ImageView) rootView.findViewById(R.id.btn_left);
        btnRight = (ImageView) rootView.findViewById(R.id.btn_right);

        verticalAngleView = (VerticalAngleView) rootView.findViewById(R.id.view_vertical);
        btn_v_reduce = (ImageView) rootView.findViewById(R.id.btn_v_reduce);
        btn_v_add = (ImageView) rootView.findViewById(R.id.btn_v_add);
        tv_v_angle = (TextView) rootView.findViewById(R.id.tv_v_angle);

        horizonAngleView = (HorizonAngleView) rootView.findViewById(R.id.view_horizon);
        btn_h_reduce = (ImageView) rootView.findViewById(R.id.btn_h_reduce);
        btn_h_add = (ImageView) rootView.findViewById(R.id.btn_h_add);
        tv_h_angle = (TextView) rootView.findViewById(R.id.tv_h_angle);

        lightAngleView = (LightAngleView) rootView.findViewById(R.id.view_light);
        btn_l_add = (ImageView) rootView.findViewById(R.id.btn_l_add);
        btn_l_reduce = (ImageView) rootView.findViewById(R.id.btn_l_reduce);
        tv_l_angle = (TextView) rootView.findViewById(R.id.tv_l_angle);

        tv_h_angle.setText(String.format(getString(R.string.template),currentHorizonAngle));
        tv_v_angle.setText(String.format(getString(R.string.template),currentVerticalAngle));
        tv_l_angle.setText(String.format(getString(R.string.template),currentlightAngle));

        scrollView = (ScrollView) rootView.findViewById(R.id.scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        initListener();
        return rootView;
    }

    private void initListener() {
        btnUpDown.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btn_v_reduce.setOnClickListener(this);
        btn_v_add.setOnClickListener(this);
        btn_h_reduce.setOnClickListener(this);
        btn_h_add.setOnClickListener(this);
        btn_l_add.setOnClickListener(this);
        btn_l_reduce.setOnClickListener(this);

        btn_h_reduce.setOnLongClickListener(this);
        btn_h_add.setOnLongClickListener(this);
        btn_v_reduce.setOnLongClickListener(this);
        btn_v_add.setOnLongClickListener(this);
        btn_l_reduce.setOnLongClickListener(this);
        btn_l_add.setOnLongClickListener(this);

        verticalAngleView.setOnAngleChangeListener(new VerticalAngleView.OnAngleChangeListener() {
            @Override
            public void onAngleChange(int angle) {
                currentVerticalAngle = angle;
                tv_v_angle.setText(String.format(getString(R.string.template),currentVerticalAngle));
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampVDegree(currentVerticalAngle);
                }
            }
        });
        horizonAngleView.setOnAngleChangeListener(new HorizonAngleView.OnAngleChangeListener() {
            @Override
            public void onAngleChange(int angle) {
                currentHorizonAngle = angle;
                tv_h_angle.setText(String.format(getString(R.string.template),currentHorizonAngle));
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampHDegree(currentHorizonAngle);
                }
            }
        });
        lightAngleView.setOnAngleChangeListener(new LightAngleView.OnAngleChangeListener() {
            @Override
            public void onAngleChange(int angle) {
                currentlightAngle = angle;
                tv_l_angle.setText(String.format(getString(R.string.template),currentlightAngle));
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampLDegree(currentlightAngle);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_up_down:
                if (upDownView.isUp()) {
                    btnUpDown.setImageResource(R.drawable.ic_arrow_up);
                    if (onOperateLampDegreeListener != null) {
                        onOperateLampDegreeListener.setLampMovement(2);
                    }
                } else {
                    btnUpDown.setImageResource(R.drawable.ic_arrow_down);
                    if (onOperateLampDegreeListener != null) {
                        onOperateLampDegreeListener.setLampMovement(1);
                    }
                }
                upDownView.setUp(!upDownView.isUp());
                break;
            case R.id.btn_left:
                leftRightView.setLeft();
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampMovement(3);
                }
                break;
            case R.id.btn_right:
                leftRightView.setRight();
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampMovement(4);
                }
                break;
            case R.id.btn_v_reduce:
//                currentVerticalAngle = Integer.parseInt(tv_v_angle.getText().toString().trim());
                currentVerticalAngle = currentVerticalAngle - 1;
                if (currentVerticalAngle <= -80)
                    currentVerticalAngle = -80;
                tv_v_angle.setText(String.format(getString(R.string.template),currentVerticalAngle));
                verticalAngleView.setAngle(currentVerticalAngle);
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampVDegree(currentVerticalAngle);
                }
                break;
            case R.id.btn_v_add:
//                currentVerticalAngle = Integer.parseInt(tv_v_angle.getText().toString().trim());
                currentVerticalAngle = currentVerticalAngle + 1;
                if (currentVerticalAngle >= 80)
                    currentVerticalAngle = 80;
                tv_v_angle.setText(String.format(getString(R.string.template),currentVerticalAngle));
                verticalAngleView.setAngle(currentVerticalAngle);
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampVDegree(currentVerticalAngle);
                }
                break;
            case R.id.btn_h_reduce:
//                currentHorizonAngle = Integer.parseInt(tv_h_angle.getText().toString().trim());
                currentHorizonAngle = currentHorizonAngle - 1;
                if (currentHorizonAngle <= -130)
                    currentHorizonAngle = -130;
                tv_h_angle.setText(String.format(getString(R.string.template),currentHorizonAngle));
                horizonAngleView.setAngle(currentHorizonAngle);
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampHDegree(currentHorizonAngle);
                }
                break;
            case R.id.btn_h_add:
//                currentHorizonAngle = Integer.parseInt(tv_h_angle.getText().toString().trim());
                currentHorizonAngle = currentHorizonAngle + 1;
                if (currentHorizonAngle >= 130)
                    currentHorizonAngle = 130;
                tv_h_angle.setText(String.format(getString(R.string.template),currentHorizonAngle));
                horizonAngleView.setAngle(currentHorizonAngle);
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampHDegree(currentHorizonAngle);
                }
                break;
            case R.id.btn_l_add:
//                currentlightAngle = Integer.parseInt(tv_l_angle.getText().toString().trim());
                currentlightAngle = currentlightAngle + 1;
                if (currentlightAngle >= 50)
                    currentlightAngle = 50;
                tv_l_angle.setText(String.format(getString(R.string.template),currentlightAngle));
                lightAngleView.setAngle(currentlightAngle);
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampLDegree(currentHorizonAngle);
                }
                break;
            case R.id.btn_l_reduce:
//                currentlightAngle = Integer.parseInt(tv_l_angle.getText().toString().trim());
                currentlightAngle = currentlightAngle - 1;
                if (currentlightAngle <= 15)
                    currentlightAngle = 15;
                tv_l_angle.setText(String.format(getString(R.string.template),currentlightAngle));
                lightAngleView.setAngle(currentlightAngle);
                if (onOperateLampDegreeListener != null) {
                    onOperateLampDegreeListener.setLampLDegree(currentHorizonAngle);
                }
                break;
        }
    }

    private static final int HORIZON_ADD = 1;
    private static final int HORIZON_REDUCE = 2;
    private static final int VETIVAL_ADD = 3;
    private static final int VETIVAL_REDUCE = 4;
    private static final int LIGHT_ADD = 5;
    private static final int LIGHT_REDUCE = 6;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean callback = msg.arg1 == 100;
            switch (msg.what) {
                case HORIZON_ADD:
                    if (!callback) {
//                        currentHorizonAngle = Integer.parseInt(tv_h_angle.getText().toString().trim());
                        currentHorizonAngle = currentHorizonAngle + 1;
                        if (currentHorizonAngle >= 130)
                            currentHorizonAngle = 130;
                        tv_h_angle.setText(String.format(getString(R.string.template),currentHorizonAngle));
                        horizonAngleView.setAngle(currentHorizonAngle);
                    }
                    if (onOperateLampDegreeListener != null && callback) {
                        onOperateLampDegreeListener.setLampHDegree(currentHorizonAngle);
                    }
                    break;
                case HORIZON_REDUCE:
                    if (!callback) {
//                        currentHorizonAngle = Integer.parseInt(tv_h_angle.getText().toString().trim());
                        currentHorizonAngle = currentHorizonAngle - 1;
                        if (currentHorizonAngle <= -130)
                            currentHorizonAngle = -130;
                        tv_h_angle.setText(String.format(getString(R.string.template),currentHorizonAngle));
                        horizonAngleView.setAngle(currentHorizonAngle);
                    }
                    if (onOperateLampDegreeListener != null && callback) {
                        onOperateLampDegreeListener.setLampHDegree(currentHorizonAngle);
                    }
                    break;
                case VETIVAL_ADD:
                    if (!callback) {
//                        currentVerticalAngle = Integer.parseInt(tv_v_angle.getText().toString().trim());
                        currentVerticalAngle = currentVerticalAngle + 1;
                        if (currentVerticalAngle >= 80)
                            currentVerticalAngle = 80;
                        tv_v_angle.setText(String.format(getString(R.string.template),currentVerticalAngle));
                        verticalAngleView.setAngle(currentVerticalAngle);
                    }
                    if (onOperateLampDegreeListener != null && callback) {
                        onOperateLampDegreeListener.setLampVDegree(currentVerticalAngle);
                    }
                    break;
                case VETIVAL_REDUCE:
                    if (!callback) {
//                        currentVerticalAngle = Integer.parseInt(tv_v_angle.getText().toString().trim());
                        currentVerticalAngle = currentVerticalAngle - 1;
                        if (currentVerticalAngle <= -80)
                            currentVerticalAngle = -80;
                        tv_v_angle.setText(String.format(getString(R.string.template),currentVerticalAngle));
                        verticalAngleView.setAngle(currentVerticalAngle);
                    }
                    if (onOperateLampDegreeListener != null && callback) {
                        onOperateLampDegreeListener.setLampVDegree(currentVerticalAngle);
                    }
                    break;
                case LIGHT_ADD:
                    if (!callback) {
//                        currentlightAngle = Integer.parseInt(tv_l_angle.getText().toString().trim());
                        currentlightAngle = currentlightAngle + 1;
                        if (currentlightAngle >= 50)
                            currentlightAngle = 50;
                        tv_l_angle.setText(String.format(getString(R.string.template),currentlightAngle));
                        lightAngleView.setAngle(currentlightAngle);
                    }
                    if (onOperateLampDegreeListener != null && callback) {
                        onOperateLampDegreeListener.setLampLDegree(currentHorizonAngle);
                    }
                    break;
                case LIGHT_REDUCE:
                    if (!callback) {
//                        currentlightAngle = Integer.parseInt(tv_l_angle.getText().toString().trim());
                        currentlightAngle = currentlightAngle - 1;
                        if (currentlightAngle <= 15)
                            currentlightAngle = 15;
                        tv_l_angle.setText(String.format(getString(R.string.template),currentlightAngle));
                        lightAngleView.setAngle(currentlightAngle);
                    }
                    if (onOperateLampDegreeListener != null && callback) {
                        onOperateLampDegreeListener.setLampLDegree(currentHorizonAngle);
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_v_reduce:
                new AngleThread(btn_v_reduce, VETIVAL_REDUCE).start();
                break;
            case R.id.btn_v_add:
                new AngleThread(btn_v_add, VETIVAL_ADD).start();
                break;
            case R.id.btn_h_reduce:
                new AngleThread(btn_h_reduce, HORIZON_REDUCE).start();
                break;
            case R.id.btn_h_add:
                new AngleThread(btn_h_add, HORIZON_ADD).start();
                break;
            case R.id.btn_l_add:
                new AngleThread(btn_l_add, LIGHT_ADD).start();
                break;
            case R.id.btn_l_reduce:
                new AngleThread(btn_l_reduce, LIGHT_REDUCE).start();
                break;
        }
        return true;
    }

    class AngleThread extends Thread {
        private ImageView button;
        private int messageWhat = -1;//1；水平  2：垂直

        public AngleThread(ImageView button, int messageWhat) {
            this.button = button;
            this.messageWhat = messageWhat;
        }

        @Override
        public void run() {
            while (button.isPressed()) {
                try {
                    mHandler.sendEmptyMessage(messageWhat);
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!button.isPressed()) {
                Message.obtain(mHandler, messageWhat, 100, 0).sendToTarget();
            }
        }
    }

    public interface OnOperateLampDegreeListener {
        void setLampHDegree(int degree);

        void setLampVDegree(int degree);

        void setLampLDegree(int degree);

        void setLampMovement(int degree);
    }
}
