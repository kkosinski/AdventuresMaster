package com.wintermute.adventuresmaster.database.dao.tools.gm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Light;

/**
 * Manages data access for light. Bug in RoomDatabase forces the naming of parameters passed into queries to be * like
 * arg0, etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface LightDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Light light);

    /**
     * @param arg0 id of target scene.
     * @return light configured for scene
     */
    @Query("SELECT * FROM light WHERE inScene = :arg0")
    LiveData<Light> getLightForScene(long arg0);
}
