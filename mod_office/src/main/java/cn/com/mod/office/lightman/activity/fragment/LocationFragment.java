package cn.com.mod.office.lightman.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.widget.LeftRightView;
import cn.com.mod.office.lightman.widget.UpDownView;

/**
 * Created by Administrator on 2016/10/18.
 */
public class LocationFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private UpDownView upDownView;
    private ImageView btnUpDown;

    private LeftRightView leftRightView;
    private ImageView btnLeft,btnRight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location,container,false);
        upDownView = (UpDownView) rootView.findViewById(R.id.view_up_down);
        btnUpDown = (ImageView) rootView.findViewById(R.id.btn_up_down);
        leftRightView = (LeftRightView) rootView.findViewById(R.id.view_left_right);
        btnLeft = (ImageView) rootView.findViewById(R.id.btn_left);
        btnRight = (ImageView) rootView.findViewById(R.id.btn_right);
        initListener();
        return rootView;
    }

    private void initListener() {
        btnUpDown.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
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
        }
    }
}
