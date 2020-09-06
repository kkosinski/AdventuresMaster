package com.wintermute.adventuresmaster.view.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;

import java.util.List;

public class HueBridgeViewAdapter extends ArrayAdapter<HueBridge>
{
    private final Context context;
    private List<HueBridge> data;

    public HueBridgeViewAdapter(Context context, List<HueBridge> data)
    {
        super(context, 0, data);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        return getConvertView(position, R.layout.bridge_view_adapter, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        return getConvertView(position, R.layout.bridge_view_adapter_dropdown, convertView, parent);
    }

    private View getConvertView(int position, int layoutId, View view, ViewGroup parent)
    {
        HueBridge item = data.get(position);
        if (view == null)
        {
            view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        }
        TextView title = view.findViewById(R.id.bridge_view_adapter_item_title);
        title.setText(item.getDeviceName());
        return view;
    }
}
