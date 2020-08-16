package com.wintermute.adventuresmaster.viewmodel;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.menu.ActivityDesc;
import com.wintermute.adventuresmaster.database.entity.menu.ActivityExtras;
import com.wintermute.adventuresmaster.database.entity.menu.MenuItem;

import java.util.List;

/**
 * Observes changes in menu and sends notifications about changes to the activity showing menu.
 *
 * @author wintermute
 */
public class MenuViewModel extends ViewModel
{
    /**
     * @param ctx of calling activity.
     * @return top level items in main menu.
     */
    public LiveData<List<MenuItem>> getTopLevelItems(Context ctx)
    {
        return AppDatabase.getAppDatabase(ctx).menuItemDao().getItemsByParentId(-1L);
    }

    /**
     * @param ctx of calling activity.
     * @param target menu item that has been selected.
     * @return selected menu item.
     */
    public LiveData<List<MenuItem>> getSelectedItemContent(Context ctx, MenuItem target)
    {
        return AppDatabase.getAppDatabase(ctx).menuItemDao().getItemsByParentId(target.getId());
    }

    /**
     * @param ctx of calling activity.
     * @param target menu item that has been selected.
     * @return its children menu item elements.
     */
    public LiveData<List<MenuItem>> getItemParentContent(Context ctx, MenuItem target)
    {
        return AppDatabase.getAppDatabase(ctx).menuItemDao().getItemsByParentId(target.getParentId());
    }

    /**
     * @param ctx of calling activity.
     * @param target menu item that has been selected.
     * @return its parent menu item element.
     */
    public LiveData<MenuItem> getItemParent(Context ctx, MenuItem target)
    {
        return AppDatabase.getAppDatabase(ctx).menuItemDao().getParent(target.getParentId());
    }

    /**
     * @param ctx of calling activity.
     * @param target menu item that has been selected.
     * @return activity description of selected menu item.
     */
    public LiveData<ActivityDesc> getActivity(Context ctx, MenuItem target)
    {
        return AppDatabase.getAppDatabase(ctx).activityDescDao().getActivityDesc(target.getId());
    }

    /**
     * @param ctx of calling activity.
     * @param target activity description of menu item that has been selected.
     * @return activity extras of selected activity.
     */
    public LiveData<List<ActivityExtras>> getActivityExtras(Context ctx, ActivityDesc target)
    {
        return AppDatabase.getAppDatabase(ctx).activityExtrasDao().getExtrasForActivity(target.getActivityId());
    }
}
