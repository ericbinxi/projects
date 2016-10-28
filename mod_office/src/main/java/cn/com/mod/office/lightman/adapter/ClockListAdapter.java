package cn.com.mod.office.lightman.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.Clock;
import cn.com.mod.office.lightman.widget.SwitchButton;

/**
 * Created by Administrator on 2016/10/23.
 */
public class ClockListAdapter extends BaseAdapter {
    private Context context;
    private List<Clock> clocks;
    private OnClockOperateListener onClockOperateListener;
    private int rightWidth;

    public ClockListAdapter(Context context, List<Clock> clocks,int rightWidth) {
        this.context = context;
        this.clocks = clocks;
        this.rightWidth = rightWidth;
    }

    public void setOnClockOperateListener(OnClockOperateListener onClockOperateListener) {
        this.onClockOperateListener = onClockOperateListener;
    }

    @Override
    public int getCount() {
        return clocks.size();
    }

    @Override
    public Object getItem(int position) {
        return clocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_clock,null);
            holder.btn_switch = (SwitchButton) convertView.findViewById(R.id.btn_switch);
            holder.clock_time = (TextView) convertView.findViewById(R.id.clock_time);
            holder.ll_repeat = (LinearLayout) convertView.findViewById(R.id.ll_repeat);
            holder.mode_name = (TextView) convertView.findViewById(R.id.mode_name);
            holder.delete = (TextView) convertView.findViewById(R.id.delete);
            holder.item_left = (LinearLayout) convertView.findViewById(R.id.left_layout);
            holder.item_right = (LinearLayout) convertView.findViewById(R.id.right_layout);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(rightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

        final Clock clock = clocks.get(position);
        holder.mode_name.setText(clock.getMode_id());
        holder.clock_time.setText(clock.getHour()+":"+clock.getMinute());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClockOperateListener!=null){
                    onClockOperateListener.onClockDelete(position,clock.getClock_id());
                }
            }
        });
        holder.btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //打开闹钟
                    if(onClockOperateListener!=null)
                        onClockOperateListener.onClockOpen(clock.getClock_id());
                }else{
                    if(onClockOperateListener!=null)
                        onClockOperateListener.onClockClosed(clock.getClock_id());
                }
            }
        });
        handleRepeatDay(holder.ll_repeat,clock);
        return convertView;
    }

    private void handleRepeatDay(LinearLayout ll_repeat, Clock clock) {
        String[] arrays = clock.getWeekday().split(",");
        for(int i=0;i<arrays.length;i++){
            String day = arrays[i].replace("(","").replace(")","");

            CheckedTextView textView = new CheckedTextView(context);
            textView.setText(getDayString(Integer.parseInt(day)));
            textView.setTextColor(context.getResources().getColor(R.color.orange));
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.btn_week);
            textView.setClickable(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            LinearLayout content = new LinearLayout(context);
            content.setGravity(Gravity.CENTER);
            content.addView(textView);
            ll_repeat.addView(content, params);
        }
    }

    private String getDayString(int day){
        switch (day){
            case 0:
                return context.getResources().getString(R.string.sunday);
            case 1:
                return context.getResources().getString(R.string.monday);
            case 2:
                return context.getResources().getString(R.string.tuesday);
            case 3:
                return context.getResources().getString(R.string.wednesday);
            case 4:
                return context.getResources().getString(R.string.thursday);
            case 5:
                return context.getResources().getString(R.string.friday);
            case 6:
                return context.getResources().getString(R.string.saturday);
        }
        return context.getResources().getString(R.string.saturday);
    }

    class ViewHolder{
        LinearLayout ll_repeat,item_left,item_right;
        TextView mode_name,clock_time,delete;
        SwitchButton btn_switch;
    }

    public interface OnClockOperateListener{
        void onClockOpen(int clockId);
        void onClockClosed(int clockId);
        void onClockDelete(int position,int clockId);
    }
}
