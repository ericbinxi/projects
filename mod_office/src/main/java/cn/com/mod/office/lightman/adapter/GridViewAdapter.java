package cn.com.mod.office.lightman.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.joshua.common.util.ViewHolder;

import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.SceneInfo;
import cn.com.mod.office.lightman.widget.SceneButton;

/**
 * Created by Administrator on 2016/10/23.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private SceneButtonAdapterListener mSceneButtonAdapterListener;
    private List<SceneInfo> mSceneInfo;
    private int mItemToolVisiblePosition;
    private int mItemCheckedPosition;

    public GridViewAdapter(Context context, List<SceneInfo> sceneInfos, SceneButtonAdapterListener listener) {
        this.mContext = context;
        this.mSceneInfo = sceneInfos;
        this.mSceneButtonAdapterListener = listener;
        mItemToolVisiblePosition = -1;
        mItemCheckedPosition = -1;
    }

    public void setSceneInfo(int checkedPosition,List<SceneInfo> mSceneInfo) {
        this.mItemCheckedPosition = checkedPosition;
        this.mSceneInfo = mSceneInfo;
        notifyDataSetChanged();
    }

    public void setCheckedPosition(int mItemCheckedPosition) {
        this.mItemCheckedPosition = mItemCheckedPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSceneInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mSceneInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_grid_scene_button, null);
        }
//        GridView.LayoutParams param = new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        convertView.setLayoutParams(param);
        // 获取对应的情景按钮
        SceneButton button = ViewHolder.get(convertView, R.id.btn_scene);
        // 获取该项对应的情景信息
        SceneInfo sceneInfo = (SceneInfo) getItem(position);
        button.setScenenInfo(sceneInfo);
        if(mItemCheckedPosition==position){
            button.setChecked(true);
        }
        // 设置按钮状态
        if (mItemToolVisiblePosition == position) {
//            button.showTools();
        } else {
            button.hideTools();
        }
        if (mItemCheckedPosition == position) {
            button.setChecked(true);
        } else {
            button.setChecked(false);
        }

        button.setSceneButtonListener(new SceneButton.SceneButtonListener() {
            @Override
            public void onLongClick(SceneButton button, SceneInfo info) {
            }

            @Override
            public void onClick(SceneButton button, SceneInfo info) {
                mItemCheckedPosition = position;
                notifyDataSetChanged();
                mSceneButtonAdapterListener.onSceneClick(info);
            }

            @Override
            public void onClockClick(SceneButton button, SceneInfo info) {
            }

            @Override
            public void onDeleteClick(SceneButton button, SceneInfo info) {
            }

            @Override
            public void onEditClick(SceneButton button, SceneInfo info) {
            }

            @Override
            public void onCanelClick(SceneButton button, SceneInfo info) {
            }
        });

        return convertView;
    }

    public interface SceneButtonAdapterListener {
        public void onSceneClick(SceneInfo sceneInfo);
    }
}
