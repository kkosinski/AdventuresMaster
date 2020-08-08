package com.wintermute.adventuresmaster.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.MenuItem;

import java.util.List;

/**
 * Represents the menu item as database access object.
 */
@Dao
public interface MenuItemDao
{
    @Query("SELECT * FROM menu WHERE parentId = :parentId")
    LiveData<List<MenuItem>> getMenuItems(long parentId);

    @Query("SELECT * FROM menu WHERE id = :parentId")
    LiveData<MenuItem> getParent(long parentId);
}
