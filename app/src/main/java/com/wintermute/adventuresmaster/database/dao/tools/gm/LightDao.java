package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
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

    @Query("SELECT * FROM light WHERE inScene = :sceneId")
    LiveData<Light> getLightInScene(long sceneId);
}
