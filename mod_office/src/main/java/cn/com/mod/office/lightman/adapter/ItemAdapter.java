package cn.com.mod.office.lightman.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.joshua.common.util.ViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.RoomEntity;
import cn.com.mod.office.lightman.entity.RoomInfo;
import cn.com.mod.office.lightman.entity.base.Item;

/**
 * 右侧列表适配器
 * Created by CAT on 2014/10/29.
 */
public class ItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<? extends Item> mItems;
    private ItemAdapterListener mListener;
    private OnItemSelectedChangeListener mItemSelectedChangeListener;
    private OnRoomClickListener onRoomClickListener;
    private List<RoomInfo> roomInfos;
    // 子项选中状态
    private List<Boolean> mItemSelectedStatus;

    public ItemAdapter(Context context, List<? extends Item> items, ItemAdapterListener listener) {
        this.mContext = context;
        this.mItems = items;
        this.mItemSelectedStatus = new ArrayList<Boolean>();
        for (int i = 0; i < items.size(); i++) {
            mItemSelectedStatus.add(false);
        }
        this.mListener = listener;
        roomInfos = new ArrayList<>();
    }

    public void setOnRoomClickListener(OnRoomClickListener onRoomClickListener) {
        this.onRoomClickListener = onRoomClickListener;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_clock_list_item, null);
        }
        // 子数据项
        final Item item = (Item) getItem(position);
        return initItem(position, convertView, item);
    }

    // 初始化getView
    public View initItem(final int position, final View v, final Item item) {
        // 勾选按钮
        final CheckBox checkBox = ViewHolder.get(v, R.id.cb);
        // 名字
        final TextView itemName = ViewHolder.get(v, R.id.name);
        // 选项名
        final RoomInfo room = (RoomInfo) item;
        itemName.setText(room.getRoomEntity().getRoom_name());

        handleCheckView(room.isSelected(),checkBox,v);
        //被选中的房间
        if(!roomInfos.contains(room))
            roomInfos.add(room);
        // 勾选按钮事件
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleCheckView(isChecked,checkBox,v);
                room.setSelected(isChecked);
                mItemSelectedStatus.set(position, isChecked);

                if (mListener != null) {
                    mListener.onItemSelectedChange(item.id, isChecked);
                }
                if(onRoomClickListener!=null){
                    onRoomClickListener.onRoomClick(roomInfos);
                }

                if (mItemSelectedChangeListener != null) {
                    mItemSelectedChangeListener.onItemSelectedChange(mItems.size(), getSelectedItemCount());
                }
            }
        });
        // 背景点击事件
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.performClick();
            }
        });

        // 从历史状态读取勾选状态
        checkBox.setChecked(mItemSelectedStatus.get(position));

        return v;
    }

    private void handleCheckView(boolean isChecked,CheckBox checkBox,View v){
        checkBox.setChecked(isChecked);
        if (isChecked) {
//            v.setBackgroundResource(R.drawable.bg_dark_repeat);
//            itemName.setTextColor(mContext.getResources().getColor(R.color.orange));
        } else {
//            v.setBackgroundColor(Color.WHITE);
//            itemName.setTextColor(mContext.getResources().getColor(R.color.black));
        }
    }

    // 选中所有
    public void selectAll(boolean isSelected) {
        if(isSelected){
            roomInfos.clear();
            roomInfos.addAll((Collection<? extends RoomInfo>) mItems);
            for (RoomInfo info:roomInfos) {
                info.setSelected(isSelected);
            }
            if(onRoomClickListener!=null)
                onRoomClickListener.onRoomClick(roomInfos);
        }else{
            roomInfos.clear();
            if(onRoomClickListener!=null)
                onRoomClickListener.onRoomClick(roomInfos);
        }
//        if (mListener != null) {
//            for (int i = 0; i < mItems.size(); i++) {
//                if (mItemSelectedStatus.get(i) != isSelected) {
//                    mListener.onItemSelectedChange(mItems.get(i).id, isSelected);
//                }
//            }
//        }
        Collections.fill(mItemSelectedStatus, isSelected);
        notifyDataSetChanged();
    }

    private int getSelectedItemCount() {
        int count = 0;
        for (int i = 0; i < mItemSelectedStatus.size(); i++) {
            if (mItemSelectedStatus.get(i)) {
                count++;
            }
        }
        return count;
    }

    public void setItemAdapterListener(ItemAdapterListener listener) {
        this.mListener = listener;
    }

    public void setOnSelectedCountChangeListener(OnItemSelectedChangeListener itemSelectedChangeListener) {
        this.mItemSelectedChangeListener = itemSelectedChangeListener;
    }

    // 选择某项
    public void selectItem(Item item, boolean isSelected) {
        int index = mItems.indexOf(item);
        if (index != -1) {
            mItemSelectedStatus.set(index, isSelected);
            notifyDataSetChanged();
        }
    }

    public interface ItemAdapterListener {
        public void onClockClick(Item item);

        public void onItemSelectedChange(String itemId, boolean isSelected);
    }

    public interface OnItemSelectedChangeListener {
        public void onItemSelectedChange(int itemSize, int selectedCount);
    }
    public interface OnRoomClickListener{
        void onRoomClick(List<RoomInfo> infos);
    }
}
