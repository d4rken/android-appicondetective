package eu.thedarken.appicondetective;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScanListAdapter extends BaseAdapter {
    private final List<ScanItem> mData = new ArrayList<ScanItem>();
    private int mOptimalWidth;
    private int mOptimalHeight;
    private int mOptimalCount;

    public ScanListAdapter(int width, int height) {
        mOptimalHeight = height;
        mOptimalWidth = width;
        mOptimalCount = mOptimalWidth * mOptimalHeight;
    }

    public void setData(List<ScanItem> data) {
        mData.clear();
        if (data != null)
            mData.addAll(data);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ScanItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        TextView pxCount;
        TextView dimen;
        TextView name;
        TextView pkg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scanner_adapter_line, parent, false);
            holder = new ViewHolder();
            holder.pxCount = (TextView) convertView.findViewById(R.id.tv_pixelcount);
            holder.dimen = (TextView) convertView.findViewById(R.id.tv_dimen);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.pkg = (TextView) convertView.findViewById(R.id.tv_packagename);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanItem item = getItem(position);
        int percent = (int) ((item.getPxCount() * 100.0f) / mOptimalCount);
        holder.pxCount.setText("PX: " + item.getPxCount() + " (" + percent + "%)");
        holder.dimen.setText(item.getWidth() + "W " + item.getHeight() + "H");
        holder.name.setText(item.getName());
        holder.pkg.setText(item.getPackageName());
        if (item.getPxCount() > mOptimalCount * 4) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.worse));
        } else if (item.getPxCount() >= mOptimalCount * 2) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.bad));
        } else if (item.getPxCount() <= mOptimalCount * 0.5f) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.meh));
        } else {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.ok));
        }
        return convertView;
    }
}
