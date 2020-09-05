package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Light;

/**
 * Manages data access for light.
 *
 * @author wintermute
 */
@Dao
public interface LightDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Light light);
}
