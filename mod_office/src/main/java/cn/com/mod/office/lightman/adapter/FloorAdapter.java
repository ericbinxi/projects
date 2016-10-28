package cn.com.mod.office.lightman.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.com.mod.office.lightman.R;
import cn.com.mod.office.lightman.entity.FloorInfo;
import cn.com.mod.office.lightman.widget.ViewScroller;

/**
 * 楼层适配器
 * Created by CAT on 2014/11/19.
 */
public class FloorAdapter extends BaseAdapter implements ViewScroller.OnItemSelectListener {
    private Context mContext;
    private List<FloorInfo> mFloorInfos;
    private FloorAdapterListener mListener;

    public FloorAdapter(Context context, List<FloorInfo> floorInfos, FloorAdapterListener listener) {
        this.mContext = context;
        this.mFloorInfos = floorInfos;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return mFloorInfos.size();
    }

    @Override
    public FloorInfo getItem(int position) {
        return mFloorInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_floor, parent, false);
        }
        final FloorInfo info = getItem(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_floor);
        View shadowView = view.findViewById(R.id.iv_shadow);
        ImageView textImageView = (ImageView) view.findViewById(R.id.img_floornum);
        //set text to image
        Bitmap output = null;
        try {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.GRAY);
            paint.setTextSize(40);
            Rect bounds = new Rect();
            paint.getTextBounds(info.name, 0, info.name.length(), bounds);
            output = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(output);
            canvas.drawARGB(0, 255, 255, 255);
            canvas.drawText(info.name, -bounds.left, -bounds.top, paint);
        } catch (OutOfMemoryError e) {
        } catch (Exception e) {
        }
        textImageView.setImageBitmap(output);

        if (info.image != null) {
            imageView.setImageBitmap(info.image);
        } else {
            imageView.setImageResource(R.drawable.floor);
        }
        shadowView.setVisibility((position == getCount() - 1) ? View.INVISIBLE : View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFloorClick(info);
            }
        });
        return view;
    }

    @Override
    public void onItemSelected(ViewScroller viewScroller, View view, int position) {
    }

    @Override
    public void onItemClicked(ViewScroller viewScroller, int position) {
    }

    @Override
    public void onItemTransited(ViewScroller viewScroller, View view, float percent) {
        float height = 290.f * viewScroller.getResources().getDisplayMetrics().density;
        ImageView shadowView = (ImageView) view.findViewById(R.id.iv_shadow);
        ViewGroup.LayoutParams params = shadowView.getLayoutParams();
        params.height = (int) (percent * height);
        shadowView.setLayoutParams(params);
    }

    public interface FloorAdapterListener {
        public void onFloorClick(FloorInfo floorInfo);
    }
}
