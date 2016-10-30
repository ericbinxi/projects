package cn.com.mod.office.lightman.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.widget.HorizonAngleView;
import cn.com.mod.office.lightman.widget.LeftRightView;
import cn.com.mod.office.lightman.widget.UpDownView;
import cn.com.mod.office.lightman.widget.VerticalAngleView;

/**
 * Created by Administrator on 2016/10/18.
 */
public class LocationFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private UpDownView upDownView;
    private ImageView btnUpDown;

    private LeftRightView leftRightView;
    private ImageView btnLeft,btnRight;

    private VerticalAngleView verticalAngleView;
    private ImageView btn_v_reduce,btn_v_add;
    private TextView tv_v_angle;
    private int currentVerticalAngle;

    private HorizonAngleView horizonAngleView;
    private ImageView btn_h_reduce,btn_h_add;
    private TextView tv_h_angle;
    private int currentHorizonAngle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location,container,false);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_up_down:
                upDownView.setUp(!upDownView.isUp());
                if(upDownView.isUp())
                    btnUpDown.setImageResource(R.drawable.ic_arrow_up);
                else
                    btnUpDown.setImageResource(R.drawable.ic_arrow_down);
                break;
            case R.id.btn_left:
                leftRightView.setLeft();
                break;
            case R.id.btn_right:
                leftRightView.setRight();
                break;
            case R.id.btn_v_reduce:
                currentVerticalAngle = Integer.parseInt(tv_v_angle.getText().toString().trim());
                currentVerticalAngle = currentVerticalAngle -5;
                tv_v_angle.setText(currentVerticalAngle +"");
                verticalAngleView.setAngle(currentVerticalAngle);
                break;
            case R.id.btn_v_add:
                currentVerticalAngle = Integer.parseInt(tv_v_angle.getText().toString().trim());
                currentVerticalAngle = currentVerticalAngle +5;
                tv_v_angle.setText(currentVerticalAngle +"");
                verticalAngleView.setAngle(currentVerticalAngle);
                break;
            case R.id.btn_h_reduce:
                currentHorizonAngle = Integer.parseInt(tv_h_angle.getText().toString().trim());
                currentHorizonAngle = currentHorizonAngle - 5;
                tv_h_angle.setText(currentHorizonAngle +"");
                horizonAngleView.setAngle(currentHorizonAngle);
                break;
            case R.id.btn_h_add:
                currentHorizonAngle = Integer.parseInt(tv_h_angle.getText().toString().trim());
                currentHorizonAngle = currentHorizonAngle + 5;
                tv_h_angle.setText(currentHorizonAngle +"");
                horizonAngleView.setAngle(currentHorizonAngle);
                break;
        }
    }
}
