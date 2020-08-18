package com.wintermute.adventuresmaster.dynamiclist;

import android.content.Context;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.adventuresmaster.database.entity.menu.Board;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides some help methods to initialize and use the dynamic list and map the items to the {@link DynamicListItem}t
 *
 * @author wintermute
 */
public class DynamicListHelper
{

    public static DynamicListHelper INSTANCE;
    private DynamicAdapter adapter;
    private List<DynamicListItem> dynamicItems;

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

    public RecyclerView initRecyclerView(Context ctx, RecyclerView recyclerView,
                                         DynamicAdapter.ItemClickListener listItemClickListener)
    {
        LinearLayoutManager layout = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layout);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), layout.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(initAdapter(ctx, listItemClickListener));
        return recyclerView;
    }

    private DynamicAdapter initAdapter(Context ctx, DynamicAdapter.ItemClickListener listItemClickListener)
    {
        dynamicItems = new ArrayList<>();
        adapter = new DynamicAdapter(ctx, dynamicItems);
        adapter.setClickListener(listItemClickListener);
        return adapter;
    }

    public void updateBoards(List<Board> items)
    {
        dynamicItems.clear();
        items.forEach(i -> dynamicItems.add(new DynamicListItem(i.getName(), i.getId())));
        adapter.notifyDataSetChanged();
    }

    public void updateScenes(List<Scene> items) {
        dynamicItems.clear();
        items.forEach(i -> dynamicItems.add(new DynamicListItem(i.getTitle(), i.getId())));
        adapter.notifyDataSetChanged();
    }
}
