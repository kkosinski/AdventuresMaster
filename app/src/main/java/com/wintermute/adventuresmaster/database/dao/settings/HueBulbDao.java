package com.wintermute.adventuresmaster.database.dao.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.wintermute.adventuresmaster.database.entity.settings.HueBulb;

import java.util.List;

/**
 * Manages access for hue bulbs. Bug in RoomDatabase forces the naming of parameters passed into queries to be like
 * arg0, etc. Otherwise tests does not work.
 *
 * @author wintermute
 */
@Dao
public interface HueBulbDao
{
    @Insert
    void insert(HueBulb hueBulb);

    @Delete
    void delete(HueBulb hueBulb);

    /**
     * @param arg0 bridge id to request philips hue bulbs.
     * @return list of paired bulbs for requested bridge id as live data.
     */
    @Query("SELECT * FROM hueBulb WHERE hueBridge = :arg0")
    LiveData<List<HueBulb>> getPairedBulbsDynamically(long arg0);

    /**
     * @param arg0 bridge id to request philips hue bulbs.
     * @return list of paired bulbs for requested bridge id.
     */
    @Query("SELECT * FROM hueBulb WHERE hueBridge = :arg0")
    List<HueBulb> getPairedBulbs(long arg0);
}
