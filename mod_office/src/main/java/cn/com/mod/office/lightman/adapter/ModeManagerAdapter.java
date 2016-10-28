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

/**
 * Created by Administrator on 2016/10/21.
 */
public class ModeManagerAdapter extends BaseAdapter {

    private List<String> datas;
    private Context context;
    private int rightWidth;
    private OnModeOperaterListener listener;

    public ModeManagerAdapter(Context context, int rightWidth) {
        this.context = context;
        this.rightWidth = rightWidth;
        datas = new ArrayList<>();
        datas.add("11111");
        datas.add("22211");
        datas.add("13311");
        datas.add("14441");
    }

    public void setListener(OnModeOperaterListener listener) {
        this.listener = listener;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mode_manager,null);
            holder = new ViewHolder();
            holder.mode_name = (TextView) convertView.findViewById(R.id.mode_name);
            holder.iv_mode = (ImageView) convertView.findViewById(R.id.iv_mode);
            holder.mode_edit = (ImageView) convertView.findViewById(R.id.mode_edit);
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

        return convertView;
    }

    class ViewHolder{
        ImageView mode_edit,iv_mode;
        TextView mode_name,delete;
        LinearLayout item_left,item_right;
    }

    public interface OnModeOperaterListener{
        void editMode(int position);
        void deleteMode(int position);
    }
}