package com.wintermute.adventuresmaster.database.dao.menu;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.menu.MenuItem;

import java.util.List;

/**
 * Represents the menu item as database access object. Bug in RoomDatabase forces the naming of parameters passed into
 * queries to be like arg0, etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface MenuItemDao
{
    /**
     * @param arg0 id of menu item´s parent.
     * @return list of children to requested id.
     */
    @Query("SELECT * FROM menuItem WHERE parentId = :arg0")
    LiveData<List<MenuItem>> getItemsByParentId(long arg0);

    /**
     * @param arg0 id of menu item´s parent.
     * @return parent item self.
     */
    @Query("SELECT * FROM menuItem WHERE id = :arg0")
    LiveData<MenuItem> getParent(long arg0);
}
