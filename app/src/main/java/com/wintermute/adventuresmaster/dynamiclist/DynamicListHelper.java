package com.wintermute.adventuresmaster.dynamiclist;

import android.content.Context;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Provides some help methods to initialize and use the dynamic list and map the items to the {@link DynamicListItem}t
 *
 * @author wintermute
 */
public class DynamicListHelper
{

    public static DynamicListHelper INSTANCE;

    private DynamicListHelper() {}

    /**
     * Create an instance if not existent, return created if already existing.
     *
     * @return instance of this class.
     */
    public static DynamicListHelper getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new DynamicListHelper();
        }
        return INSTANCE;
    }

    /**
     * @param ctx of calling activity.
     * @param recyclerView inside the calling activity.
     * @return a preconfigured recycler view with adapter set.
     */
    public RecyclerView initRecyclerView(Context ctx, RecyclerView recyclerView)
    {
        LinearLayoutManager layout = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layout);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), layout.getOrientation());
        recyclerView.addItemDecoration(divider);
        return recyclerView;
    }

    /**
     * @param ctx of calling activity.
     * @param items to put into adapter.
     * @param listItemClickListener to attach to adapter.
     * @return preconfigured dynamic adapter for the {@link DynamicAdapter}.
     */
    public DynamicAdapter initAdapter(Context ctx, List<DynamicListItem> items,
                                      DynamicAdapter.ItemClickListener listItemClickListener)
    {
        DynamicAdapter adapter = new DynamicAdapter(ctx, items);
        adapter.setClickListener(listItemClickListener);
        return adapter;
    }
}
