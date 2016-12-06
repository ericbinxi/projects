package cn.com.mod.office.lightman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.Frame;

/**
 * Created by Administrator on 2016/10/22.
 */
public class DynamicModeAdapter extends BaseAdapter {

    private Context context;
    private List<Frame> datas;
    private View addView;
    private ImageView add;
    private OnFrameOperateListener onFrameOperateListener;

    public DynamicModeAdapter(Context context,List<Frame> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setOnFrameOperateListener(OnFrameOperateListener onFrameOperateListener) {
        this.onFrameOperateListener = onFrameOperateListener;
    }

    public void setDatas(List<Frame> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dymode, null);
            holder = new Viewholder();
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            holder.edit = (ImageView) convertView.findViewById(R.id.edit);
            holder.index = (TextView) convertView.findViewById(R.id.index);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }
        Frame frame = datas.get(position);
        holder.index.setText(String.format(context.getString(R.string.frame_index),position+1));
        holder.time.setText(frame.getHour()+":"+frame.getMinute()+":"+frame.getSecond());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFrameOperateListener!=null)
                    onFrameOperateListener.editFrame(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFrameOperateListener!=null){
                    onFrameOperateListener.deleteFrame(position);
                }
            }
        });
        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFrameOperateListener!=null){
                    onFrameOperateListener.editTime(position);
                }
            }
        });
        return convertView;
    }

    class Viewholder {
        ImageView delete, edit;
        TextView index, time;
    }
    public interface OnFrameOperateListener{
        void editFrame(int position);
        void deleteFrame(int position);
        void editTime(int position);
    }
}
