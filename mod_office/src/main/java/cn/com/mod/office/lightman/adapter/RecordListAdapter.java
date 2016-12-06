package cn.com.mod.office.lightman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.FaultRecord;

/**
 * Created by Administrator on 2016/11/12.
 */
public class RecordListAdapter extends BaseAdapter {

    private Context context;
    private List<FaultRecord> records;
    private int rightLeftWidth;
    private OnRecordOperateListener onRecordOperateListener;

    public RecordListAdapter(Context context, int rightLeftWidth, List<FaultRecord> records) {
        this.context = context;
        this.rightLeftWidth = rightLeftWidth;
        this.records = records;
    }

    public void setRecords(List<FaultRecord> record) {
        this.records = record;
        notifyDataSetChanged();
    }

    public void setOnRecordOperateListener(OnRecordOperateListener onRecordOperateListener) {
        this.onRecordOperateListener = onRecordOperateListener;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_record,null);
            holder.item_left = (LinearLayout) convertView.findViewById(R.id.left_layout);
            holder.item_right = (LinearLayout) convertView.findViewById(R.id.right_layout);
            holder.index = (TextView) convertView.findViewById(R.id.index);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.fault_desc = (TextView) convertView.findViewById(R.id.faulr_desc);
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(rightLeftWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

        final FaultRecord record = records.get(position);
        holder.index.setText(position+1+":");
        holder.fault_desc.setText(record.getMsg_content());
        holder.time.setText(record.getOp_time().substring(0,11));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRecordOperateListener!=null)
                    onRecordOperateListener.delete(position,record);
            }
        });

        return convertView;
    }

    class ViewHolder{
        LinearLayout item_left,item_right;
        TextView index,fault_desc,time;
        ImageView delete;
    }

   public interface OnRecordOperateListener{
       void delete(int position,FaultRecord record);
   }
}
