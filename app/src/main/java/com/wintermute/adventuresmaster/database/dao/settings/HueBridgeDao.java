package com.wintermute.adventuresmaster.database.dao.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;

import java.util.List;

/**
 * Manages hue bridge access.
 *
 * @author wintermute
 */
@Dao
public interface HueBridgeDao
{
    @Insert
    long insert(HueBridge target);

    @Query("SELECT * FROM hueBridge WHERE url = :url")
    HueBridge getByUrl(String url);

    @Query("SELECT * FROM hueBridge WHERE defaultDevice = 1")
    HueBridge getDefaultDevice();

    @Update
    void update(HueBridge target);

    @Query("SELECT * FROM hueBridge ORDER BY defaultDevice DESC")
    LiveData<List<HueBridge>> getAll();
}
