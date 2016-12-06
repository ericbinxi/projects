package cn.com.mod.office.lightman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.base.BaseModeEntity;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ModeManagerAdapter extends BaseAdapter {

    private List<BaseModeEntity> modes;
    private Context context;
    private int rightWidth;
    private OnModeOperaterListener listener;

    public ModeManagerAdapter(Context context, int rightWidth,List<BaseModeEntity> datas) {
        this.context = context;
        this.rightWidth = rightWidth;
        this.modes = datas;
    }

    public void setModes(List<BaseModeEntity> modes) {
        this.modes = modes;
        notifyDataSetChanged();
    }

    public void setListener(OnModeOperaterListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return modes.size();
    }

    @Override
    public Object getItem(int position) {
        return modes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mode_manager,null);
            holder = new ViewHolder();
            holder.mode_name = (TextView) convertView.findViewById(R.id.mode_name);
            holder.iv_mode = (ImageView) convertView.findViewById(R.id.iv_mode);
            holder.mode_edit = (ImageView) convertView.findViewById(R.id.mode_edit);
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
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

        BaseModeEntity mode = modes.get(position);
        holder.mode_name.setText(mode.getMode_name());
        if(1==mode.getModeType()){
            holder.iv_mode.setImageResource(R.drawable.icon_mode_normal);
        }else{
            holder.iv_mode.setImageResource(R.drawable.icon_mode_dynamic);
        }
        holder.mode_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.editMode(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.deleteMode(position);
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView mode_edit,iv_mode,delete;
        TextView mode_name;
        LinearLayout item_left,item_right;
    }

    public interface OnModeOperaterListener{
        void editMode(int position);
        void deleteMode(int position);
    }
}
