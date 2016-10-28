package cn.com.mod.office.lightman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.joshua.common.util.ViewHolder;

import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.ClockInfo;
import cn.com.mod.office.lightman.widget.ClockEditPanel;
import cn.com.mod.office.lightman.widget.SwitchButton;

/**
 * 闹钟列表适配器
 * Created by CAT on 2014/11/7.
 */
public class ClockExpandableListAdapter extends BaseExpandableListAdapter {
    private List<ClockInfo> mClockInfos;
    private Context mContext;
    private ClockEditPanel mEditPanel;
    private ClockExpandableListAdapterListener mListener;

    public ClockExpandableListAdapter(Context context, List<ClockInfo> clockInfos, ClockExpandableListAdapterListener listener) {
        this.mContext = context;
        this.mClockInfos = clockInfos;
        this.mListener = listener;
        mEditPanel = new ClockEditPanel(context);
        mEditPanel.setClockEditPanelListener(new ClockEditPanel.ClockEditPanelListener() {
            @Override
            public void onDelete(ClockInfo clockInfo) {
                mListener.onClockDelete(clockInfo);
            }

            @Override
            public void onSubmit(ClockInfo clockInfo, String week, String time) {
                mListener.onSubmit(clockInfo, week, time);
            }
        });
    }

    @Override
    public int getGroupCount() {
        return mClockInfos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mClockInfos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mClockInfos.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_clock_panel_group, null);
        }
        return initItem(convertView, groupPosition);
    }

    private View initItem(View v, final int groupPosition) {
        final ClockInfo clockInfo = (ClockInfo) getGroup(groupPosition);

        TextView sceneName = ViewHolder.get(v, R.id.scene_name);
        TextView time = ViewHolder.get(v, R.id.time);
        View edit = ViewHolder.get(v, R.id.btn_edit);
        final SwitchButton switchButton = ViewHolder.get(v, R.id.clock_switch);
        sceneName.setText(clockInfo.sceneName);
        time.setText(clockInfo.time);
        switchButton.setOnCheckedChangeListener(null);
        switchButton.setChecked(!clockInfo.status);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onClockSwitchChange(clockInfo, !isChecked);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditClick(groupPosition);
            }
        });
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ClockInfo info = (ClockInfo) getGroup(groupPosition);
        mEditPanel.setClockInfo(info);
        return mEditPanel;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public interface ClockExpandableListAdapterListener {
        public void onEditClick(int groupPosition);

        public void onClockSwitchChange(ClockInfo clockInfo, boolean isOpen);

        public void onClockDelete(ClockInfo clockInfo);

        public void onSubmit(ClockInfo clockInfo, String week, String time);
    }
}
