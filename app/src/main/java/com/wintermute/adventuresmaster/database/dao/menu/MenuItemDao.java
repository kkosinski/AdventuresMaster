package com.wintermute.adventuresmaster.database.dao.menu;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.menu.MenuItem;

import java.util.List;

/**
 * Represents the menu item as database access object.
 */
@Dao
public interface MenuItemDao
{
    @Query("SELECT * FROM menuItem WHERE parentId = :parentId")
    LiveData<List<MenuItem>> getItemsByParentId(long parentId);

    @Query("SELECT * FROM menuItem WHERE id = :parentId")
    LiveData<MenuItem> getParent(long parentId);
}
