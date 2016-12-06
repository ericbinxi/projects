package cn.com.mod.office.lightman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.ParamEntity;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ParamsListAdapter extends BaseAdapter {

    private List<ParamEntity> datas;
    private Context context;
    private int rightWidth;
    private OnBackupParamOperateListener onBackupParamOperateListener;
    private int checkedPosition = -1;

    public ParamsListAdapter(Context context,int rightWidth,List<ParamEntity> datas) {
        this.context = context;
        this.rightWidth = rightWidth;
        this.datas = datas;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public void setDatas(List<ParamEntity> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setOnBackupParamOperateListener(OnBackupParamOperateListener onBackupParamOperateListener) {
        this.onBackupParamOperateListener = onBackupParamOperateListener;
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
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_paramslist,null);
            holder = new ViewHolder();
            holder.index = (TextView) convertView.findViewById(R.id.index);
            holder.desc = (TextView) convertView.findViewById(R.id.describe);
            holder.remark = (TextView) convertView.findViewById(R.id.remark);
            holder.apply = (TextView) convertView.findViewById(R.id.apply);
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

        final ParamEntity entity = datas.get(position);
        holder.index.setText(position+1+".");
        holder.desc.setText(String.format(context.getResources().getString(R.string.param_desc_format),entity.getBrightness()+"",entity.getColorTemp()+"",entity.getColorRgbValue()));
        holder.remark.setText(entity.getParam_addInfo());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBackupParamOperateListener!=null)
                    onBackupParamOperateListener.deleteParam(position);
            }
        });
        if(checkedPosition == position){
            holder.apply.setText("已应用");
            holder.apply.setBackgroundResource(R.drawable.shape_btn_gray);
            holder.apply.setClickable(false);
            checkedPosition = -1;
        }else {
            holder.apply.setText("应用");
            holder.apply.setBackgroundResource(R.drawable.shape_btn_orange);
            holder.apply.setClickable(true);
        }
        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBackupParamOperateListener!=null)
                    onBackupParamOperateListener.applyParam(position);
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView index,desc,remark,apply;
        ImageView delete;
        LinearLayout item_left,item_right;
    }

    public interface OnBackupParamOperateListener{
        void deleteParam(int position);
        void applyParam(int position);
    }
}
