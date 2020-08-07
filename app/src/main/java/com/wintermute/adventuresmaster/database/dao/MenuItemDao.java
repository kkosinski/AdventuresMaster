package com.wintermute.adventuresmaster.database.dao;

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
    @Query("SELECT * FROM menu WHERE parentId = (:parentId)")
    List<MenuItem> getMenuItems(long parentId);

    @Query("SELECT * FROM menu WHERE id = (:id)")
    MenuItem getSelectedItem(long id);

    @Query("SELECT * FROM menu")
    List<MenuItem> getAll();
}
