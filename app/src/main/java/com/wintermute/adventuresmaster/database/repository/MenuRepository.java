package com.wintermute.adventuresmaster.database.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.dao.menu.ActivityDescDao;
import com.wintermute.adventuresmaster.database.dao.menu.ActivityExtrasDao;
import com.wintermute.adventuresmaster.database.dao.menu.MenuItemDao;
import com.wintermute.adventuresmaster.database.entity.menu.ActivityDesc;
import com.wintermute.adventuresmaster.database.entity.menu.ActivityExtras;
import com.wintermute.adventuresmaster.database.entity.menu.MenuItem;

import java.util.List;

/**
 * Data cache for items important for menu.
 *
 * @author wintermute
 */
public class MenuRepository
{
    private ActivityDescDao activityDescDao;
    private ActivityExtrasDao activityExtrasDao;
    private MenuItemDao menuItemDao;

    /**
     * Creates an instance.
     *
     * @param context of calling activity.
     */
    public MenuRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(context);
        activityDescDao = appDatabase.activityDescDao();
        activityExtrasDao = appDatabase.activityExtrasDao();
        menuItemDao = appDatabase.menuItemDao();
    }

    /**
     * @return top level items in main menu.
     */
    public LiveData<List<MenuItem>> getTopLevelItems()
    {
        return menuItemDao.getItemsByParentId(-1L);
    }

    /**
     * @param target menu item that has been selected.
     * @return selected menu item.
     */
    public LiveData<List<MenuItem>> getSelectedItemContent(MenuItem target)
    {
        return menuItemDao.getItemsByParentId(target.getId());
    }

    /**
     * @param target menu item that has been selected.
     * @return its children menu item elements.
     */
    public LiveData<List<MenuItem>> getItemParentContent(MenuItem target)
    {
        return menuItemDao.getItemsByParentId(target.getParentId());
    }

    /**
     * @param target menu item that has been selected.
     * @return its parent menu item element.
     */
    public LiveData<MenuItem> getItemParent(MenuItem target)
    {
        return menuItemDao.getParent(target.getParentId());
    }

    /**
     * @param target menu item that has been selected.
     * @return activity description of selected menu item.
     */
    public LiveData<ActivityDesc> getActivity(MenuItem target)
    {
        return activityDescDao.getActivityDesc(target.getId());
    }

    /**
     * @param target activity description of menu item that has been selected.
     * @return activity extras of selected activity.
     */
    public LiveData<List<ActivityExtras>> getActivityExtras(ActivityDesc target)
    {
        return activityExtrasDao.getExtrasForActivity(target.getActivityId());
    }
}
