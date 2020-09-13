package com.wintermute.adventuresmaster.database.dao.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;

import java.util.List;

/**
 * Manages hue bridge access. Bug in RoomDatabase forces the naming of parameters passed into queries to be like arg0,
 * etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface HueBridgeDao
{
    @Insert
    long insert(HueBridge target);

    /**
     * @param arg0 url to reach the philips hue bridge.
     * @return philips hue bridge selected by unique url.
     */
    @Query("SELECT * FROM hueBridge WHERE url = :arg0")
    HueBridge getByUrl(String arg0);

    @Query("SELECT * FROM hueBridge WHERE defaultDevice = 1")
    HueBridge getDefaultDevice();

    @Update
    void update(HueBridge target);

    @Query("SELECT * FROM hueBridge ORDER BY defaultDevice DESC")
    LiveData<List<HueBridge>> getAll();
}
