package com.wintermute.adventuresmaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.wintermute.adventuresmaster.view.ViewItem;

import java.util.List;

/**
 * Generic adapter for list views.
 *
 * @author wintermute
 */
public class ListAdapter extends BaseAdapter
{
    private List<ViewItem> items;
    private LayoutInflater inflater;

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return null;
    }
}
