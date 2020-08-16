package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;

/**
 * SceneDao as database access object.
 *
 * @author wintermute
 */
@Dao
public interface SceneDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Scene scene);

}
