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

/**
 * Created by Administrator on 2016/10/22.
 */
public class DynamicModeAdapter extends BaseAdapter {

    private Context context;
    private List<String> datas;
    private View addView;
    private ImageView add;

    public DynamicModeAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");
        datas.add("111");

    }

    @Override
    public int getCount() {
        return datas.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == datas.size())
            return 2;//加号
        else
           return 1;
    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

    @Override
    public Object getItem(int position) {
//        if(position==datas.size())
//            return addView;
//        else
            return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if(getItemViewType(position)==2){
//            return addView;
//        }else{
            Viewholder holder = null;
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_dymode,null);
                holder = new Viewholder();
                holder.delete = (ImageView) convertView.findViewById(R.id.delete);
                holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                holder.index = (TextView) convertView.findViewById(R.id.index);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(holder);
            }else{
                holder = (Viewholder) convertView.getTag();
            }
            return convertView;
//        }

    }

    class Viewholder{
        ImageView delete,edit;
        TextView index,time;
    }
}
