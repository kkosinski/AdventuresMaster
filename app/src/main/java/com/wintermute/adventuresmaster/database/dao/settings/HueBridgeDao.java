package com.wintermute.adventuresmaster.database.dao.settings;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;

/**
 * Manages hue bridge access.
 *
 * @author wintermute
 */
@Dao
public interface HueBridgeDao
{
    @Insert
    long insert(HueBridge hueBridge);

    @Query("SELECT * FROM hueBridge WHERE url = :url")
    HueBridge getByUrl(String url);
}
